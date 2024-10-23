package dev.toma.configuration.mixin;

import dev.toma.configuration.config.io.ConfigIO;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin implements TickablePacketListener, ClientGamePacketListener {

    @Inject(
            method = "handleLogin",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addPlayer(ILnet/minecraft/client/player/AbstractClientPlayer;)V", shift = At.Shift.BEFORE)
    )
    private void configuration$handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        // Set current environment to playing to disallow modification of synchronized/menu only edit fields on dedicated servers
        ConfigIO.setEnvironment(ConfigIO.ConfigEnvironment.PLAYING);
    }
}
