package dev.toma.configuration.config.value;

import java.util.Iterator;
import java.util.Optional;

public interface HierarchicalConfigValue {

    <T> Optional<T> getChild(Iterator<String> iterator, Class<T> targetType);
}
