package com.company.blackjack.client.view.component;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class DBox extends PlayerBox {

    public DBox() {
        super();

        Label dealerLabel = new Label("DEALER");
        dealerLabel.setStyle("-fx-text-fill: white");

        setMinHeight(180);
        getChildren().add(0, dealerLabel);
    }

    @Override
    public void setCardShift(int numberOfCard, ImageView cardImage) {
        cardImage.setX(numberOfCard * SHIFT);
    }
}
