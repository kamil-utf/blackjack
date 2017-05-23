package com.company.blackjack.client.view;

import com.company.blackjack.client.view.component.NumberTextField;
import com.company.blackjack.client.view.component.TimeoutDialog;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BetDialog extends TimeoutDialog<Integer> {

    private static final String BET = "Bet";

    private final Integer minValue;
    private final Integer maxValue;

    private final HBox hBox;
    private final Label timerLabel;
    private final NumberTextField betTextField;

    public BetDialog(int timeout) {
        this(timeout,0, Integer.MAX_VALUE);
    }

    public BetDialog(int timeout, int minValue, int maxValue) {
        super(timeout);
        this.minValue = minValue;
        this.maxValue = maxValue;

        final DialogPane dialogPane = getDialogPane();

        this.hBox = new HBox();
        this.hBox.setSpacing(10);
        this.hBox.setAlignment(Pos.CENTER_LEFT);

        this.betTextField = new NumberTextField();
        this.hBox.getChildren().addAll(new Label(BET + ":"), betTextField);

        this.timerLabel = new Label();
        this.timerLabel.textProperty().bind(Bindings.concat(getTimerProperty().asString(), " s"));
        this.hBox.getChildren().add(timerLabel);

        dialogPane.setContent(hBox);

        setTitle("Betting");
        setHeaderText("Min. bet: " + minValue);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        validate();
        Platform.runLater(() -> {
            betTextField.requestFocus();
            startTimer();
        });

        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? Integer.parseInt(betTextField.getText()) : null;
        });
    }

    private void validate() {
        Node betButton = getDialogPane().lookupButton(ButtonType.OK);
        betButton.setDisable(true);

        betTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String currentValue = newValue.trim();
            if(!currentValue.isEmpty()) {
                int intValue = Integer.parseInt(newValue.trim());
                betButton.setDisable(intValue < minValue || intValue > maxValue);
            }
        });
    }
}
