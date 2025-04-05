package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.ForgeHooksClient;
import net.skymoe.enchaddons.impl.mixincallback.ForgeHooksClientMixinCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
    @Inject(method = "dispatchRenderLast", at = @At("HEAD"), remap = false)
    private static void onRenderWorldLast(RenderGlobal context, float partialTicks, CallbackInfo ci) {
        ForgeHooksClientMixinCallback.INSTANCE.onRenderWorldLast(context, partialTicks);
    }
}
