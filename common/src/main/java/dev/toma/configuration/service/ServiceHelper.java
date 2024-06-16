package dev.toma.configuration.service;

import dev.toma.configuration.Configuration;

import java.util.ServiceLoader;

public class ServiceHelper {

    public static <T> T loadService(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Configuration.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}