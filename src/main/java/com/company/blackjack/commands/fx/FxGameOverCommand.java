package com.company.blackjack.commands.fx;

import com.company.blackjack.commands.ByeCommand;
import com.company.blackjack.commands.Command;
import com.company.blackjack.net.GenericSocket;
import javafx.scene.control.Alert;

public class FxGameOverCommand implements Command {

    @Override
    public void execute(GenericSocket handler) {
        handler.sendCommand(new ByeCommand());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Woops... Game Over");
        alert.setContentText("Take out a loan from wife / husband to continue...");
        alert.showAndWait();
    }
}
