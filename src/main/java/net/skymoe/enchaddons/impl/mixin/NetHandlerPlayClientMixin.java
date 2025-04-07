package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import net.skymoe.enchaddons.impl.mixincallback.NetHandlerPlayClientCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {
    @Shadow private Minecraft gameController;

    @Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER))
    public void processPacket(S02PacketChat packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS02PacketChatPre(packetIn);
    }

    @Inject(method = "handleConfirmTransaction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER))
    public void processPacket(S32PacketConfirmTransaction packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS32PacketConfirmTransactionPre(packetIn);
    }

    @Inject(method = "handlePlayerListItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER))
    public void processPacket(S38PacketPlayerListItem packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS38PacketPlayerListItemPre(packetIn);
    }

    @Inject(method = "handleTeams", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER))
    public void processPacket(S3EPacketTeams packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS3EPacketTeamsPre(packetIn);
    }

    @Inject(method = "handleMaps", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER))
    public void processPacket(S34PacketMaps packetIn, CallbackInfo ci) {
//        if (!gameController.isCallingFromMinecraftThread()) return;
        NetHandlerPlayClientCallback.INSTANCE.onS34PacketMapsPre(packetIn);
    }
}
