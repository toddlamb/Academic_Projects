//
// Created by todd on 4/21/17.
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include "cards.h"
#define MAX_SCORE 21

typedef enum {false,true} boolean;

//Determines the number of aces in a hand.
int aceCount(struct card** hand, int cardCount) {
    int result = 0;
    for (int i = 0; i < cardCount; i++) {
        if (hand[i]->card_rank == ace) {
            result++;
        }
    }
    return result;
}

//Calculates the value of a card. For aces, default value is one.
int cardValue(struct card* card1) {
    if (card1->card_rank <= ten) {
        return card1->card_rank + 2;
    } else if(card1->card_rank < ace) {
        return 10;
    } else {
        return 1; //not sure if 1 or 11 should go here yet.
    }
}

/*Takes a base hand value and accounts for any aces by adjusting the base value to give the player the best possible
  score without busting. */
int optimizeAceValues(int handValue, int aceCount) {
    for (int i = 0; i < aceCount && handValue <= MAX_SCORE; i++) {
        if ((handValue + 10) <= MAX_SCORE) {
            handValue += 10;
        }
    }
    return handValue;
}

//Calculates the optimal value of a hand
int handValue(struct card** hand, int cardCount) {
    int handValue = 0;
    for (int i = 0; i < cardCount; i++) {
      handValue += cardValue(hand[i]);
    }
    return optimizeAceValues(handValue,aceCount(hand,cardCount));
}

//Determines if two hands have equal value.
boolean handsEqual(struct card** hand_one, int num_cards_hand_one,
                   struct card** hand_two, int num_cards_hand_two) {
    return handValue(hand_one,num_cards_hand_one) == handValue(hand_two,num_cards_hand_two);
}

//Determines if a given hand exceeds the max score.
boolean handIsBust(struct card** hand, int cardCount) {
    return handValue(hand,cardCount) > MAX_SCORE;
}

// Compare two blackjack hands. Return a value < 0 if hand_one wins, a value of 0
// if the hands are equal and a value > 0 if hand_two wins
int blackjack_compare(struct card** hand_one, int num_cards_hand_one,
                      struct card** hand_two, int num_cards_hand_two) {
    if (handsEqual(hand_one,num_cards_hand_one,hand_two,num_cards_hand_two)
        || (handIsBust(hand_one,num_cards_hand_one) && handIsBust(hand_two,num_cards_hand_two))) {
        return 0;
    } else if (handIsBust(hand_one,num_cards_hand_one)) {
        return 1;
    } else if (handIsBust(hand_two,num_cards_hand_two)) {
        return -1;
    } else {
        return handValue(hand_two,num_cards_hand_two) > handValue(hand_one,num_cards_hand_one) ? 1:-1;
    }
}

// Blackjack to_string, should return the value of the hand or "BUST" if the total
// is over 21
char* blackjack_to_string(struct card** my_hand, int num_cards_in_hand) {
   int handVal = handValue(my_hand,num_cards_in_hand);
    char* res = malloc(sizeof(char) * 4);
    if (handVal > 21) {
        strcpy(res,"BUST");
    } else {
        snprintf(res, 4, "%d", handVal);
    }
    return res;
}

void testBlackJack() {
    struct card aceCard = {spades,ace};
    struct card tenCard = {hearts,ten};
    struct card fiveCard = {clubs,five};
    struct card queenCard = {diamonds,queen};
    struct card eightCard = {hearts,eight};
    struct card* blackjack[1];
    blackjack[0] = &aceCard;
    blackjack[1] = &tenCard;
    struct card* fifteenhand[1];
    fifteenhand[0] = &tenCard;
    fifteenhand[1] = &fiveCard;
    struct card* aceAndFive[1];
    aceAndFive[0] = &aceCard;
    aceAndFive[1] = &fiveCard;
    struct card* bustedHand[3];
    bustedHand[0] = &tenCard;
    bustedHand[1] = &fiveCard;
    bustedHand[2] = &aceCard;
    bustedHand[3] = &queenCard;
    struct card* bustedHand2[4];
    bustedHand2[0] = &eightCard;
    bustedHand2[1] = &queenCard;
    bustedHand2[2] = &fiveCard;
    assert(aceCount(fifteenhand,2) == 0);
    assert(aceCount(aceAndFive,2) == 1);
    assert(handValue(blackjack,2) == 21);
    assert(handValue(fifteenhand,2) == 15);
    assert(handValue(aceAndFive,2) == 16);
    assert(handValue(bustedHand2,3) == 23);
    assert(handValue(bustedHand,4) == 26);
    assert(blackjack_compare(fifteenhand,2,fifteenhand,2) == 0);
    assert(blackjack_compare(bustedHand,4,bustedHand2,3) == 0);
    assert(blackjack_compare(aceAndFive,2,fifteenhand,2) == -1);
    assert(blackjack_compare(aceAndFive,2,blackjack,2) == 1);
    char* blackJackStr = blackjack_to_string(blackjack,2);
    char* fifteenStr = blackjack_to_string(fifteenhand,2);
    char* bustedHandStr = blackjack_to_string(bustedHand,4);
    char* bustedHand2Str = blackjack_to_string(bustedHand2,3);
    assert(strcmp(blackJackStr,"21") == 0);
    assert(strcmp(fifteenStr,"15") == 0);
    assert(strcmp(bustedHandStr,"BUST") == 0);
    assert(strcmp(bustedHand2Str,"BUST") == 0);
    free(blackJackStr);
    free(fifteenStr);
    free(bustedHandStr);
    free(bustedHand2Str);
}
