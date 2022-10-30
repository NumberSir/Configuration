package dev.toma.configuration.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configurable {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Comment {
        String[] value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Synchronized {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface UpdatePolicy {
        UpdatePolicyType value();
    }

    // --- String ---

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface StringPattern {
        String value();
    }

    // --- Numbers ---

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MinValue {
        double value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MaxValue {
        double value();
    }
}