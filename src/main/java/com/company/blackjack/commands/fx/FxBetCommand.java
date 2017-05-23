package com.company.blackjack.commands.fx;

import com.company.blackjack.client.FxClientHandler;
import com.company.blackjack.client.view.BetDialog;
import com.company.blackjack.commands.BetCommand;
import com.company.blackjack.commands.Command;
import com.company.blackjack.commands.SkipRoundCommand;
import com.company.blackjack.net.GenericSocket;

import java.util.Optional;

public class FxBetCommand implements Command {

    private final Integer timeout;
    private final Integer minBet;
    private final Integer maxBet;

    public FxBetCommand(int timeout, int minBet, int maxBet) {
        this.timeout = timeout;
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof FxClientHandler) {
            BetDialog dialog = new BetDialog(timeout, minBet, maxBet);

            Optional<Integer> result = dialog.showAndWait();
            if(result.isPresent()) {
                handler.sendCommand(new BetCommand(result.get()));
            } else {
                handler.sendCommand(new SkipRoundCommand());
            }
        }
    }
}
