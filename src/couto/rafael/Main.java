package couto.rafael;

import couto.rafael.model.Card;
import couto.rafael.model.Color;
import couto.rafael.model.Player;
import couto.rafael.model.Table;
import couto.rafael.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Card> deck = DeckUtils.buildDeck();

        List<Player> players = new ArrayList<>();
        /*players.add(new Player("Rafael"));
        players.add(new Player("José"));
        players.add(new Player("Ricardo"));*/
        players.add(new Player("CPU1"));
        players.add(new Player("CPU2"));
        players.add(new Player("CPU3"));

        Collections.shuffle(players);

        PlayerUtils.call(players, deck);

        Table table = TableUtils.init(deck);

        GameUtils.init(players, table, deck);
    }
}
