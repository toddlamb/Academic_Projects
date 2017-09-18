all: run_program

run_program: program1
	./program1 numbers.txt 553 4 1

program1: main.c
	gcc -pthread -o program1 main.c

clean:
	rm program1
