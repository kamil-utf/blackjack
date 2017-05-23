package com.company.blackjack.client.view.component;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class TableViewDialog<T> extends Dialog<T> {

    private final GridPane grid;
    private final TableView<T> tableView;

    public TableViewDialog() {
        final DialogPane dialogPane = getDialogPane();

        // -- tableview
        this.tableView = new TableView<>();
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        this.grid.add(tableView, 0, 0);

        dialogPane.setContent(grid);
        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        validate();
        Platform.runLater(() -> tableView.requestFocus());

        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? tableView.getSelectionModel().getSelectedItem() : null;
        });
    }

    private void validate() {
        Node connectButton = getDialogPane().lookupButton(ButtonType.OK);
        connectButton.setDisable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> connectButton.setDisable(newValue == null)));
    }

    public void createColumn(String name) {
        createColumn(name, name);
    }

    public void createColumn(String name, String field) {
        TableColumn<T, String> column = new TableColumn<>();
        column.setText(name.toUpperCase());
        column.setCellValueFactory(new PropertyValueFactory<>(field));

        this.tableView.getColumns().add(column);
    }

    public void addItem(T item) {
        if(item != null) {
            this.tableView.getItems().add(item);
        }
    }
}
