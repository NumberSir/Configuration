package dev.toma.configuration.network;

import dev.toma.configuration.Configuration;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class Networking {

    public static final Marker MARKER = MarkerManager.getMarker("Network");

    public static void sendClientPacket(ServerPlayer target, CustomPacketPayload packet) {
        target.connection.send(packet);
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Configuration.MODID);

        registrar.playToClient(S2C_SendConfigData.TYPE, S2C_SendConfigData.CODEC, S2C_SendConfigData::handle);
    }
}
