package com.company.blackjack.client;

import com.company.blackjack.client.view.OverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BlackjackClient extends Application {

    private OverviewController controller;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Overview.fxml"));
            Pane pane = loader.load();
            this.controller = loader.getController();

            primaryStage.setScene(new Scene(pane));
            primaryStage.setTitle("Blackjack Client");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        controller.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
