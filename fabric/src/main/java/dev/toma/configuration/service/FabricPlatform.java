package dev.toma.configuration.service;

import dev.toma.configuration.config.Environment;
import dev.toma.configuration.network.FabricNetworkManager;
import dev.toma.configuration.network.NetworkManager;
import dev.toma.configuration.service.services.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements Platform {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Environment getEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Environment.CLIENT : Environment.SERVER;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return FabricNetworkManager.INSTANCE;
    }
}
