package couto.rafael.util;

import couto.rafael.model.Card;
import couto.rafael.model.Player;
import couto.rafael.model.SpecialCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DeckUtils {
    public static List<Card> buildDeck(){
        List<Card> deck = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 10; j++){
                if(j != 0)
                    deck.add(generateCard(i, j));

                deck.add(generateCard(i, j));
            }
        }

        deck.addAll(buildSpecialCards());

        shuffle(deck);

        return deck;
    }

    private static void shuffle(List<Card> cards){
        Collections.shuffle(cards);
    }

    private static List<SpecialCard> buildSpecialCards(){
        List<SpecialCard> specialCards = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 2; k++)
                    specialCards.add(generateSpecialCard(i, j));
            }
        }

        for(int i = 4; i < 8; i++){
            for(int j = 3; j < 5; j++){
                specialCards.add(generateSpecialCard(i, j));
            }
        }

        return specialCards;
    }

    private static Card generateCard(int i, int j){
        return new Card(ColorUtils.getColor(i), j);
    }

    private static SpecialCard generateSpecialCard(int i, int j){
        return new SpecialCard(
                ColorUtils.getColor(i),
                j-5,
                EffectUtils.getEffect(j)
        );
    }

    public static void buy(Player player, List<Card> deck){
        if(deck.size() > 0) {
            Card bought = deck.get(0);
            player.getHand().add(bought);
            deck.remove(0);
            if(!player.getName().contains("CPU"))
                System.out.println("Você comprou: "+bought.toString());
        }
    }
}
