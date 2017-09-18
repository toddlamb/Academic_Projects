#include <stdio.h>
#include <sys/types.h>
#include <pthread.h>
#include <stdlib.h>
#include <semaphore.h>
#include "Array.h"

#define BYTES_PER_MBYTE 1048576

//*****************************************************************************************************
//NO NULL PARAMETERS SHOULD BE PROVIDED FOR THESE FUNCTIONS AS DOING SO WILL ELICIT UNDEFINED BEHAVIOUR.
//*****************************************************************************************************

//Semaphore used for ensuring all thread completion prior to print of result array at end.
sem_t sem;

//Mutex used for shared writing to the result array.
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

//Used for passing all needed info to thread.
typedef struct {
    int beginIndex;
    int endIndex;
    IntArray *searchLocation;
    IntArray *results;
    int searchVal;
} SearchParam;

//Generates a struct array, one struct of thread arguments per thread.
 void genThreadArg(SearchParam* result, int numThreads, int numCount, IntArray *searchArr, IntArray *resArray, int searchVal) {
    int totThreads = numThreads;
    for (int i = 0; i < searchArr->size; i += (numCount / totThreads)) {
        result[--numThreads].beginIndex = i;
        result[numThreads].endIndex = i + (numCount / totThreads);
        result[numThreads].searchLocation = searchArr;
        result[numThreads].searchVal = searchVal;
        result[numThreads].results = resArray;
    }
    result[0].endIndex += (numCount % totThreads);
}

//Square math function
int sqr(int x) { return x * x; }

//Adds value to an integer (for use with multi-threads)
void writeToArray(int num, IntArray *array) {
    if (pthread_mutex_lock(&mutex)) {
       printf("The mutex couldn't be locked.");
       exit(-5);
    }
    addIntToArray(num, array);
    if (pthread_mutex_unlock(&mutex)) {
       printf("The mutex couldn't be unlocked.");
       exit(-6);
    }
}


//Searches for a given value in any subset of an array. Thread exits upon completion.
void *searchArray(void *parameters) {
    sem_wait(&sem);
    sem_post(&sem);
    SearchParam* param = (SearchParam*) parameters;
    for (int i = param->beginIndex; i < param->endIndex; i++) {
        if (param->searchLocation->array[i] == param->searchVal) {
                writeToArray((i + 1),param->results);
        }
    }
    pthread_exit(NULL);
}

//Creates a thread array and passes the function to them.
pthread_t *createThreads(int numberThreads, void *function, SearchParam* arg) {
    pthread_t *threadArr = malloc(sizeof(pthread_t) * numberThreads);
    for (int i = 0; i < numberThreads; i++) {
        pthread_create(&threadArr[i], NULL, function, &(arg[i]));
        if  (&threadArr[i] == NULL) {
            printf("One or more threads couldn't be created.");
            exit(-1);
        }
    }
    return threadArr;
}


//Main waits for workers to finish before printing
void waitOnThreads(pthread_t* threads, int threadCount) {
    for (int i = 0; i < threadCount; i++) {
        if (pthread_join(threads[i],NULL)) {
          printf("An error has occured while attempting to join program threads."); 
          exit(-2);
        }
    }
}


//Main takes in <file name> <number to search for> <number of threads> <input size in MB>
int main(int argc, char *argv[]) {
    //initialize all variables prior to i/o
    int totBytes = atoi(argv[4]) * BYTES_PER_MBYTE;
    int numCount = totBytes / sizeof(int);
    IntArray *searchArr = newIntArray(numCount);
    IntArray *resultArray = newIntArray(1000);
    if (sem_init(&sem,0,0) == -1) {
       printf("The semaphore couldn't be created");
       exit(-3);
    }
    SearchParam* param = malloc(sizeof(SearchParam) * atoi(argv[3]) - 1);
    //initializes threads with locked semaphore
    pthread_t* threads = createThreads(atoi(argv[3]),searchArray,param);
    //Do file i/o and read into array
    addFileNumToArray(searchArr, argv[1]);
    //Takes necessary thread arguments and splits up search indexes for thread.
    genThreadArg(param,atoi(argv[3]), numCount, searchArr, resultArray, atoi(argv[2]));
    //Release threads for array search and write to
    sem_post(&sem);
    //Wait on all threads to complete before continuing
    waitOnThreads(threads,atoi(argv[3]));
    //Print result array
    printIntArray(resultArray);
    //Frees memory.
    free(param);
    freeIntArray(searchArr);
    freeIntArray(resultArray);
    free(threads);
    sem_destroy(&sem);
    pthread_mutex_destroy(&mutex);
}
