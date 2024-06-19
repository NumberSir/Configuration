package dev.toma.configuration.service;

import dev.toma.configuration.config.Environment;
import dev.toma.configuration.network.ForgeNetworkManager;
import dev.toma.configuration.network.NetworkManager;
import dev.toma.configuration.service.services.Platform;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ForgePlatform implements Platform {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public Environment getEnvironment() {
        return FMLEnvironment.dist == Dist.CLIENT ? Environment.CLIENT : Environment.SERVER;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return ForgeNetworkManager.INSTANCE;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }
}
