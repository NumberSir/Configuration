package dev.toma.configuration.client.theme;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.client.theme.adapter.*;
import dev.toma.configuration.client.widget.AbstractThemeWidget;
import dev.toma.configuration.client.widget.EditBoxWidget;
import dev.toma.configuration.client.widget.SliderWidget;
import dev.toma.configuration.client.widget.render.SolidColorRenderer;
import dev.toma.configuration.client.widget.render.SpriteRenderer;
import dev.toma.configuration.config.adapter.TypeMatcher;
import net.minecraft.resources.ResourceLocation;

public class DefaultConfigTheme {

    public static final ResourceLocation DEFAULT_ASSETS = new ResourceLocation(Configuration.MODID, "textures/widget/widgets.png");

    public static void configure(ConfigTheme theme) {
        theme.setHeader(new ConfigTheme.Header(null, 0x99 << 24, 0xaaaaaa));
        theme.setFooter(new ConfigTheme.Footer(0x99 << 24));
        theme.setScrollbar(new ConfigTheme.Scrollbar(5, 0xFF << 24));
        theme.setConfigEntry(new ConfigTheme.ConfigEntry(0xAAAAAA, style -> style.withItalic(true), 0x44FFFFFF));
        theme.setBackgroundFillColor(0x55 << 24);
        theme.setWidgetTextColor(0xE0E0E0, 0xFFFFFF, 0x707070);

        theme.registerDisplayAdapter(TypeMatcher.matchBoolean(), new BooleanDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchCharacter(), new CharacterDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchByte(), new ByteDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchShort(), new ShortDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchInteger(), new IntegerDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchLong(), new LongDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchFloat(), new FloatDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchDouble(), new DoubleDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchString(), new StringDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchBooleanArray(), new BooleanArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchCharacterArray(), new ChararacterArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchByteArray(), new ByteArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchShortArray(), new ShortArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchIntegerArray(), new IntegerArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchLongArray(), new LongArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchFloatArray(), new FloatArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchDoubleArray(), new DoubleArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchStringArray(), new StringArrayDisplayAdapter());
        theme.registerDisplayAdapter(TypeMatcher.matchEnum(), new EnumDisplayAdapter<>());
        theme.registerDisplayAdapter(TypeMatcher.matchEnumArray(), new EnumArrayDisplayAdapter<>());
        theme.registerDisplayAdapter(TypeMatcher.matchObject(), new ObjectDisplayAdapter());

        theme.setButtonBackground(t -> new SpriteRenderer(t, DEFAULT_ASSETS, AbstractThemeWidget::getSpriteOptions, new SpriteRenderer.NineSliceOptions(1, 1, 200, 20)));
        theme.setEditBoxBackground(t -> new SpriteRenderer(t, DEFAULT_ASSETS, EditBoxWidget::getSpriteOptions, new SpriteRenderer.NineSliceOptions(1, 1, 200, 20)));
        theme.setSliderBackground(t -> new SpriteRenderer(t, DEFAULT_ASSETS, SliderWidget::getSpriteOptions, new SpriteRenderer.NineSliceOptions(1, 1, 200, 20)));
        theme.setSliderHandle(t -> new SpriteRenderer(t, DEFAULT_ASSETS, SliderWidget::getHandleSpriteOptions));
        theme.setColorBackground(t -> new SolidColorRenderer(() -> t.isHoveredOrFocused() ? 0xFFFFFFFF : 0xFFA0A0A0));
    }
}
