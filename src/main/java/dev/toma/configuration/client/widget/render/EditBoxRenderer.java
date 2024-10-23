package dev.toma.configuration.client.widget.render;

import net.minecraft.client.gui.GuiGraphics;

public class EditBoxRenderer implements IRenderer {

    @Override
    public void draw(GuiGraphics graphics, int x, int y, int width, int height, boolean hovered) {
        int frame = hovered ? 0xFFFFFFFF : 0xFFA0A0A0;
        graphics.fill(x, y, x + width, y + height, frame);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF << 24);
    }
}
