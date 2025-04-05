package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.LivingEntityEvent

fun onLivingEntityDeathPre(
    entity: EntityLivingBase,
    cause: DamageSource,
) {
    LivingEntityEvent
        .Death(entity, cause)
        .also(EA.eventDispatcher)
}
