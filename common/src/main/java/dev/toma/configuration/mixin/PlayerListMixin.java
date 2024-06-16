package dev.toma.configuration.mixin;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.network.NetworkManager;
import dev.toma.configuration.network.message.S2C_SendConfigDataMessage;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Inject(
            method = "placeNewPlayer",
            at = @At("TAIL")
    )
    private void configuration$sendServerConfigs(Connection connection, ServerPlayer player, CommonListenerCookie listenerCookie, CallbackInfo ci) {
        Set<String> networkConfigs = ConfigHolder.getSynchronizedConfigs();
        NetworkManager manager = Configuration.PLATFORM.getNetworkManager();
        networkConfigs.forEach(cfg -> manager.dispatchClientMessage(player, new S2C_SendConfigDataMessage(cfg)));
    }
}
