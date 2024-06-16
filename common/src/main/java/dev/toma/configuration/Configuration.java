package dev.toma.configuration;

import dev.toma.configuration.service.ServiceHelper;
import dev.toma.configuration.service.services.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Configuration {

    public static final String MOD_ID = "configuration";
    public static final Logger LOGGER = LogManager.getLogger("Configuration");
    public static final Platform PLATFORM = ServiceHelper.loadService(Platform.class);

    public static void setup() {

    }
}
