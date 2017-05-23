package com.company.blackjack.commands;

import com.company.blackjack.players.Player;
import com.company.blackjack.server.Room;

public abstract class NotifyCommand implements Command {

    public void notifyDealer(Player player) {
        Room room = null;
        if(player != null && (room = player.getRoom()) != null) {
            room.notifyDealer();
        }
    }
}
