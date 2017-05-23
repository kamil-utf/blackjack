package com.company.blackjack.exception;

import com.company.blackjack.server.Room;

public class FullRoomException extends Exception {

    private Room room;

    public FullRoomException(String msg, Room room) {
        super(msg);
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
