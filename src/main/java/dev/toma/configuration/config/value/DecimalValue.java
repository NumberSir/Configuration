package dev.toma.configuration.config.value;

import dev.toma.configuration.config.Configurable;

import java.lang.reflect.Field;
import java.util.Objects;

public abstract class DecimalValue<N extends Number> extends ConfigValue<N> {

    protected Range range;

    public DecimalValue(ValueData<N> data, Range range) {
        super(data);
        this.range = Objects.requireNonNull(range);
    }

    @Override
    protected void readFieldData(Field field) {
        Configurable.DecimalRange decimalRange = field.getAnnotation(Configurable.DecimalRange.class);
        if (decimalRange != null) {
            this.range = Range.newBoundedRange(decimalRange.min(), decimalRange.max());
        }
    }

    @Override
    public ValidationResult apply(N n) {
        if (this.range == null || this.range.isWithin(n.longValue())) {
            return ValidationResult.valid();
        }
        return ValidationResult.error(ValidationResult.NUMBER_OUT_OF_RANGE, n, this.range.min, this.range.max);
    }

    @Override
    public abstract N getCorrectedValue(N in);

    public static final class Range {

        private final double min, max;

        private Range(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public static Range newBoundedRange(double min, double max) {
            if (min > max) {
                throw new IllegalArgumentException(String.format("Invalid number range: Min value (%f) cannot be bigger than max value (%f)", min, max));
            }
            return new Range(min, max);
        }

        public static Range unboundedDouble() {
            return newBoundedRange(-Double.MAX_VALUE, Double.MAX_VALUE);
        }

        public static Range unboundedFloat() {
            return newBoundedRange(-Float.MAX_VALUE, Float.MAX_VALUE);
        }

        public boolean isWithin(double number) {
            return number >= min && number <= max;
        }

        public double min() {
            return this.min;
        }

        public double max() {
            return this.max;
        }

        public double clamp(double in) {
            return Math.min(max, Math.max(min, in));
        }

        public float clamp(float in) {
            return (float) Math.min(max, Math.max(min, in));
        }
    }
}
