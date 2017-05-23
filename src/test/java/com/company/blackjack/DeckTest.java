package com.company.blackjack;

import com.company.blackjack.core.Card;
import com.company.blackjack.core.Deck;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class DeckTest {

    private static final int DECK_SIZE = 52;

    private Deck deck;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void initWithoutShuffle_Deck_setOf52PlayingCards() {
        assertEquals(DECK_SIZE, deck.size());

        for(Card.Suit suit : reverse(Card.Suit.values())) {
            for(Card.Rank rank : reverse(Card.Rank.values())) {
                Card expectedCard = new Card(suit, rank);
                expectedCard.setFaceUp(true);

                assertEquals(expectedCard.toString(), deck.dealUp().toString());
            }
        }
    }

    @Test
    public void dealUp_Deck_faceUpCard() {
        assertTrue(deck.dealUp().isFaceUp());
    }

    @Test
    public void dealDown_Deck_faceDownCard() {
        assertFalse(deck.dealDown().isFaceUp());
    }

    private static <T> T[] reverse(T[] arrayToReverse){
        Collections.reverse(Arrays.asList(arrayToReverse));
        return arrayToReverse;
    }
}
