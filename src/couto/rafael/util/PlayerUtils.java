package couto.rafael.util;

import couto.rafael.model.*;

import java.util.List;

public final class PlayerUtils {
    public static void call(List<Player> players, List<Card> deck){
        for(int i = 0; i < 7; i++){
            for(Player p:players){
                p.getHand().add(deck.get(0));
                deck.remove(0);
            }
        }
    }
}
