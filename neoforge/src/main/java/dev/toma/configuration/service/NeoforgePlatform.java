package dev.toma.configuration.service;

import dev.toma.configuration.config.Environment;
import dev.toma.configuration.network.NeoforgeNetworkManager;
import dev.toma.configuration.network.NetworkManager;
import dev.toma.configuration.service.services.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;

public class NeoforgePlatform implements Platform {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Environment getEnvironment() {
        return FMLEnvironment.dist == Dist.CLIENT ? Environment.CLIENT : Environment.SERVER;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return NeoforgeNetworkManager.INSTANCE;
    }
}