package dev.toma.configuration.config.value;

import dev.toma.configuration.Configuration;
import org.apache.logging.log4j.message.FormattedMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractArrayValue<T> extends ConfigValue<T> implements ArrayValue {

    public AbstractArrayValue(ValueData<T> valueData) {
        super(valueData);
    }

    @Override
    public <V> Optional<V> getChild(Iterator<String> iterator, Class<V> targetType) {
        String key = iterator.next();
        T arrayValue = this.get();
        try {
            int length = Array.getLength(arrayValue);
            int elementIndex = Integer.parseInt(key);
            if (elementIndex < 0 || elementIndex >= length) {
                Configuration.LOGGER.warn("Attempted to get array config value {} which is out of bounds!", key);
                return Optional.empty();
            }
            Object item = Array.get(arrayValue, elementIndex);
            if (iterator.hasNext()) {
                if (item instanceof HierarchicalConfigValue hierarchicalConfigValue) {
                    return hierarchicalConfigValue.getChild(iterator, targetType);
                }
                Configuration.LOGGER.warn("Attempted to get non-existing value {} in config!", key);
            } else {
                if (targetType.isAssignableFrom(item.getClass())) {
                    return Optional.of(targetType.cast(item));
                }
                Configuration.LOGGER.warn("Attempted to get invalid value type {} in config!", key);
            }
            return Optional.empty();
        } catch (Exception e) {
            Configuration.LOGGER.error(new FormattedMessage("Failed to obtain child value for key {} due to error", key), e);
            return Optional.empty();
        }
    }
}
