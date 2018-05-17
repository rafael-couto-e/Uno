package couto.rafael.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Card> tableCards;

    public Table() {
        tableCards = new ArrayList<>();
    }

    public List<Card> getTableCards() {
        return tableCards;
    }

    public void setTableCards(List<Card> tableCards) {
        this.tableCards = tableCards;
    }

    public Card getTableTop(){
        if(tableCards.size() == 0)
            throw new IndexOutOfBoundsException("No cards on the table");

        return tableCards.get(0);
    }
}
