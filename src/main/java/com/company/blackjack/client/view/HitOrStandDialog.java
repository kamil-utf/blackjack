package com.company.blackjack.client.view;

import com.company.blackjack.client.view.component.TimeoutDialog;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HitOrStandDialog extends TimeoutDialog<ButtonType> {

    private final HBox hBox;
    private final Label timerLabel;

    public HitOrStandDialog(int timeout) {
        super(timeout);

        final DialogPane dialogPane = getDialogPane();

        this.hBox = new HBox();
        this.hBox.setSpacing(10);
        this.hBox.setAlignment(Pos.CENTER_LEFT);

        this.hBox.getChildren().add(new Label("It's time to choose..."));

        this.timerLabel = new Label();
        this.timerLabel.setPadding(new Insets(0, 0, 0, 15));
        this.timerLabel.textProperty().bind(Bindings.concat(getTimerProperty().asString(), " s"));
        this.hBox.getChildren().add(timerLabel);

        dialogPane.setContent(hBox);

        setTitle("Hit Or Stand");

        // -- buttons
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button hitButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        hitButton.setText("Hit");
        Button standButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        standButton.setText("Stand");

        Platform.runLater(() -> startTimer());
    }
}
