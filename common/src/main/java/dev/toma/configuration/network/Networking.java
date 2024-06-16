package dev.toma.configuration.network;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.service.services.Platform;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public final class Networking {

    public static void sendClientPacket(ServerPlayer target, CustomPacketPayload packetPayload) {
        Platform plat = Configuration.PLATFORM;
        NetworkManager manager = plat.getNetworkManager();
        manager.dispatchClientMessage(target, packetPayload);
    }
}
