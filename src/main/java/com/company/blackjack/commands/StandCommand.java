package com.company.blackjack.commands;

import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.players.Player;
import com.company.blackjack.server.ClientHandler;

public class StandCommand extends NotifyCommand {

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof ClientHandler) {
            Player player = ((ClientHandler) handler).getPlayer();
            if(player != null) {
                player.setState(Player.State.STAND);
                super.notifyDealer(player);
            }
        }
    }
}
