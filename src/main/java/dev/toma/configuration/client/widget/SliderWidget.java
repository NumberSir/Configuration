package dev.toma.configuration.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.client.theme.ConfigTheme;
import dev.toma.configuration.client.widget.render.IRenderer;
import dev.toma.configuration.client.widget.render.SpriteRenderer;
import dev.toma.configuration.config.value.NumericValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.text.DecimalFormat;

public class SliderWidget<N extends Number & Comparable<N>> extends AbstractThemeWidget {

    public static final SpriteRenderer.SpriteOptions SLIDER_OPT = new SpriteRenderer.SpriteOptions(0, 60);
    public static final SpriteRenderer.SpriteOptions SLIDER_HOVERED_OPT = new SpriteRenderer.SpriteOptions(0, 80);
    public static final SpriteRenderer.SpriteOptions HANDLE_OPT = new SpriteRenderer.SpriteOptions(200, 60);
    public static final SpriteRenderer.SpriteOptions HANDLE_HOVERED_OPT = new SpriteRenderer.SpriteOptions(200, 80);

    protected final Font font;
    protected final NumericValue<N> numericValue;
    protected IRenderer handleRenderer;
    protected double value;
    protected N num;

    private DecimalFormat decimalFormat;

    public SliderWidget(int x, int y, int width, int height, ConfigTheme theme, NumericValue<N> numericValue, Font font) {
        super(x, y, width, height, theme);
        this.numericValue = numericValue;
        this.value = numericValue.getSliderValue();
        this.font = font;

        this.updateDisplayText();
    }

    public static SpriteRenderer.SpriteOptions getSpriteOptions(AbstractThemeWidget themeWidget) {
        return themeWidget.isHoveredOrFocused() ? SLIDER_HOVERED_OPT : SLIDER_OPT;
    }

    public static SpriteRenderer.SpriteOptions getHandleSpriteOptions(AbstractThemeWidget themeWidget) {
        return themeWidget.isFocused() ? HANDLE_HOVERED_OPT : HANDLE_OPT;
    }

    public void setFormatter(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        this.updateDisplayText();
    }

    public void setHandleRenderer(IRenderer handleRenderer) {
        this.handleRenderer = handleRenderer;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaTick) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.renderBackground(guiGraphics);
        this.applyRenderer(this.handleRenderer, guiGraphics, this.getX() + (int)(this.value * (this.width - 8.0D)), this.getY(), 8, this.getHeight());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int textColor = this.theme.getWidgetTextColor(this.active, this.isHovered);
        this.renderScrollingString(guiGraphics, font, 2, textColor);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    private void setValueFromMouse(double x) {
        this.setValue((x - (this.getX() + 4.0D)) / (this.width - 8.0D));
    }

    public void setValue(double value) {
        double oldValue = this.value;
        this.value = Mth.clamp(value, 0.0, 1.0);
        if (oldValue != this.value) {
            N updatedValue = this.numericValue.getValueFromSlider(this.value);
            this.numericValue.setValue(updatedValue);
            this.setChanged();
        }
        this.updateDisplayText();
    }

    protected void updateDisplayText() {
        this.num = this.numericValue.get();
        this.setMessage(Component.literal(this.decimalFormat != null ? this.decimalFormat.format(this.num) : String.valueOf(this.num)));
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setValueFromMouse(mouseX);
        super.onDrag(mouseX, mouseY, dragX, dragY);
    }

    @Override
    public void playDownSound(SoundManager $$0) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }
}
