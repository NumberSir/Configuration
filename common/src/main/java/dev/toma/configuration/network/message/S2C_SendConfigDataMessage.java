package dev.toma.configuration.network.message;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.adapter.TypeAdapter;
import dev.toma.configuration.config.value.ConfigValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record S2C_SendConfigDataMessage(String config) implements CustomPacketPayload {

    public static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath(Configuration.MODID, "send_config_data");
    public static final Type<S2C_SendConfigDataMessage> TYPE = new Type<>(IDENTIFIER);
    public static final StreamCodec<FriendlyByteBuf, S2C_SendConfigDataMessage> CODEC = StreamCodec.of(
            (o, s2CSendConfigDataMessage) -> s2CSendConfigDataMessage.encode(o),
            S2C_SendConfigDataMessage::decode
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.config);
        ConfigHolder.getConfig(this.config).ifPresent(data -> {
            Map<String, ConfigValue<?>> serialized = data.getNetworkSerializedFields();
            buffer.writeInt(serialized.size());
            for (Map.Entry<String, ConfigValue<?>> entry : serialized.entrySet()) {
                String id = entry.getKey();
                ConfigValue<?> value = entry.getValue();
                TypeAdapter adapter = value.getAdapter();
                buffer.writeUtf(id);
                adapter.encodeToBuffer(value, buffer);
            }
        });
    }

    private static S2C_SendConfigDataMessage decode(FriendlyByteBuf buffer) {
        String config = buffer.readUtf();
        S2C_SendConfigDataMessage packet = new S2C_SendConfigDataMessage(config);
        int i = buffer.readInt();
        ConfigHolder.getConfig(config).ifPresent(data -> {
            Map<String, ConfigValue<?>> serialized = data.getNetworkSerializedFields();
            for (int j = 0; j < i; j++) {
                String fieldId = buffer.readUtf();
                ConfigValue<?> value = serialized.get(fieldId);
                if (value == null) {
                    Configuration.LOGGER.fatal("Received unknown config value {}", fieldId);
                    throw new RuntimeException("Unknown config field: " + fieldId);
                }
                packet.setValue(value, buffer);
            }
        });
        return packet;
    }

    @SuppressWarnings("unchecked")
    private <V> void setValue(ConfigValue<V> value, FriendlyByteBuf buffer) {
        TypeAdapter adapter = value.getAdapter();
        V v = (V) adapter.decodeFromBuffer(value, buffer);
        value.set(v);
    }
}
