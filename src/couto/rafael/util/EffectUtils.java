package couto.rafael.util;

import couto.rafael.model.Card;
import couto.rafael.model.Effect;
import couto.rafael.model.Player;

import java.util.List;

public final class EffectUtils {
    public static Effect getEffect(int index){
        switch (index){
            case 0:
                return Effect.REVERSE;
            case 1:
                return Effect.PLUS2;
            case 2:
                return Effect.SKIP;
            case 3:
                return Effect.COLOR;
        }

        return Effect.PLUS4;
    }

    public static String getEffectString(Effect effect){
        switch (effect){
            case REVERSE:
                return "inverter";
            case PLUS2:
                return "+2";
            case SKIP:
                return "passa a vez";
            case COLOR:
                return "troca a cor";
        }

        return "+4";
    }

    public static void performEffectOn(Player player, List<Card> deck, Effect effect, int cummulation){
        switch (effect){
            case PLUS4:
                for(int i = 0; i < 4*cummulation; i++){
                    player.getHand().add(deck.get(i));
                    deck.remove(i);
                }
                break;
            case PLUS2:
                for(int i = 0; i < 2*cummulation; i++){
                    player.getHand().add(deck.get(i));
                    deck.remove(i);
                }
                break;
        }
    }
}
