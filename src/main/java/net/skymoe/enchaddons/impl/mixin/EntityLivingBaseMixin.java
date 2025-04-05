package net.skymoe.enchaddons.impl.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.skymoe.enchaddons.impl.mixincallback.EntityLivingBaseMixinCallbackKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeathPre(DamageSource cause, CallbackInfo ci) {
        EntityLivingBaseMixinCallbackKt.onLivingEntityDeathPre((EntityLivingBase) (Object) this, cause);
    }
}
