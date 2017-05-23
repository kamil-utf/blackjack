package com.company.blackjack.server;

import com.company.blackjack.exception.FullRoomException;
import com.company.blackjack.players.Dealer;
import com.company.blackjack.players.Player;
import com.company.blackjack.utils.XmlUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RoomDispatcher {

    private volatile static RoomDispatcher instance = null;
    private final Map<String, Room> rooms;

    private RoomDispatcher() {
        rooms = new ConcurrentHashMap<>();
    }

    public static RoomDispatcher getInstance() {
        if(instance == null) {
            synchronized(RoomDispatcher.class) {
                if(instance == null) {
                    instance = new RoomDispatcher();
                }
            }
        }

        return instance;
    }

    public void parseXML(Document doc) {
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName(Room.ELEMENT_TAG);
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                int size = XmlUtils.getIntTagValue(Room.SIZE_TAG, element);
                String dealer = XmlUtils.getTagValue(Dealer.ELEMENT_TAG, element);
                int minBet = XmlUtils.getIntTagValue(Room.BET_TAG, element);

                putRoom(new Room(size, dealer, minBet));
            }
        }
    }

    private void putRoom(Room room) {
        rooms.putIfAbsent(UUID.randomUUID().toString(), room);
    }

    public Room assignRoom(String roomId, Player player) throws FullRoomException {
        if(player == null) {
            throw new IllegalStateException("Player is the NULL object.");
        }

        Room room = rooms.computeIfAbsent(roomId, f -> {return null;});   //TODO
        room.addPlayer(player);
        return room;
    }

    public String getRoomsJSON() {
        JSONArray array = new JSONArray();

        for(Map.Entry<String, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Room.ID_TAG, entry.getKey());
            jsonObject.put(Dealer.ELEMENT_TAG, room.getDealerName());
            jsonObject.put(Room.SIZE_TAG, room.slots());
            jsonObject.put(Room.BET_TAG, room.getMinBet());
            array.put(jsonObject);
        }

        JSONObject header = new JSONObject();
        header.put(Room.COLLECTION_TAG, array);
        return header.toString();
    }
}
