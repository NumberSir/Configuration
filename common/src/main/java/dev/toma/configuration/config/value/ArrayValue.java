package dev.toma.configuration.config.value;

public interface ArrayValue extends HierarchicalConfigValue {

    boolean isFixedSize();

    default String elementToString(Object element) {
        return element.toString();
    }
}
