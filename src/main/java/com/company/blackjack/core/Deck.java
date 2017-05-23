package com.company.blackjack.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    private List<Card> cards = new LinkedList<Card>();

    public Deck() {
        this(false);
    }

    public Deck(boolean shuffle) {
        refill();
        if(shuffle) {
            shuffle();
        }
    }

    private void refill() {
        cards.clear();

        for(Card.Suit suit : Card.Suit.values()) {
            for(Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public int size() {
        return cards.size();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealUp() {
        Card card = deal();
        card.setFaceUp(true);

        return card;
    }

    public Card dealDown() {
        Card card = deal();
        card.setFaceUp(false);

        return card;
    }

    private Card deal() {
        if(cards.isEmpty()) {
            refill();
            shuffle();
        }

        return cards.remove(cards.size() - 1);
    }
}
