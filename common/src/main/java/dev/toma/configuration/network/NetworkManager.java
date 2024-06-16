package dev.toma.configuration.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface NetworkManager {

    void dispatchClientMessage(ServerPlayer player, CustomPacketPayload message);
}
