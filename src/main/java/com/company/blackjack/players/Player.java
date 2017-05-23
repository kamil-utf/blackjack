package com.company.blackjack.players;

import com.company.blackjack.server.ClientHandler;
import com.company.blackjack.server.Room;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {

    public static final int DEFAULT_MONEY = 1000;

    public static final String COLLECTION_TAG = "players";
    public static final String ELEMENT_TAG = "player";
    public static final String ID_TAG = "id";
    public static final String BET_TAG = "bet";

    public enum State {
        WAITING_FOR_ROOM,
        WAITING_FOR_ACTION,
        BETTING,
        HIT,
        STAND,
        BLACKJACK,
        SKIP_ROUND,
        DISCONNECTED
    }

    private final ClientHandler handler;

    private final String id;
    private Integer money;
    private AtomicInteger bet;
    private volatile Room room;
    private volatile State state;

    public Player(ClientHandler handler) {
        this.handler = handler;

        this.id = UUID.randomUUID().toString();
        this.bet = new AtomicInteger();
        this.money = DEFAULT_MONEY;
        this.state = State.WAITING_FOR_ROOM;
    }

    public void leaveRoom() {
        if(room != null) {
            room.removePlayer(this);
        }
    }

    public ClientHandler handler() {
        return handler;
    }

    public String getId() {
        return id;
    }

    public int getBet() {
        return bet.get();
    }

    public void setBet(int bet) {
        this.bet.set(bet);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    /* Method only for dealer */

    int getMoney() {
        return money;
    }

    void chargeMoney(int sum) {
        money -= sum;
    }

    void giveMoney(int sum) {
        money += sum;
    }
}
