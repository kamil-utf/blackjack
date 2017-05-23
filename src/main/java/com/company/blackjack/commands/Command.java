package com.company.blackjack.commands;

import com.company.blackjack.net.GenericSocket;

import java.io.Serializable;

public interface Command extends Serializable {
    void execute(GenericSocket handler);
}
