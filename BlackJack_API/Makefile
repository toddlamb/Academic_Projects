all: main

main: main.c cards.h deck_library blackjack_library
	LD_LIBRARY_PATH=./ gcc -std=c99 -o main main.c -L. -ldeck -lblackjack

run_main: main valgrind_main
	LD_LIBRARY_PATH=./ ./main

deck_library: deck.c cards.h
	gcc -std=c99 -c -Wall -Werror -fPIC deck.c
	gcc -shared -o libdeck.so deck.o

blackjack_library: blackjack.c
	gcc -std=c99 -c -Wall -Werror -fPIC blackjack.c
	gcc -shared -o libblackjack.so blackjack.o -L. -ldeck

valgrind_main: main
	LD_LIBRARY_PATH=./ valgrind -v --tool=memcheck --leak-check=yes --num-callers=20 --track-fds=yes --track-origins=yes --leak-check=full --show-leak-kinds=all ./main

clean:
	rm -r main libdeck.so deck.o blackjack.o libblackjack.so
