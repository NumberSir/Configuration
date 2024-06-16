package dev.toma.configuration.service.services;

import dev.toma.configuration.config.Environment;
import dev.toma.configuration.network.NetworkManager;

public interface Platform {

    String getPlatformName();

    Environment getEnvironment();

    NetworkManager getNetworkManager();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();
}