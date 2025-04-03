package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.skymoe.enchaddons.impl.mixincallback.MinecraftMixinCallbackKt;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public WorldClient theWorld;

    @Shadow public GameSettings gameSettings;

    @Inject(method = "startGame", at = @At("RETURN"))
    private void onStartGamePost(CallbackInfo ci) {
        MinecraftMixinCallbackKt.startGamePost();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onRunTickPre(CallbackInfo ci) { MinecraftMixinCallbackKt.onRunTickPre(); }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;)V", at = @At("HEAD"))
    private void onUnloadWorldPre(CallbackInfo ci) {
        if (theWorld != null) {
            MinecraftMixinCallbackKt.onWorldUnloadPre(theWorld);
        }
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;hideGUI:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void onHideGUIPost(CallbackInfo ci) {
        MinecraftMixinCallbackKt.onHideGUIKeyDetect(gameSettings);
    }
}
