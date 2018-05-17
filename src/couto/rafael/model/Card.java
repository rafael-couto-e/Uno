package couto.rafael.model;

import couto.rafael.util.ColorUtils;

public class Card {
    protected Color color;
    protected int number;

    public Card() {
    }

    public Card(Color color, int number) {
        this.color = color;
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Carta nº "+number+" "+ ColorUtils.getColorString(color);
    }
}
