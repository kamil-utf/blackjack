package com.company.blackjack.commands;

import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.server.ClientHandler;

public class ByeCommand implements Command {

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof ClientHandler) {
            handler.sendCommand(new ByeCommand());
        }
    }
}
