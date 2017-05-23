package com.company.blackjack.client.view;

import com.company.blackjack.client.view.component.NumberTextField;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

public class ConnectionDialog extends Dialog<Pair<String, Integer>> {

    private static final String HOST = "Host";
    private static final String PORT = "Port";

    private final HBox hBox;
    private final TextField hostTextField;
    private final NumberTextField portTextField;

    public ConnectionDialog() {
        final DialogPane dialogPane = getDialogPane();

        this.hBox = new HBox();
        this.hBox.setSpacing(10);
        this.hBox.setAlignment(Pos.CENTER_LEFT);

        this.hostTextField = new TextField();
        this.hBox.getChildren().addAll(new Label(HOST + ":"), hostTextField);

        this.portTextField = new NumberTextField();
        this.portTextField.setPrefWidth(48.0);
        this.hBox.getChildren().addAll(new Label(PORT + ":"), portTextField);

        dialogPane.setContent(hBox);

        setTitle("New Connection Wizard");
        setHeaderText("Blackjack Server Connection");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        validate();
        Platform.runLater(() -> hostTextField.requestFocus());

        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == ButtonBar.ButtonData.OK_DONE) {
                return new Pair<>(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
            } else {
                return null;
            }
        });
    }

    private void validate() {
        Node connectButton = getDialogPane().lookupButton(ButtonType.OK);
        connectButton.setDisable(true);

        hostTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            connectButton.setDisable(newValue.trim().isEmpty() || portTextField.getText().isEmpty());
        });

        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            connectButton.setDisable(newValue.trim().isEmpty() || hostTextField.getText().isEmpty());
        });
    }
}
