package dev.toma.configuration.client.widget;

import net.minecraft.resources.ResourceLocation;

public record WidgetSprites(ResourceLocation enabled, ResourceLocation disabled, ResourceLocation enabledFocused, ResourceLocation disabledFocused) {

    public WidgetSprites(ResourceLocation enabled, ResourceLocation disabled) {
        this(enabled, enabled, disabled, disabled);
    }

    public WidgetSprites(ResourceLocation enabled, ResourceLocation disabled, ResourceLocation focused) {
        this(enabled, disabled, focused, disabled);
    }

    public ResourceLocation get(boolean $$0, boolean $$1) {
        if ($$0) {
            return $$1 ? this.enabledFocused : this.enabled;
        } else {
            return $$1 ? this.disabledFocused : this.disabled;
        }
    }

    public ResourceLocation enabled() {
        return this.enabled;
    }

    public ResourceLocation disabled() {
        return this.disabled;
    }

    public ResourceLocation enabledFocused() {
        return this.enabledFocused;
    }

    public ResourceLocation disabledFocused() {
        return this.disabledFocused;
    }
}
