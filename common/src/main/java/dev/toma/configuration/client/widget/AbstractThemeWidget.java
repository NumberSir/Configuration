package dev.toma.configuration.client.widget;

import dev.toma.configuration.client.theme.ConfigTheme;
import dev.toma.configuration.client.widget.render.IRenderer;
import dev.toma.configuration.client.widget.render.SpriteRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class AbstractThemeWidget extends AbstractWidget {

    public static final Component REVERT = Component.translatable("text.configuration.value.revert");
    public static final Component REVERT_DEFAULT = Component.translatable("text.configuration.value.revert_default");
    public static final SpriteRenderer.SpriteOptions DISABLED = new SpriteRenderer.SpriteOptions(0, 20);
    public static final SpriteRenderer.SpriteOptions HOVERED = new SpriteRenderer.SpriteOptions(0, 40);

    protected final ConfigTheme theme;

    protected IRenderer backgroundRenderer;
    protected ChangeListener<AbstractThemeWidget> changeListener;

    public AbstractThemeWidget(int x, int y, int width, int height, ConfigTheme theme) {
        this(x, y, width, height, CommonComponents.EMPTY, theme);
    }

    public AbstractThemeWidget(int x, int y, int width, int height, Component text, ConfigTheme theme) {
        super(x, y, width, height, text);
        this.theme = theme;
    }

    public static SpriteRenderer.SpriteOptions getSpriteOptions(AbstractThemeWidget widget) {
        return widget.active ? widget.isHoveredOrFocused() ? HOVERED : SpriteRenderer.SpriteOptions.DEFAULT : DISABLED;
    }

    public void setBackgroundRenderer(IRenderer backgroundRenderer) {
        this.backgroundRenderer = backgroundRenderer;
    }

    public void renderBackground(GuiGraphics graphics) {
        this.applyRenderer(this.backgroundRenderer, graphics, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void applyRenderer(IRenderer renderer, GuiGraphics graphics, int x, int y, int width, int height) {
        if (renderer != null) {
            renderer.draw(graphics, x, y, width, height, this.isHovered);
        }
    }

    public void setChangeListener(ChangeListener<AbstractThemeWidget> changeListener) {
        this.changeListener = changeListener;
    }

    public void setChanged() {
        if (this.changeListener != null) {
            this.changeListener.onChanged(this);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getMessage());
    }

    public ConfigTheme getTheme() {
        return theme;
    }

    public int getRight() {
        return this.getX() + this.getWidth();
    }

    public int getBottom() {
        return this.getY() + this.getHeight();
    }

    @FunctionalInterface
    public interface ChangeListener<T> {
        void onChanged(T t);
    }
}
