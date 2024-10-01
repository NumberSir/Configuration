package dev.toma.configuration.config.value;

import dev.toma.configuration.config.adapter.TypeAdapter;
import dev.toma.configuration.config.exception.ConfigValueMissingException;
import dev.toma.configuration.config.format.IConfigFormat;
import net.minecraft.network.FriendlyByteBuf;

public class EnumValue<E extends Enum<E>> extends ConfigValue<E> {

    private final String[] additionalComments;

    public EnumValue(ValueData<E> valueData) {
        super(valueData);
        this.additionalComments = generateEnumComments(valueData.getValueType());
    }

    @Override
    protected void serialize(IConfigFormat format) {
        format.addComments(this.additionalComments);
        format.writeEnum(this.getId(), this.get(Mode.SAVED));
    }

    @Override
    protected void deserialize(IConfigFormat format) throws ConfigValueMissingException {
        this.setValue(format.readEnum(this.getId(), getValueType()));
    }

    static <E extends Enum<E>> String[] generateEnumComments(Class<E> enumType) {
        String[] comments = new String[enumType.getEnumConstants().length + 1];
        comments[0] = "Allowed values:";
        for (int i = 0; i < enumType.getEnumConstants().length; i++) {
            comments[i + 1] = "- " + enumType.getEnumConstants()[i].name();
        }
        return comments;
    }

    public static final class Adapter<E extends Enum<E>> extends TypeAdapter<E> {

        @Override
        public ConfigValue<E> serialize(TypeAttributes<E> attributes, Object instance, TypeSerializer serializer) throws IllegalAccessException {
            return new EnumValue<>(ValueData.of(attributes));
        }

        @Override
        public void encodeToBuffer(ConfigValue<E> value, FriendlyByteBuf buffer) {
            buffer.writeEnum(value.get());
        }

        @Override
        public E decodeFromBuffer(ConfigValue<E> value, FriendlyByteBuf buffer) {
            Class<E> type = value.getValueType();
            return buffer.readEnum(type);
        }
    }
}
