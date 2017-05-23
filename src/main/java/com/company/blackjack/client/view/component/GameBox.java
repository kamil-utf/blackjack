package com.company.blackjack.client.view.component;

import com.company.blackjack.players.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameBox extends HBox {

    private static final String MONEY = "Balance: ";

    // DealerBox
    private final DBox dBox;

    // PlayerId
    private final String playerId;

    // PlayersBox
    private final Map<String, PBox> pBoxes;

    private final Label moneyLabel;

    private final Pane topBar;
    private final Pane statusBar;

    public GameBox(String playerId, BorderPane borderPane) {
        this.playerId = playerId;
        this.pBoxes = new HashMap<>();

        // DealerBox
        this.dBox = new DBox();
        this.topBar = (Pane) borderPane.getTop();
        this.topBar.getChildren().add(dBox);

        // MoneyLabel
        this.moneyLabel = new Label();
        this.moneyLabel.setStyle("-fx-text-fill: white");
        this.moneyLabel.setPadding(new Insets(5, 15, 5, 5));

        HBox statusItems = new HBox(moneyLabel);
        statusItems.setAlignment(Pos.CENTER_RIGHT);
        statusItems.setId("status-items");

        // PlayersBox
        this.statusBar = (Pane) borderPane.getBottom();
        this.statusBar.getChildren().addAll(this, statusItems);

        setAlignment(Pos.CENTER);
        setSpacing(30);
    }

    public void updatePBox(JSONObject playerJSON) {
        String id = playerJSON.getString(Player.ID_TAG);
        pBoxes.computeIfAbsent(id, key -> {
            PBox pBox = new PBox();
            getChildren().add(pBox);
            return pBox;
        }).update(playerJSON, id.equals(playerId));
    }

    public void updateDBox(JSONObject dealerJSON) {
        dBox.update(dealerJSON);
    }

    public void updateMoney(int sum) {
        moneyLabel.setText(MONEY + sum + "$");
    }

    public void initNewGameState() {
        pBoxes.values().forEach(PBox::setNonUpated);
    }

    public void clean() {
        Iterator<Map.Entry<String, PBox>> it = pBoxes.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, PBox> entry = it.next();
            PBox pBox = entry.getValue();

            if(!pBox.isUpated()) {
                pBox.destroy();
                it.remove();
            }
        }
    }

    public void destroy() {
        topBar.getChildren().clear();
        statusBar.getChildren().clear();
    }
}
