package com.company.blackjack.commands.fx;

import com.company.blackjack.client.FxClientHandler;
import com.company.blackjack.client.view.HitOrStandDialog;
import com.company.blackjack.commands.Command;
import com.company.blackjack.commands.HitCommand;
import com.company.blackjack.commands.StandCommand;
import com.company.blackjack.net.GenericSocket;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class FxHitOrStandCommand implements Command {

    private Integer timeout;

    public FxHitOrStandCommand(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof FxClientHandler) {
            //GameBox pBoxList = ((FxClientHandler) handler).getpBoxList();

            HitOrStandDialog dialog = new HitOrStandDialog(timeout);
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.get() == ButtonType.OK) {
                handler.sendCommand(new HitCommand());
            } else {
                handler.sendCommand(new StandCommand());
            }
        }
    }

}
