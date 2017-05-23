package com.company.blackjack;


import com.company.blackjack.core.Card;
import com.company.blackjack.core.Hand;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HandTests {

    private Hand hand;
    private int score;

    public HandTests(Hand hand, int score) {
        this.hand = hand;
        this.score = score;
    }

    @Test
    public void testTotal() {
        assertEquals(score, hand.total());
    }

    @Test
    public void testIsBlackjack() {
        assertEquals(score == 21 && hand.size() == 2, hand.isBlackjack());
    }

    @Test
    public void testIsBust() {
        assertEquals(score > 21, hand.isBust());
    }

    @Parameterized.Parameters
    public static Collection handsCollection() {
        return Arrays.asList(new Object[][] {
                { provideKingAndAce().getKey(), provideKingAndAce().getValue() },
                { provideSixSevenAndEight().getKey(), provideSixSevenAndEight().getValue() },
                { provideKingQueenAndEight().getKey(), provideKingQueenAndEight().getValue() },
                { provideAceKingAndQueen().getKey(), provideAceKingAndQueen().getValue() },
                { provideAceFiveAndAce().getKey(), provideAceFiveAndAce().getValue() }
        });
    }

    private static Pair<Hand, Integer> provideKingAndAce() {
        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.KING));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE));
        hand.turnOver();

        return new Pair<>(hand, Card.Rank.KING.getValue() + Card.Rank.ACE.getValue());
    }

    private static Pair<Hand, Integer> provideSixSevenAndEight() {
        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.SIX));
        hand.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.SEVEN));
        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.EIGHT));
        hand.turnOver();

        return new Pair<>(
                hand, Card.Rank.SIX.getValue() + Card.Rank.SEVEN.getValue() + Card.Rank.EIGHT.getValue()
        );
    }

    private static Pair<Hand, Integer> provideKingQueenAndEight() {
        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.KING));
        hand.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN));
        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.EIGHT));
        hand.turnOver();

        return new Pair<>(
                hand, Card.Rank.KING.getValue() + Card.Rank.QUEEN.getValue() + Card.Rank.EIGHT.getValue()
        );
    }

    private static Pair<Hand, Integer> provideAceKingAndQueen() {
        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE));
        hand.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.KING));
        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.QUEEN));
        hand.turnOver();

        return new Pair<>(
                hand, Card.Rank.KING.getValue() + Card.Rank.QUEEN.getValue() + 1
        );
    }

    private static Pair<Hand, Integer> provideAceFiveAndAce() {
        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE));
        hand.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE));
        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE));
        hand.turnOver();

        return new Pair<>(
                hand, Card.Rank.ACE.getValue() + Card.Rank.FIVE.getValue() + 1
        );
    }
}
