package com.company.blackjack.client;

import com.company.blackjack.client.view.component.GameBox;
import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.net.SocketListener;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

import java.net.Socket;

public class FxClientHandler extends GenericSocket {

    private final SocketListener listener;
    private final BorderPane borderPane;
    private GameBox gameBox;

    public FxClientHandler(Socket socket, SocketListener listener, BorderPane borderPane) {
        super(socket);
        this.listener = listener;
        this.borderPane = borderPane;
    }

    @Override
    public void onCommand(Object command) {
        Platform.runLater(() -> listener.onCommand(command));
    }

    public GameBox getGameBox() {
        return gameBox;
    }
    public void setGameBox(String playerId) {
        gameBox = new GameBox(playerId, borderPane);
    }
}
