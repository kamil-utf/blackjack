package com.company.blackjack.players;

import com.company.blackjack.commands.fx.FxBetCommand;
import com.company.blackjack.commands.fx.FxGameOverCommand;
import com.company.blackjack.commands.fx.FxGameStateCommand;
import com.company.blackjack.commands.fx.FxHitOrStandCommand;
import com.company.blackjack.core.Card;
import com.company.blackjack.core.Deck;
import com.company.blackjack.core.Hand;
import com.company.blackjack.server.Room;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Dealer implements Runnable {

    public static final String ELEMENT_TAG = "dealer";

    private static final int BOUND_OF_HIT = 17;

    private static final int BET_TIMEOUT = 25;
    private static final int HIT_OR_STAND_TIMEOUT = 20;

    private int actionIndex = 0;

    private final Room room;
    private final Hand hand;
    private final Deck deck;
    private final String name;
    private final DealerAction[] actions;
    private final Map<Player, Hand> gamblers;

    private volatile boolean stop = false;

    public Dealer(Room room, String name) {
        super();

        this.room = room;
        this.name = name;
        this.hand = new Hand();
        this.deck = new Deck(true);
        this.gamblers = new HashMap<>();

        this.actions = new DealerAction[] {
            new StartNewRoundAction(),
            new WaitForBetsAction(),
            new DealCardsAction(),
            new FollowOrdersAction(),
            new PayoffAction()
        };
    }

    @Override
    public void run() {

        // Game loop
        while(!stop) {
            waitForPlayer();

            DealerAction nextAction = actions[actionIndex];
            if(nextAction != null) {
                nextAction.perform();
                actionIndex = (actionIndex + 1) % actions.length;
            }
        }
    }

    private synchronized void waitForPlayer() {
        while(room.isEmpty()) {
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            // Reset action index - new round
            actionIndex = 0;
        }
    }

    private void waitForActions(Player.State... states) {
        waitForActions(new HashSet<>(Arrays.asList(states)));
    }

    private synchronized void waitForActions(Set<Player.State> expectedStates, Player... gamblers) {
        expectedStates.add(Player.State.DISCONNECTED);

        Set<Player> recipients;
        if(gamblers.length == 0) {
            recipients = this.gamblers.keySet();
        } else {
            recipients = new HashSet<>(Arrays.asList(gamblers));
        }

        while(isPerformed(expectedStates, recipients)) {
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPerformed(Set<Player.State> expectedStates, Set<Player> gamblers) {
        for(Player gambler : gamblers) {
            if(!expectedStates.contains(gambler.getState())) {
                return true;
            }
        }

        return false;
    }

    public synchronized void notifyDealer() {
        notifyAll();
    }

    public void stop() {
        stop = true;
    }

    public String getName() {
        return name;
    }

    private void sleep() {
        try {
            Thread.sleep(2500);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void forEachRoomPlayers(Consumer<Player> consumer) {
        List<Player> players;
        if(room != null && (players = room.getListOfPlayers()) != null) {
            for(Player player : players) {
                consumer.accept(player);
            }
        }
    }

    private void notifyAllOfGameState() {
        String gameState = getGameStateJSON();
        forEachRoomPlayers(player -> {
            if(player.getState() != Player.State.DISCONNECTED)
                player.handler().sendCommand(new FxGameStateCommand(gameState, player.getMoney()));
        });
    }

    private String getGameStateJSON() {
        // Dealer
        JSONObject dealerJSON = new JSONObject();
        dealerJSON.put(Card.COLLECTION_TAG, hand.getCardsJSON());
        dealerJSON.put(Hand.TOTAL_TAG, hand.total());

        // Players
        JSONArray playersJSON = new JSONArray();

        forEachRoomPlayers(player -> {
            JSONObject playerJSON = new JSONObject();

            String id = player.getId();
            playerJSON.put(Player.ID_TAG, id);

            Hand hand = showHand(id);
            if(hand != null) {
                playerJSON.put(Hand.TOTAL_TAG, hand.total());
                playerJSON.put(Player.BET_TAG, player.getBet());
                playerJSON.put(Card.COLLECTION_TAG, hand.getCardsJSON());
            }

            playersJSON.put(playerJSON);
        });

        JSONObject main = new JSONObject();
        main.put(Dealer.ELEMENT_TAG, dealerJSON);
        main.put(Player.COLLECTION_TAG, playersJSON);
        return main.toString();
    }

    private Hand showHand(String id) {
        for(Map.Entry<Player, Hand> entry : gamblers.entrySet()) {
            Player gambler = entry.getKey();
            Hand hand = entry.getValue();

            if(gambler.getId().equals(id)) {
                return hand;
            }
        }

        return null;
    }

    /* Dealer actions */

    private class StartNewRoundAction implements DealerAction {

        @Override
        public void perform() {
            sleep();

            Map<Player, Hand> tmpMap = new HashMap<>(gamblers);
            gamblers.clear();

            hand.clear();       // Clear dealer hand
            forEachRoomPlayers(player -> {
                Hand hand = tmpMap.computeIfAbsent(player, k -> new Hand());
                gamblers.putIfAbsent(player, hand);

                hand.clear();
                player.setBet(0);
                player.setState(Player.State.WAITING_FOR_ACTION);
                player.handler().sendCommand(
                        new FxBetCommand(BET_TIMEOUT, room.getMinBet(), player.getMoney())
                );
            });

            notifyAllOfGameState();

            sleep();
        }
    }

    private class WaitForBetsAction implements DealerAction {

        @Override
        public void perform() {
            waitForActions(Player.State.BETTING, Player.State.SKIP_ROUND);

            Iterator<Map.Entry<Player, Hand>> it = gamblers.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<Player, Hand> entry = it.next();
                Player gambler = entry.getKey();

                if(gambler.getState() == Player.State.BETTING) {
                    gambler.chargeMoney(gambler.getBet());
                } else {
                    // Remove gamblers with SKIP ROUND state
                    it.remove();
                }
            }
        }
    }

    private class DealCardsAction implements DealerAction {

        @Override
        public void perform() {
            if(gamblers.size() == 0) {
                try {
                    Thread.sleep(2500);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                actionIndex = -1;
                return;
            }

            // Dealer hand
            hand.addCard(deck.dealUp());
            hand.addCard(deck.dealDown());

            // Other hands
            gamblers.values().forEach(hand -> {
                hand.addCard(deck.dealUp());
                hand.addCard(deck.dealUp());
            });

            notifyAllOfGameState();
        }
    }

    private class FollowOrdersAction implements DealerAction {

        private final Set<Player.State> allowedStates = new HashSet<>(
                Arrays.asList(Player.State.HIT, Player.State.STAND)
        );

        @Override
        public void perform() {
            sleep();    // It's time to thinking...

            gamblers.forEach((gambler, hand) -> {
                while(hand.total() < 21) {
                    gambler.handler().sendCommand(new FxHitOrStandCommand(HIT_OR_STAND_TIMEOUT));
                    waitForActions(allowedStates, gambler);

                    Player.State currentState = gambler.getState();
                    if(currentState == Player.State.HIT) {
                        hand.addCard(deck.dealUp());
                        gambler.setState(Player.State.WAITING_FOR_ACTION);
                        notifyAllOfGameState();
                    }
                    else if(currentState == Player.State.STAND || currentState == Player.State.DISCONNECTED) {
                        break;
                    }
                }
            });

            // Dealer movements
            hand.turnOver();
            if(hitCondition()) {
                while (hand.total() < Dealer.BOUND_OF_HIT) {
                    hand.addCard(deck.dealUp());
                }
            }

            notifyAllOfGameState();
        }

        private boolean hitCondition() {
            for(Hand hand : gamblers.values()) {
                if(!hand.isBust() && !hand.isBlackjack()) {
                    return true;
                }
            }

            return false;
        }
    }

    private class PayoffAction implements DealerAction {

        @Override
        public void perform() {
            payoff(h -> hand.isBlackjack() && h.isBlackjack(), 1);
            payoff(h -> !hand.isBlackjack() && h.isBlackjack(), 3);
            payoff(h -> hand.isBust() && !h.isBust(), 2);
            payoff(h -> !hand.isBust() && !h.isBust() && h.isGreaterThan(hand), 2);

            // Game Over
            gamblers.keySet().forEach(gambler -> {
                if(gambler.getMoney() == 0) {
                    gambler.handler().sendCommand(new FxGameOverCommand());
                }
            });
        }

        private void payoff(Predicate<Hand> predicate, int multiplier) {
            Iterator<Map.Entry<Player, Hand>> it = gamblers.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<Player, Hand> entry = it.next();
                Player gambler = entry.getKey();
                Hand hand = entry.getValue();

                if(predicate.test(hand)) {
                    gambler.giveMoney(gambler.getBet() * multiplier);
                    it.remove();
                }
            }
        }
    }
}
