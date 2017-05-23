package com.company.blackjack.commands;

import com.company.blackjack.commands.fx.FxListOfRoomsCommand;
import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.server.RoomDispatcher;

public class ListOfRoomsCommand implements Command {

    @Override
    public void execute(GenericSocket handler) {
        RoomDispatcher dispatcher = RoomDispatcher.getInstance();
        handler.sendCommand(new FxListOfRoomsCommand(dispatcher.getRoomsJSON()));
    }
}
