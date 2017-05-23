package com.company.blackjack.commands.fx;

import com.company.blackjack.commands.Command;
import com.company.blackjack.net.GenericSocket;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class FxAlertCommand implements Command {

    private Alert.AlertType type;
    private String message;
    private Command command;

    public FxAlertCommand(Alert.AlertType type, String message, Command command) {
        this.type = type;
        this.message = message;
        this.command = command;
    }

    @Override
    public void execute(GenericSocket handler) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name() + " Details");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait().ifPresent(buttonType -> {
            if(command != null && buttonType == ButtonType.OK) {
                command.execute(handler);
            }
        });
    }
}
