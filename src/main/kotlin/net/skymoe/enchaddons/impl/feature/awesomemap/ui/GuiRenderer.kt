package net.skymoe.enchaddons.impl.feature.awesomemap.ui

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils
import net.skymoe.enchaddons.impl.nanovg.GUIEvent
import net.skymoe.enchaddons.util.MC

object GuiRenderer {
    val elements =
        mutableListOf(
            MapElement(),
            ScoreElement(),
        )
    private var displayTitle = ""
    private var titleTicks = 0

    fun displayTitle(
        title: String,
        ticks: Int,
    ) {
        displayTitle = title
        titleTicks = ticks
    }

    fun clearTitle() {
        displayTitle = ""
        titleTicks = 0
    }

    fun onOverlay(event: GUIEvent.HUD) {
        if (MC.currentScreen is EditLocationGui) return

        MC.entityRenderer.setupOverlayRendering()

        elements.forEach {
            if (!it.shouldRender()) return@forEach
            GlStateManager.pushMatrix()
            GlStateManager.translate(it.x.toFloat(), it.y.toFloat(), 0f)
            GlStateManager.scale(it.scale, it.scale, 1f)
            it.render()
            GlStateManager.popMatrix()
        }

        if (titleTicks > 0) {
            val sr = ScaledResolution(MC)
            RenderUtils.drawText(
                text = displayTitle,
                x = sr.scaledWidth / 2f,
                y = sr.scaledHeight / 4f,
                scale = 4.0,
                color = 0xFF5555,
                center = true,
            )
        }
    }

    fun onTick() {
        if (titleTicks > 0) titleTicks--
    }
}
