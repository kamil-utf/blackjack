package com.company.blackjack;

import com.company.blackjack.core.Card;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTests {

    private Card card;

    @Before
    public void setUp() {
        card = new Card(Card.Suit.SPADES, Card.Rank.ACE);
    }

    @Test
    public void initCard_AceOfSpades() {
        assertEquals(Card.Suit.SPADES, card.getSuit());
        assertEquals(Card.Rank.ACE, card.getRank());
        assertEquals(Card.Rank.ACE.getValue(), card.getRank().getValue());
    }

    @Test
    public void setFaceUp_AceOfSpades_turnsOverACard() {
        card.setFaceUp(true);
        assertTrue(card.isFaceUp());

        card.setFaceUp(false);
        assertFalse(card.isFaceUp());
    }

    @Test
    public void toString_AceOfSpades_formattedNameForCard() {
        card.setFaceUp(true);
        assertEquals("ACE_OF_SPADES", card.toString());

        card.setFaceUp(false);
        assertEquals("HIDDEN", card.toString());
    }
}
