package dev.toma.configuration.client.widget;

import dev.toma.configuration.config.value.EnumValue;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TextComponent;

public class EnumWidget<E extends Enum<E>> extends AbstractWidget {

    private final EnumValue<E> value;

    public EnumWidget(int x, int y, int w, int h, EnumValue<E> value) {
        super(x, y, w, h, TextComponent.EMPTY);
        this.value = value;
        this.updateText();
    }

    @Override
    public void onClick(double p_230982_1_, double p_230982_3_) {
        this.nextValue();
        this.updateText();
    }

    private void nextValue() {
        E e = this.value.get();
        E[] values = e.getDeclaringClass().getEnumConstants();
        int i = e.ordinal();
        int j = (i + 1) % values.length;
        E next = values[j];
        this.value.set(next);
    }

    private void updateText() {
        E e = this.value.get();
        this.setMessage(new TextComponent(e.name()));
    }
}
