package com.company.blackjack.client.view;

import com.company.blackjack.client.FxClientHandler;
import com.company.blackjack.commands.ByeCommand;
import com.company.blackjack.commands.Command;
import com.company.blackjack.commands.ListOfRoomsCommand;
import com.company.blackjack.net.SocketListener;
import com.company.blackjack.utils.CardImages;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.Socket;

public class OverviewController {

    @FXML private BorderPane borderPane;

    @FXML private MenuItem connectItem;
    @FXML private MenuItem disconnectItem;

    private FxClientHandler handler;

    @FXML
    private void initialize() {
        CardImages.getInstance().load();

        initHeader();
    }

    private void initHeader() {
        ImageView imageView = new ImageView(
                new Image(getClass().getResource("/images/header.png").toExternalForm())
        );

        Pane pane = (Pane) borderPane.getCenter();
        pane.getChildren().add(imageView);
    }

    private void connect(String host, int port) {
        try {
            handler = new FxClientHandler(
                    new Socket(host, port), new FxSocketListener(), borderPane
            );
            handler.connect();

            try {
                // Waiting for init connection
                Thread.sleep(500);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            handler.sendCommand(new ListOfRoomsCommand());
            connectItem.setDisable(true);
            disconnectItem.setDisable(false);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if(handler != null) {
            handler.sendCommand(new ByeCommand());
            handler.getGameBox().destroy();

            connectItem.setDisable(false);
            disconnectItem.setDisable(true);
        }
    }

    @FXML
    private void handleConnect() {
        ConnectionDialog dialog = new ConnectionDialog();
        dialog.showAndWait().ifPresent(pair -> {
            String host = pair.getKey();
            Integer port = pair.getValue();
            if(host != null && port != null) {
                connect(pair.getKey(), pair.getValue());
            }
        });
    }

    @FXML
    private void handleDisconnect() {
        shutdown();
    }

    private class FxSocketListener implements SocketListener {

        @Override
        public void onCommand(Object command) {
            if(command instanceof Command) {
                ((Command) command).execute(handler);
            }
        }
    }
}
