package net.skymoe.enchaddons.event.minecraft

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.skymoe.enchaddons.event.Event

sealed interface LivingEntityEvent : Event {
    val entity: EntityLivingBase

    data class Death(
        override val entity: EntityLivingBase,
        val cause: DamageSource,
    ) : LivingEntityEvent
}
