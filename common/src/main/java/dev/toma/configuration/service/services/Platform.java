package dev.toma.configuration.service.services;

public interface Platform {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();
}