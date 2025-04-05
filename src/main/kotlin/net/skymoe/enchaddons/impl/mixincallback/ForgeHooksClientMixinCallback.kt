package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.client.renderer.RenderGlobal
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.RenderEvent

object ForgeHooksClientMixinCallback {
    fun onRenderWorldLast(
        context: RenderGlobal,
        partialTicks: Float,
    ) {
        RenderEvent.World
            .Last(context, partialTicks)
            .also(EA.eventDispatcher)
    }
}
