package com.company.blackjack.commands;

import com.company.blackjack.commands.fx.FxAlertCommand;
import com.company.blackjack.commands.fx.FxWelcomeCommand;
import com.company.blackjack.exception.FullRoomException;
import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.players.Player;
import com.company.blackjack.server.ClientHandler;
import com.company.blackjack.server.Room;
import com.company.blackjack.server.RoomDispatcher;
import javafx.scene.control.Alert;

public class JoinRoomCommand implements Command {

    private String roomId;

    public JoinRoomCommand(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof ClientHandler) {
            ClientHandler clientHandler = (ClientHandler) handler;
            try {
                RoomDispatcher dispatcher = RoomDispatcher.getInstance();
                Room room = dispatcher.assignRoom(roomId, clientHandler.getPlayer());

                Player player = null;
                if(room != null && (player = clientHandler.getPlayer()) != null) {
                    player.setRoom(room);
                    handler.sendCommand(new FxWelcomeCommand(player.getId()));
                }
            } catch(FullRoomException e) {
                handler.sendCommand(
                        new FxAlertCommand(Alert.AlertType.WARNING, e.getMessage(), client -> {
                            if(client != null) {
                                client.sendCommand(new ListOfRoomsCommand());
                            }
                        })
                );
            }
        }
    }
}
