#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "cards.h"
#include <assert.h>
#define CARDS_PER_SUIT 13
#define DECK_SIZE 52

//
// Created by todd on 4/21/17.
//

// create a standard 52-card deck
void* create_deck() {
    void* res = malloc(sizeof(struct card*) * DECK_SIZE);
	struct card** deck = (struct card**) res;
	for (int i = 0; i < DECK_SIZE; i += CARDS_PER_SUIT) {
		for (int j = two; j <= ace; j++) {
			deck[i+j] = malloc(sizeof(struct card));
            deck[i+j]->card_suit = i/CARDS_PER_SUIT;
			deck[i+j]->card_rank = j;
		}
	}
	return res;
}


// Return the number of cards left in the deck
int number_of_cards_remaining_in_deck(void* deck) {
	struct card** cards = (struct card**) deck;
	int i = 0;
	while (cards[++i] && i < DECK_SIZE){}
	return i;
}

// free the deck memory
void destroy_deck(void* deck) {
	struct card** cards = (struct card**) deck;
	int cardCount= number_of_cards_remaining_in_deck(deck);
	for (int i = 0; i < cardCount; i++) {
        if (cards[i]) {
            free(cards[i]);
        }
	}
    free(deck);
}

// return a card, removing it from the deck
struct card* deal_card(void* deck) {
    struct card** cards = (struct card**) deck;
	srand(31);
	int location = rand() % number_of_cards_remaining_in_deck(deck);
	struct card* res = cards[location];
    int deckCount = number_of_cards_remaining_in_deck(deck);
	for (int i = location; i < deckCount - 1; i++) {
	  cards[i] = cards[i+1];
	}
	cards[deckCount-1] = NULL;
	return res;
}

// return the number of cards indicated and remove them from the deck
struct card** deal_cards(void* deck, int num_cards) {
	struct card** res = malloc(sizeof(struct card) * num_cards);
	for (int i = 0; i < num_cards; i++) {
		res[i] = deal_card(deck);
	}
	return res;
}


// This will return the remaining cards in the deck, in their current order, without removing any
struct card** peek_at_deck(void* deck) {
    struct card** cards = (struct card**) deck;
	struct card** res = calloc(DECK_SIZE,sizeof(struct card*));
    for (int i = 0; i < number_of_cards_remaining_in_deck(deck); i++) {
        res[i] = malloc(sizeof(struct card));
        void* dest = res[i];
        void* src = (void*)cards[i];
        memcpy(dest,src,sizeof(struct card));
    }
	return res;
}

//Print deck
void printDeck(void* deck) {
    struct card** cards = (struct card**) deck;
    for (int i = 0; i < number_of_cards_remaining_in_deck(deck); i++) {
        printf("%s\n",to_string(cards[i]));
    }
    printf("\n");
}

//Returns the suit of a given card as a string.
char* suitString(struct card* card1) {
    switch (card1->card_suit) {
        case hearts:
            return "hearts";
        case diamonds:
            return "diamonds";
        case spades:
            return "spades";
        case clubs:
            return "clubs";
        default:
            return NULL;
    }
}
//Gets the card type as a string.
char* cardString(struct card* card1) {
    switch (card1->card_rank) {
        case two:
            return "two";
        case three:
            return "three";
        case four:
            return "four";
        case five:
            return "five";
        case six:
            return "six";
        case seven:
            return "seven";
        case eight:
            return "eight";
        case nine:
            return "nine";
        case ten:
            return "ten";
        case jack:
            return "jack";
        case queen:
            return "queen";
        case king:
            return "king";
        case ace:
            return "ace";
        default:
            return NULL;
    }
}

// Return a string describing a card e.g. "four of hearts" or "king of clubs"
char* to_string(struct card* my_card) {
    char* result = calloc(50,sizeof(char));
    strcat(result,cardString(my_card));
    strcat(result," of ");
    strcat(result,suitString(my_card));
	return result;
}


void testDeck() {
    void* deck = create_deck();
    struct card** cards = (struct card**) deck;
    char* cardToString = to_string(cards[0]);
    assert(strcmp("two of hearts",cardToString) == 0);
    free(cardToString);
    assert(number_of_cards_remaining_in_deck(deck) == DECK_SIZE);
    struct card* card = deal_card(deck);
    assert(card->card_suit >= hearts && card->card_suit <= clubs);
    assert(card->card_rank >= two && card->card_rank <= ace);
    free(card);
    assert(number_of_cards_remaining_in_deck(deck) == 51);
    cards = deal_cards(deck,4);
    assert(number_of_cards_remaining_in_deck(deck) == 47);
    destroy_deck((void*)cards);
    struct card** deck_peek = peek_at_deck(deck);
    assert(number_of_cards_remaining_in_deck(deck) == 47);
    assert(number_of_cards_remaining_in_deck(deck_peek) == 47);
    destroy_deck((void*)deck_peek);
    destroy_deck(deck);
};
