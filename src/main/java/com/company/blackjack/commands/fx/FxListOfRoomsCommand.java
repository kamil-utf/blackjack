package com.company.blackjack.commands.fx;

import com.company.blackjack.client.model.Desk;
import com.company.blackjack.client.view.component.TableViewDialog;
import com.company.blackjack.commands.Command;
import com.company.blackjack.commands.JoinRoomCommand;
import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.players.Dealer;
import com.company.blackjack.server.Room;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

public class FxListOfRoomsCommand implements Command {

    private final String jsonData;

    public FxListOfRoomsCommand(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public void execute(GenericSocket handler) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray rooms = jsonObject.getJSONArray(Room.COLLECTION_TAG);

        TableViewDialog<Desk> dialog = new TableViewDialog<>();
        dialog.setTitle("Selection");
        dialog.setHeaderText("Blackjack Table Selection");

        // Create tableview key
        dialog.createColumn("Dealer");
        dialog.createColumn("Players", "size");
        dialog.createColumn("Min. Bet", "minBet");

        // Create tableview data
        for(int i = 0; i < rooms.length(); i++) {
            JSONObject room = rooms.getJSONObject(i);
            dialog.addItem(parseJSONDesk(room));
        }

        Optional<Desk> result = dialog.showAndWait();
        if(result.isPresent()) {
            sendJoinRoomCommand(handler, result.get().getId());
        } else {
            handler.close();
        }
    }

    private Desk parseJSONDesk(JSONObject jsonObject) {
        Desk desk = new Desk();

        desk.setId(jsonObject.getString(Room.ID_TAG));
        desk.setDealer(jsonObject.getString(Dealer.ELEMENT_TAG));
        desk.setSize(jsonObject.getString(Room.SIZE_TAG));
        desk.setMinBet(jsonObject.getDouble(Room.BET_TAG));

        return desk;
    }

    private void sendJoinRoomCommand(GenericSocket handler, String deskId) {
        if(handler != null && deskId != null) {
            handler.sendCommand(new JoinRoomCommand(deskId));
        }
    }
}
