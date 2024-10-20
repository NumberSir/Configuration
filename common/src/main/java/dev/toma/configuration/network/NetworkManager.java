package dev.toma.configuration.network;

import dev.toma.configuration.network.message.NetworkMessage;
import net.minecraft.server.level.ServerPlayer;

public interface NetworkManager {

    void dispatchClientMessage(ServerPlayer player, NetworkMessage message);
}
