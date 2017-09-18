#include <stdio.h>
#include <stdlib.h>

//Defines a re-sizable array type.
typedef struct {
    int *array;
    int length;
    int size;
} IntArray;

//Creates a new re-sizable array.
IntArray *newIntArray(int length) {
    IntArray *res = malloc(sizeof(IntArray));
    res->array = (int *) calloc(length, sizeof(int));
    res->length = length;
    res->size = 0;
    return res;
}

//Expands the array if the array is full.
void increaseIntArray(int increaseBy, IntArray *array) {
    array->length += increaseBy;
    array->array = realloc(array->array, sizeof(array->array) + (sizeof(array->array[0]) * increaseBy));
}

//Frees the given resizable-array.
void freeIntArray(IntArray *array) {
    free(array->array);
    free(array);
}

//Adds an int to the array.
void addIntToArray(int addVal, IntArray *array) {
    if (array->size == array->length) {
        increaseIntArray(10, array);
        array->array[(array->size)] = addVal;
        (*array).size++;
    } else {
        int index = array->size;
        array->array[index] = addVal;
        (*array).size++;
    }
}

//Prints the given array
void printIntArray(IntArray *array) {
    printf("{%d",array->array[0]);
    for (int i = 1; i < array->size; i++) {
        printf(",%d", array->array[i]);
    }
    printf("}");
}

//Loads a list of numbers in a file to the given array.
void addFileNumToArray(IntArray *array, char *filePath) {
    if (*filePath == '.') { return; }
    FILE *file = fopen(filePath, "r");
    char str[1024];
    int val;
    while (fgets(str,10,file) != NULL) {
        val = atoi(str);
        addIntToArray(val, array);
    }
}














 










