package couto.rafael.util;

import couto.rafael.model.Color;

public final class ColorUtils {
    public static Color getColor(int index){
        switch (index){
            case 0:
                return Color.BLUE;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.RED;
            case 3:
                return Color.YELLOW;
        }

        return Color.BLACK;
    }

    public static String getColorString(Color color) {
        switch (color){
            case BLUE:
                return "azul";
            case GREEN:
                return "verde";
            case YELLOW:
                return "amarela";
            case RED:
                return "vermelha";
        }

        return "preta";
    }
}
