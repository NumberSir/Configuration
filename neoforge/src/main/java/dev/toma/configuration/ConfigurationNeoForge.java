package dev.toma.configuration;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Configuration.MOD_ID)
public class ConfigurationNeoForge {

    public ConfigurationNeoForge(IEventBus eventBus) {
        Configuration.setup();
    }
}