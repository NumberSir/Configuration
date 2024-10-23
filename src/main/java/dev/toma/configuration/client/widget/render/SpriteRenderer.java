package dev.toma.configuration.client.widget.render;

import dev.toma.configuration.client.widget.AbstractThemeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class SpriteRenderer implements IRenderer {

    private final AbstractThemeWidget themeWidget;
    private final ResourceLocation assets;
    private final Function<AbstractThemeWidget, SpriteOptions> spriteOptionsFunction;
    private final NineSliceOptions nineSlice;

    public SpriteRenderer(AbstractThemeWidget themeWidget, ResourceLocation assets) {
        this(themeWidget, assets, t -> SpriteOptions.DEFAULT);
    }

    public SpriteRenderer(AbstractThemeWidget themeWidget, ResourceLocation assets, Function<AbstractThemeWidget, SpriteOptions> spriteOptionsFunction) {
        this(themeWidget, assets, spriteOptionsFunction, null);
    }

    public SpriteRenderer(AbstractThemeWidget themeWidget, ResourceLocation assets, Function<AbstractThemeWidget, SpriteOptions> spriteOptionsFunction, NineSliceOptions options) {
        this.themeWidget = themeWidget;
        this.assets = assets;
        this.spriteOptionsFunction = spriteOptionsFunction;
        this.nineSlice = options;
    }

    @Override
    public void draw(GuiGraphics graphics, int x, int y, int width, int height, boolean hovered) {
        SpriteOptions spriteOptions = this.spriteOptionsFunction.apply(themeWidget);
        graphics.pose().pushPose();
        if (this.nineSlice != null)
            graphics.blitNineSliced(this.assets, x, y, width, height, this.nineSlice.xBorder, this.nineSlice.yBorder, this.nineSlice.totalSliceWidth, this.nineSlice.totalSliceHeight, spriteOptions.u, spriteOptions.v);
        else
            graphics.blit(this.assets, x, y, spriteOptions.u, spriteOptions.v, width, height);
        graphics.pose().popPose();
    }

    public record SpriteOptions(int u, int v) {

        public static final SpriteOptions DEFAULT = new SpriteOptions(0, 0);
    }

    public record NineSliceOptions(int xBorder, int yBorder, int totalSliceWidth, int totalSliceHeight) {
    }
}
