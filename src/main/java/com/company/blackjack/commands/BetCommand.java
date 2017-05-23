package com.company.blackjack.commands;

import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.players.Player;
import com.company.blackjack.server.ClientHandler;

public class BetCommand extends NotifyCommand {

    public final Integer bet;

    public BetCommand(int bet) {
        this.bet = bet;
    }

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof ClientHandler) {
            Player player = ((ClientHandler) handler).getPlayer();
            if(player != null) {
                player.setBet(bet);
                player.setState(Player.State.BETTING);
                super.notifyDealer(player);
            }
        }
    }
}
