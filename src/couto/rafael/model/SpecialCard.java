package couto.rafael.model;

import couto.rafael.util.ColorUtils;
import couto.rafael.util.EffectUtils;

public class SpecialCard extends Card {
    private Effect effect;

    public SpecialCard() {
        super();
    }

    public SpecialCard(Color color, int number, Effect effect) {
        super(color, number);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "Carta especial "+ ColorUtils.getColorString(color)+" - efeito: "+ EffectUtils.getEffectString(effect);
    }
}
