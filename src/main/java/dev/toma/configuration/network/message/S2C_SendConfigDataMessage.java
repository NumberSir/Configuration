package dev.toma.configuration.network.message;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.adapter.TypeAdapter;
import dev.toma.configuration.config.value.ConfigValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public record S2C_SendConfigDataMessage(String config, Map<String, NetworkConfigValue<?>> values) implements NetworkMessage {

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(Configuration.MODID, "send_config_data");

    public S2C_SendConfigDataMessage(String config) {
        this(config, null);
    }

    private static Map<String, ConfigValue<?>> loadValuesForSynchronization(String config) {
        ConfigHolder<?> holder = ConfigHolder.getConfig(config)
                .orElseThrow(() -> new IllegalArgumentException("Unknown config: " + config));
        return holder.getNetworkSerializedFields();
    }

    @Override
    public ResourceLocation getPacketId() {
        return IDENTIFIER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        Map<String, ConfigValue<?>> synchronizedFields = loadValuesForSynchronization(this.config);

        buf.writeUtf(this.config);
        buf.writeInt(synchronizedFields.size());
        Configuration.LOGGER.debug("Sending {} config fields to client from config '{}'", synchronizedFields.size(), this.config);
        for (Map.Entry<String, ConfigValue<?>> entry : synchronizedFields.entrySet()) {
            String field = entry.getKey();
            ConfigValue<?> value = entry.getValue();
            buf.writeUtf(field);
            this.encodeToBuffer(value, buf);
        }
    }

    @Override
    public void handle(Player player) {
        Configuration.LOGGER.debug("Received {} config fields from server for config '{}'", this.values.size(), this.config);
        for (Map.Entry<String, NetworkConfigValue<?>> entry : this.values.entrySet()) {
            NetworkConfigValue<?> value = entry.getValue();
            value.bind();
        }
    }

    private <T> void encodeToBuffer(ConfigValue<T> value, FriendlyByteBuf buffer) {
        TypeAdapter<T> adapter = value.getAdapter();
        adapter.encodeToBuffer(value, buffer);
    }

    public static S2C_SendConfigDataMessage read(FriendlyByteBuf buffer) {
        String config = buffer.readUtf();
        int valuesCount = buffer.readInt();
        Map<String, ConfigValue<?>> synchronizedFields = loadValuesForSynchronization(config);
        if (valuesCount != synchronizedFields.size()) {
            throw new IllegalArgumentException("Number of synchronization fields did not match for config " + config);
        }
        Map<String, NetworkConfigValue<?>> values = new LinkedHashMap<>();
        for (int i = 0; i < valuesCount; i++) {
            String field = buffer.readUtf();
            ConfigValue<?> configValue = synchronizedFields.get(field);
            if (configValue == null) {
                Configuration.LOGGER.fatal("Received unknown config value {}", field);
                throw new RuntimeException("Unknown config field: " + field);
            }
            saveValue(values, configValue, field, buffer);

        }
        return new S2C_SendConfigDataMessage(config, values);
    }

    private static <T> void saveValue(Map<String, NetworkConfigValue<?>> map, ConfigValue<T> value, String field, FriendlyByteBuf buffer) {
        TypeAdapter<T> adapter = value.getAdapter();
        T t = adapter.decodeFromBuffer(value, buffer);
        map.put(field, new NetworkConfigValue<>(value, t));
    }

    private record NetworkConfigValue<T>(ConfigValue<T> configValue, T value) {

        void bind() {
            this.configValue.setFromNetwork(this.value);
        }
    }
}
