package com.company.blackjack.commands.fx;

import com.company.blackjack.client.FxClientHandler;
import com.company.blackjack.commands.Command;
import com.company.blackjack.net.GenericSocket;

public class FxWelcomeCommand implements Command {

    private String id;

    public FxWelcomeCommand(String id) {
        this.id = id;
    }

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof FxClientHandler) {
            ((FxClientHandler) handler).setGameBox(id);
        }
    }
}
