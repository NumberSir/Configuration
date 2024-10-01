package dev.toma.configuration.config;

import dev.toma.configuration.ConfigurationSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Predicate;

/**
 * Field visibility on GUI - advanced fields are only visible to users who enable the advanced mode,
 * hidden fields are not displayed on GUI
 *
 * @since 3.1
 */
public enum FieldVisibility {

    /**
     * Field is normally displayed in GUI
     */
    NORMAL(opt -> true, null),

    /**
     * Field is displayed only when advanced mode is active in configuration options
     */
    ADVANCED(ConfigurationSettings::isAdvancedMode, "advanced"),

    /**
     * Field is never displayed in GUI
     */
    HIDDEN(opt -> false, null);

    private final Predicate<ConfigurationSettings> visibilityCheck;
    private final MutableComponent label;

    FieldVisibility(Predicate<ConfigurationSettings> visibilityCheck, String identifier) {
        this.visibilityCheck = visibilityCheck;
        this.label = identifier != null ? Component.translatable("text.configuration.description.visibility." + identifier) : null;
    }

    public boolean isVisible(ConfigurationSettings settings) {
        return visibilityCheck.test(settings);
    }

    public MutableComponent getLabel() {
        return label;
    }
}
