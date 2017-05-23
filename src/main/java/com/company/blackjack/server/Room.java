package com.company.blackjack.server;

import com.company.blackjack.exception.FullRoomException;
import com.company.blackjack.players.Dealer;
import com.company.blackjack.players.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Room {

    public static final String COLLECTION_TAG = "rooms";
    public static final String ELEMENT_TAG = "room";
    public static final String ID_TAG = "id";
    public static final String SIZE_TAG = "size";
    public static final String BET_TAG = "min_bet";

    private final Integer size;
    private final Dealer dealer;
    private final Integer minBet;
    private final List<Player> players;

    public Room(int size, String dealerName, int minBet) {
        this.size = size;
        this.dealer = new Dealer(this, dealerName);
        this.minBet = minBet;
        this.players = Collections.synchronizedList(new LinkedList<>());

        new Thread(dealer).start();
    }

    public void addPlayer(Player player) throws FullRoomException {
        if(isFull()) {
            throw new FullRoomException("Room is full.", this);
        }

        players.add(player);
        notifyDealer();
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getListOfPlayers() {
        synchronized(players) {
            return new LinkedList<>(players);
        }
    }

    public void stopGame() {
        dealer.stop();
    }

    public void notifyDealer() {
        dealer.notifyDealer();
    }

    public boolean isEmpty() {
        return players.size() == 0;
    }

    public boolean isFull() {
        return size != null && players.size() >= size;
    }

    public Integer getSize() {
        return size;
    }

    public String getDealerName() {
        return dealer.getName();
    }

    public Integer getMinBet() {
        return minBet;
    }

    public String slots() {
        return players.size() + " / " + size;
    }
}
