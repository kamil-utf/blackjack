package com.company.blackjack.commands.fx;

import com.company.blackjack.client.FxClientHandler;
import com.company.blackjack.client.view.component.GameBox;
import com.company.blackjack.commands.Command;
import com.company.blackjack.net.GenericSocket;
import com.company.blackjack.players.Dealer;
import com.company.blackjack.players.Player;
import org.json.JSONArray;
import org.json.JSONObject;

public class FxGameStateCommand implements Command {

    private final String jsonData;
    private final Integer money;

    public FxGameStateCommand(String jsonData, int money) {
        this.jsonData = jsonData;
        this.money = money;
    }

    @Override
    public void execute(GenericSocket handler) {
        if(handler instanceof FxClientHandler) {
            GameBox gameBox = ((FxClientHandler) handler).getGameBox();
            gameBox.initNewGameState();

            // Update game state
            gameBox.updateMoney(money);

            JSONObject jsonObject = new JSONObject(jsonData);

            JSONObject dealerJSON = jsonObject.getJSONObject(Dealer.ELEMENT_TAG);
            gameBox.updateDBox(dealerJSON);

            JSONArray players = jsonObject.getJSONArray(Player.COLLECTION_TAG);
            for(int i = 0; i < players.length(); i++) {
                JSONObject playerJSON = players.getJSONObject(i);
                gameBox.updatePBox(playerJSON);
            }

            gameBox.clean();
        }
    }
}
