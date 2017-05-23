package com.company.blackjack.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Hand {

    public static final String TOTAL_TAG = "total";
    private static final int BLACKJACK = 21;

    private List<Card> cards = new LinkedList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    public int total() {
        int score = 0;
        int numberOfAces = 0;

        List<Card> tmpCards = new LinkedList<>(cards);
        tmpCards.removeIf(card -> !card.isFaceUp());

        for(Card card : tmpCards) {
            if(card.getRank() == Card.Rank.ACE) {
                numberOfAces++;
            }

            score += card.getRank().getValue();
        }

        while(score > BLACKJACK && numberOfAces > 0) {
            score -= 10;
            numberOfAces--;
        }

        return score;
    }

    public void clear() {
        cards.clear();
    }

    public int size() {
        return cards.size();
    }

    public boolean isGreaterThan(Hand hand) {
        return this.total() > hand.total();
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && total() == BLACKJACK;
    }

    public boolean isBust() {
        return total() > BLACKJACK;
    }

    public void turnOver() {
        cards.forEach(card -> card.setFaceUp(true));
    }

    public JSONArray getCardsJSON() {
        JSONArray cardsJSON = new JSONArray();
        for(Card card : cards) {
            JSONObject cardJSON = new JSONObject();
            cardJSON.put(Card.NAME_TAG, card);
            cardsJSON.put(cardJSON);
        }

        return cardsJSON;
    }
}
