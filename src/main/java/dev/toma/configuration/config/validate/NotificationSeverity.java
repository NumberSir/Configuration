package dev.toma.configuration.config.validate;

import dev.toma.configuration.Configuration;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public enum NotificationSeverity {

    INFO("", TextFormatting.RESET, 0xF0030319, 0x502493E5, 0x502469E5),
    WARNING("warning", TextFormatting.GOLD, 0xF0563900, 0x50FFB200, 0x509E6900),
    ERROR("error", TextFormatting.RED, 0xF0270006, 0x50FF0000, 0x50880000);

    private final ResourceLocation icon;
    private final TextFormatting extraFormatting;
    public final int background;
    public final int fadeMin;
    public final int fadeMax;

    NotificationSeverity(String iconName, TextFormatting formatting, int background, int fadeMin, int fadeMax) {
        this.icon = new ResourceLocation(Configuration.MODID, "textures/icons/" + iconName + ".png");
        this.extraFormatting = formatting;
        this.background = background;
        this.fadeMin = fadeMin;
        this.fadeMax = fadeMax;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public TextFormatting getExtraFormatting() {
        return extraFormatting;
    }

    public boolean isOkStatus() {
        return this == INFO;
    }
}
