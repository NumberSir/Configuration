package dev.toma.configuration.client.screen;

import dev.toma.configuration.ConfigurationSettings;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ConfigSettingsScreen extends Screen {

    private static final Component TITLE = Component.translatable("options.title");
    private final Screen parent;

    public ConfigSettingsScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    public void onClose() {
        super.onClose();
        this.close();
    }

    @Override
    protected void init() {
        ConfigurationSettings settings = ConfigurationSettings.getInstance();
        // Advanced mode
        Checkbox checkbox = getCheckbox(settings);
        this.addRenderableWidget(checkbox);

        // Back button
        this.addRenderableWidget(
                Button.builder(Component.translatable("gui.back"), button -> close())
                        .pos(5, this.height - 25)
                        .size(120, 20)
                        .build()
        );
    }

    private Checkbox getCheckbox(ConfigurationSettings settings) {
        Checkbox checkbox = new Checkbox(10, 40, this.width - 20, 20, Component.translatable("text.configuration.options.advanced_mode"), settings.isAdvancedMode()) {
            @Override
            public void onPress() {
                super.onPress();
                settings.setAdvancedMode(selected());
            }
        };
        checkbox.setTooltip(Tooltip.create(Component.translatable("text.configuration.options.advanced_mode.tooltip")));
        checkbox.setTooltipDelay(500);
        return checkbox;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float renderDelta) {
        this.renderBackground(graphics, mouseX, mouseY, renderDelta);
        super.render(graphics, mouseX, mouseY, renderDelta);
    }

    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float renderDelta) {
        super.renderBackground(graphics);
        // Header BG
        graphics.fill(0, 0, this.width, 30, 0x99 << 24);
        // Footer BG
        graphics.fill(0, this.height - 30, this.width, this.height, 0x99 << 24);
        // Header text
        graphics.drawString(this.font, TITLE, (this.width - this.font.width(TITLE)) / 2, (30 - this.font.lineHeight) / 2, 0xFFFFFF);
    }

    private void close() {
        ConfigurationSettings.saveSettings();
        this.minecraft.setScreen(this.parent);
    }
}
