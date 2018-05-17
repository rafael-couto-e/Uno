package couto.rafael.util;

import couto.rafael.model.Card;
import couto.rafael.model.SpecialCard;
import couto.rafael.model.Table;

import java.util.List;

public final class TableUtils {
    public static Table init(List<Card> deck){
        Table table = new Table();

        for(int i = 0; i < deck.size(); i++) {
            Card c = deck.get(i);

            if(c instanceof SpecialCard) continue;

            table.getTableCards().add(deck.get(i));
            deck.remove(i);

            break;
        }

        return table;
    }

    public static void put(Table t, Card c){
        t.getTableCards().add(0, c);
    }
}
