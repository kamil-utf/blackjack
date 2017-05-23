package com.company.blackjack.client.view.component;

import com.company.blackjack.players.Player;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

public class PBox extends PlayerBox {

    private final static String BET = "Bet: ";

    private final Label nameLabel;
    private final Label betLabel;
    private boolean upated = false;

    public PBox() {
        super();

        this.betLabel = new Label();
        this.betLabel.setStyle("-fx-text-fill: white");
        getChildren().add(betLabel);

        this.nameLabel = new Label();
        this.nameLabel.setStyle("-fx-text-fill: white");
        getChildren().add(0, nameLabel);
    }

    @Override
    public void setCardShift(int numberOfCard, ImageView cardImage) {
        cardImage.setX(numberOfCard * SHIFT);
        cardImage.setY(numberOfCard * SHIFT);
    }

    public void update(JSONObject playerJSON, boolean me) {
        super.update(playerJSON);

        displayName(me ? "YOU" : null);
        if(playerJSON.has(Player.BET_TAG)) {
            displayBet(playerJSON.getInt(Player.BET_TAG));
        }

        upated = true;
    }

    @Override
    public void clear() {
        super.clear();
        displayName(null);
        displayBet(0);
    }

    private void displayName(String name) {
        nameLabel.setText(name);
    }

    private void displayBet(int bet) {
        betLabel.setText(BET + bet + "$");
    }

    public boolean isUpated() {
        return upated;
    }

    public void setNonUpated() {
        upated = false;
    }

    public void destroy() {
        getChildren().clear();
    }
}
