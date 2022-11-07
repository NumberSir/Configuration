package dev.toma.configuration.client.widget;

import dev.toma.configuration.config.value.BooleanValue;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class BooleanWidget extends Widget {

    public static final ITextComponent TRUE = new TranslationTextComponent("text.configuration.value.true").withStyle(TextFormatting.GREEN);
    public static final ITextComponent FALSE = new TranslationTextComponent("text.configuration.value.false").withStyle(TextFormatting.RED);
    private final BooleanValue value;

    public BooleanWidget(int x, int y, int w, int h, BooleanValue value) {
        super(x, y, w, h, StringTextComponent.EMPTY);
        this.value = value;
        this.readState();
    }

    @Override
    public void onClick(double x, double y) {
        this.setState(!this.value.get());
    }

    private void readState() {
        boolean value = this.value.get();
        this.setMessage(value ? TRUE : FALSE);
    }

    private void setState(boolean state) {
        this.value.set(state);
        this.readState();
    }
}
