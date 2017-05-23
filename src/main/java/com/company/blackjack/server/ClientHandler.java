package com.company.blackjack.server;

import com.company.blackjack.commands.Command;
import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.players.Player;

import java.net.Socket;

public class ClientHandler extends GenericSocket {

    private final Player player;

    public ClientHandler(Socket socket) {
        super(socket);
        player = new Player(this);

        System.out.println("PLAYER " + player.getId() + " >> Connection opened.");
    }

    @Override
    public void onCommand(Object command) {
        if(command instanceof Command) {
            ((Command) command).execute(this);
        }
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void close() {
        player.setState(Player.State.DISCONNECTED);
        player.leaveRoom();

        System.out.println("PLAYER " + player.getId() + " >> Connection closed.");
        super.close();
    }
}
