package dev.toma.configuration.config.value;

import dev.toma.configuration.config.ConfigUtils;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.adapter.TypeAdapter;
import dev.toma.configuration.config.exception.ConfigValueMissingException;
import dev.toma.configuration.config.format.IConfigFormat;
import net.minecraft.network.FriendlyByteBuf;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BooleanArrayValue extends AbstractArrayValue<boolean[]> {

    private boolean fixedSize;

    public BooleanArrayValue(ValueData<boolean[]> valueData) {
        super(valueData);
    }

    @Override
    public boolean isFixedSize() {
        return fixedSize;
    }

    @Override
    protected void readFieldData(Field field) {
        this.fixedSize = field.getAnnotation(Configurable.FixedSize.class) != null;
    }

    @Override
    protected boolean[] getCorrectedValue(boolean[] in) {
        if (this.fixedSize) {
            boolean[] defaultArray = this.valueData.getDefaultValue();
            if (in.length != defaultArray.length) {
                ConfigUtils.logArraySizeCorrectedMessage(this.getId(), Arrays.toString(in), Arrays.toString(defaultArray));
                return defaultArray;
            }
        }
        return in;
    }

    @Override
    protected void serialize(IConfigFormat format) {
        format.writeBoolArray(this.getId(), this.get());
    }

    @Override
    protected void deserialize(IConfigFormat format) throws ConfigValueMissingException {
        this.set(format.readBoolArray(this.getId()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean[] booleans = this.get();
        for (int i = 0; i < booleans.length; i++) {
            builder.append(this.elementToString(booleans[i]));
            if (i < booleans.length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public static final class Adapter extends TypeAdapter {

        @Override
        public ConfigValue<?> serialize(String name, String[] comments, Object value, TypeSerializer serializer, AdapterContext context) throws IllegalAccessException {
            return new BooleanArrayValue(ValueData.of(name, (boolean[]) value, context, comments));
        }

        @Override
        public void encodeToBuffer(ConfigValue<?> value, FriendlyByteBuf buffer) {
            boolean[] arr = (boolean[]) value.get();
            buffer.writeInt(arr.length);
            for (boolean b : arr) {
                buffer.writeBoolean(b);
            }
        }

        @Override
        public Object decodeFromBuffer(ConfigValue<?> value, FriendlyByteBuf buffer) {
            boolean[] arr = new boolean[buffer.readInt()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = buffer.readBoolean();
            }
            return arr;
        }
    }
}
