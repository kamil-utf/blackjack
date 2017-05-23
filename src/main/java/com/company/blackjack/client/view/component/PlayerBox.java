package com.company.blackjack.client.view.component;

import com.company.blackjack.core.Card;
import com.company.blackjack.core.Hand;
import com.company.blackjack.utils.CardImages;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class PlayerBox extends VBox {

    protected final static int SHIFT = 20;
    protected final static String TOTAL = "Total: ";

    private final Group cardGroup;
    private final Label totalLabel;

    public PlayerBox() {
        this.cardGroup = new Group();
        this.totalLabel = new Label();
        this.totalLabel.setStyle("-fx-text-fill: white");

        setAlignment(Pos.CENTER);
        getChildren().addAll(cardGroup, totalLabel);
    }

    public abstract void setCardShift(int numberOfCard, ImageView cardImage);

    public void update(JSONObject jsonObject) {
        clear();

        if(jsonObject.has(Card.COLLECTION_TAG)) {
            cardGroup.getChildren().clear();

            JSONArray cardsJSON = jsonObject.getJSONArray(Card.COLLECTION_TAG);
            CardImages cardImages = CardImages.getInstance();
            for (int i = 0; i < cardsJSON.length(); i++) {
                JSONObject cardJSON = cardsJSON.getJSONObject(i);

                ImageView cardImage = new ImageView(cardImages.get(cardJSON.getString(Card.NAME_TAG)));
                cardImage.setFitWidth(CardImages.CARD_WIDTH);
                cardImage.setFitHeight(CardImages.CARD_HEIGHT);
                setCardShift(i, cardImage);
                displayCard(cardImage);
            }
        }

        if(jsonObject.has(Hand.TOTAL_TAG)) {
            displayTotal(jsonObject.getInt(Hand.TOTAL_TAG));
        }
    }

    public void clear() {
        cardGroup.getChildren().clear();
        displayTotal(0);
    }

    private void displayCard(ImageView cardImage) {
        cardGroup.getChildren().add(cardImage);
    }

    private void displayTotal(int total) {
        totalLabel.setText(TOTAL + total);
    }
}