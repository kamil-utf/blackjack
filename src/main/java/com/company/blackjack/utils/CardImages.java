package com.company.blackjack.utils;

import com.company.blackjack.core.Card;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class CardImages {

    public static final int CARD_WIDTH = 100;
    public static final int CARD_HEIGHT = 145;

    private static CardImages instance = null;
    private Map<String, Image> store;

    private CardImages() {
        store = new HashMap<>();
    }

    public static CardImages getInstance() {
        if(instance == null) {
            instance = new CardImages();
        }

        return instance;
    }

    public void load() {
        for(Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                String key = rank.toString() + "_OF_" + suit.toString();
                Image image = new Image(
                        getClass().getResource("/images/" + key.toLowerCase() + ".png").toExternalForm()
                );

                store.putIfAbsent(key, image);
            }
        }

        // Hidden card
        String key = "HIDDEN";
        Image image = new Image(
                getClass().getResource("/images/" + key.toLowerCase() + ".png").toExternalForm()
        );
        store.putIfAbsent(key, image);
    }

    public Image get(String key) {
        return store.get(key);
    }
}
