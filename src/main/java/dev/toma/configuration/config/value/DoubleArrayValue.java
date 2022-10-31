package dev.toma.configuration.config.value;

import dev.toma.configuration.config.adapter.TypeAdapter;
import dev.toma.configuration.config.exception.ConfigValueMissingException;
import dev.toma.configuration.config.format.IConfigFormat;
import net.minecraft.network.PacketBuffer;

public class DoubleArrayValue extends ConfigValue<double[]> {

    public DoubleArrayValue(ValueData<double[]> valueData) {
        super(valueData);
    }

    @Override
    protected void serialize(IConfigFormat format) {
        format.writeDoubleArray(this.getId(), this.get());
    }

    @Override
    protected void deserialize(IConfigFormat format) throws ConfigValueMissingException {
        this.set(format.readDoubleArray(this.getId()));
    }

    public static final class Adapter extends TypeAdapter {

        public Adapter() {
            super("double[]");
        }

        @Override
        public boolean isTargetType(Class<?> type) {
            return type.equals(double[].class);
        }

        @Override
        public void encodeToBuffer(ConfigValue<?> value, PacketBuffer buffer) {
            double[] arr = (double[]) value.get();
            buffer.writeInt(arr.length);
            for (double v : arr) {
                buffer.writeDouble(v);
            }
        }

        @Override
        public Object decodeFromBuffer(ConfigValue<?> value, PacketBuffer buffer) {
            double[] arr = new double[buffer.readInt()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = buffer.readDouble();
            }
            return arr;
        }

        @Override
        public ConfigValue<?> serialize(String name, String[] comments, Object value, TypeSerializer serializer, AdapterContext context) throws IllegalAccessException {
            return new DoubleArrayValue(ValueData.of(name, (double[]) value, context, comments));
        }
    }
}
