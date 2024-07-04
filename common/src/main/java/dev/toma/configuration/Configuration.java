package dev.toma.configuration;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.format.ConfigFormats;
import dev.toma.configuration.config.format.IConfigFormatHandler;
import dev.toma.configuration.config.io.ConfigIO;
import dev.toma.configuration.service.ServiceHelper;
import dev.toma.configuration.service.services.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public final class Configuration {

    public static final String MODID = "configuration";
    public static final Logger LOGGER = LogManager.getLogger("Configuration");
    public static final Platform PLATFORM = ServiceHelper.loadService(Platform.class);

    public static void setup() {
        if (PLATFORM.isDevelopmentEnvironment()) {
            registerConfig(TestingConfig.class, ConfigFormats.yaml());
        }
    }

    /**
     * Registers your config class. Config will be immediately loaded upon calling.
     *
     * @param cfgClass Your config class
     * @param formatFactory File format to be used by this config class. You can use values
     *                      from {@link dev.toma.configuration.config.format.ConfigFormats} for example.
     * @return Config holder containing your config instance. You obtain it by calling
     * {@link ConfigHolder#getConfigInstance()} method.
     * @param <CFG> Config type
     */
    public static <CFG> ConfigHolder<CFG> registerConfig(Class<CFG> cfgClass, IConfigFormatHandler formatFactory) {
        Config cfg = cfgClass.getAnnotation(Config.class);
        if (cfg == null) {
            throw new IllegalArgumentException("Config class must be annotated with '@Config' annotation");
        }
        String id = cfg.id();
        String filename = cfg.filename();
        if (filename.isEmpty()) {
            filename = id;
        }
        String group = cfg.group();
        if (group.isEmpty()) {
            group = id;
        }
        ConfigHolder<CFG> holder = new ConfigHolder<>(cfgClass, id, filename, group, formatFactory);
        ConfigHolder.registerConfig(holder);
        if (cfgClass.getAnnotation(Config.NoAutoSync.class) == null) {
            ConfigIO.FILE_WATCH_MANAGER.addTrackedConfig(holder);
        }
        return holder;
    }

    /**
     * Allows you to get your config holder based on ID
     * @param id Config ID
     * @return Optional with config holder when such object exists
     * @param <CFG> Config type
     */
    public static <CFG> Optional<ConfigHolder<CFG>> getConfig(String id) {
        return ConfigHolder.getConfig(id);
    }

    /**
     * Returns list of config holders for the specified group
     * @param group Group ID
     * @return List with config holders. May be empty.
     */
    public static List<ConfigHolder<?>> getConfigsByGroup(String group) {
        return ConfigHolder.getConfigsByGroup(group);
    }
}
