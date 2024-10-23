package dev.toma.configuration.network;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.network.message.NetworkMessage;
import dev.toma.configuration.network.message.S2C_SendConfigDataMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class ForgeNetworkManager implements NetworkManager {

    public static final ForgeNetworkManager INSTANCE = new ForgeNetworkManager();
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Configuration.MODID, "network"))
            .networkProtocolVersion(() -> "1")
            .clientAcceptedVersions(ver -> ver.equals("1"))
            .serverAcceptedVersions(ver -> ver.equals("1"))
            .simpleChannel();
    private static int messageIndex;

    public static void registerMessages() {
        registerMessage(S2C_SendConfigDataMessage.class, S2C_SendConfigDataMessage::read);
    }

    @Override
    public void dispatchClientMessage(ServerPlayer player, NetworkMessage message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    private static <T extends NetworkMessage> void registerMessage(Class<T> message, Function<FriendlyByteBuf, T> constructor) {
        CHANNEL.registerMessage(messageIndex++, message, NetworkMessage::write, constructor, (payload, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                payload.handle(player);
            });
        });
    }
}
