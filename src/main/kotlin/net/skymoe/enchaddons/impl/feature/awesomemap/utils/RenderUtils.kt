package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.DungeonPlayer
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomState
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.math.float
import java.awt.Color
import kotlin.math.roundToInt

object RenderUtils {
//    val neuCheckmarks = CheckmarkSet(10, "neu")
//    val defaultCheckmarks = CheckmarkSet(16, "default")
//    val legacyCheckmarks = CheckmarkSet(8, "legacy")

    fun renderRect(
        vg: Long,
        x: Number,
        y: Number,
        w: Number,
        h: Number,
        color: Color,
    ) {
        if (color.alpha == 0) return
        NanoVGHelper.INSTANCE.drawRoundedRect(
            vg,
            x.toFloat(),
            y.toFloat(),
            w.toFloat(),
            h.toFloat(),
            color.toARGBInt(),
            4.0F, // TODO
        )
    }

    fun renderRectBorder(
        vg: Long,
        x: Double,
        y: Double,
        w: Double,
        h: Double,
        thickness: Double,
        color: Color,
    ) {
        if (color.alpha == 0) return
        NanoVGHelper.INSTANCE.drawHollowRoundRect(
            vg,
            x.toFloat(),
            y.toFloat(),
            w.toFloat(),
            h.toFloat(),
            color.toARGBInt(),
            4.0F, // TODO
            thickness.float,
        )
    }

    fun renderCenteredText(
        vg: Long,
        text: List<String>,
        x: Int,
        y: Int,
        color: Int,
    ) {
        // TODO
        if (text.isEmpty()) return
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat(), y.toFloat(), 0f)
        GlStateManager.scale(AwesomeMap.config.textScale, AwesomeMap.config.textScale, 1f)

        if (AwesomeMap.config.mapRotate) {
            GlStateManager.rotate(MC.thePlayer.rotationYaw + 180f, 0f, 0f, 1f)
        } else if (AwesomeMap.config.mapDynamicRotate) {
            GlStateManager.rotate(-MapRender.dynamicRotation, 0f, 0f, 1f)
        }

        val fontHeight = MC.fontRendererObj.FONT_HEIGHT + 1
        val yTextOffset = text.size * fontHeight / -2f

        text.withIndex().forEach { (index, text) ->
            MC.fontRendererObj.drawString(
                text,
                MC.fontRendererObj.getStringWidth(text) / -2f,
                yTextOffset + index * fontHeight,
                color,
                true,
            )
        }

        if (AwesomeMap.config.mapDynamicRotate) {
            GlStateManager.rotate(MapRender.dynamicRotation, 0f, 0f, 1f)
        }

        GlStateManager.popMatrix()
    }

    fun drawCheckmark(
        vg: Long,
        x: Float,
        y: Float,
        state: RoomState,
    ) {
//        // TODO
//        val (checkmark, size) =
//            when (AwesomeMap.config.mapCheckmark) {
//                1 -> defaultCheckmarks.getCheckmark(state) to defaultCheckmarks.size.toDouble()
//                2 -> neuCheckmarks.getCheckmark(state) to neuCheckmarks.size.toDouble()
//                3 -> legacyCheckmarks.getCheckmark(state) to legacyCheckmarks.size.toDouble()
//                else -> return
//            }
//        if (checkmark != null) {
//            GlStateManager.enableAlpha()
//            GlStateManager.color(1f, 1f, 1f, 1f)
//            MC.textureManager.bindTexture(checkmark)
//
//            drawTexturedQuad(
//                x + (MapUtils.roomSize - size) / 2,
//                y + (MapUtils.roomSize - size) / 2,
//                size,
//                size,
//            )
//            GlStateManager.disableAlpha()
//        }
    }

    fun drawPlayerHead(
        vg: Long,
        name: String,
        player: DungeonPlayer,
    ) {
//        GlStateManager.pushMatrix()
//        try {
//            // Translates to the player's location which is updated every tick.
//            if (player.isPlayer || name == MC.thePlayer.name) {
//                GlStateManager.translate(
//                    (MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first,
//                    (MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second,
//                    0.0,
//                )
//            } else {
//                GlStateManager.translate(player.mapX.toFloat(), player.mapZ.toFloat(), 0f)
//            }
//
//            // Apply head rotation and scaling
//            GlStateManager.rotate(player.yaw + 180f, 0f, 0f, 1f)
//            GlStateManager.scale(AwesomeMap.config.playerHeadScale, AwesomeMap.config.playerHeadScale, 1f)
//            GlStateManager.enableAlpha()
//
//            if (AwesomeMap.config.mapVanillaMarker && (player.isPlayer || name == MC.thePlayer.name)) {
//                GlStateManager.rotate(180f, 0f, 0f, 1f)
//                GlStateManager.color(1f, 1f, 1f, 1f)
//                MC.textureManager.bindTexture(mapIcons)
//                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
//                worldRenderer.pos(-6.0, 6.0, 0.0).tex(0.0, 0.0).endVertex()
//                worldRenderer.pos(6.0, 6.0, 0.0).tex(1.0, 0.0).endVertex()
//                worldRenderer.pos(6.0, -6.0, 0.0).tex(1.0, 1.0).endVertex()
//                worldRenderer.pos(-6.0, -6.0, 0.0).tex(0.0, 1.0).endVertex()
//                tessellator.draw()
//                GlStateManager.rotate(-180f, 0f, 0f, 1f)
//            } else {
//                // Render black border around the player head
//                renderRectBorder(-6.0, -6.0, 12.0, 12.0, 1.0, Color(0, 0, 0, 255))
//
//                preDraw()
//                GlStateManager.enableTexture2D()
//                GlStateManager.color(1f, 1f, 1f, 1f)
//
//                MC.textureManager.bindTexture(player.skin)
//
//                Gui.drawScaledCustomSizeModalRect(-6, -6, 8f, 8f, 8, 8, 12, 12, 64f, 64f)
//                if (player.renderHat) {
//                    Gui.drawScaledCustomSizeModalRect(-6, -6, 40f, 8f, 8, 8, 12, 12, 64f, 64f)
//                }
//
//                postDraw()
//            }
//
//            // Handle player names
//            if (AwesomeMap.config.playerHeads == 2 ||
//                AwesomeMap.config.playerHeads == 1 &&
//                MC.thePlayer.heldItem?.itemID.equalsOneOf(
//                    "SPIRIT_LEAP",
//                    "INFINITE_SPIRIT_LEAP",
//                    "HAUNT_ABILITY",
//                )
//            ) {
//                if (!AwesomeMap.config.mapRotate) {
//                    GlStateManager.rotate(-player.yaw + 180f, 0f, 0f, 1f)
//                }
//                GlStateManager.translate(0f, 10f, 0f)
//                GlStateManager.scale(AwesomeMap.config.playerNameScale, AwesomeMap.config.playerNameScale, 1f)
//                MC.fontRendererObj.drawString(
//                    name,
//                    -MC.fontRendererObj.getStringWidth(name) / 2f,
//                    0f,
//                    0xffffff,
//                    true,
//                )
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        GlStateManager.popMatrix()
    }

    fun Color.toARGBInt(): Int = (alpha shl 24) or (red shl 16) or (green shl 8) or blue

    fun Color.grayScale(): Color {
        val gray = (red * 0.299 + green * 0.587 + blue * 0.114).roundToInt()
        return Color(gray, gray, gray, alpha)
    }

    fun Color.darken(factor: Float): Color =
        Color((red * factor).roundToInt(), (green * factor).roundToInt(), (blue * factor).roundToInt(), alpha)

    fun Entity.getInterpolatedPosition(partialTicks: Float): Triple<Double, Double, Double> =
        Triple(
            this.lastTickPosX + (this.posX - this.lastTickPosX) * partialTicks,
            this.lastTickPosY + (this.posY - this.lastTickPosY) * partialTicks,
            this.lastTickPosZ + (this.posZ - this.lastTickPosZ) * partialTicks,
        )

    fun drawText(
        vg: Long,
        text: String,
        x: Float,
        y: Float,
        scale: Double = 1.0,
        color: Int = 0xFFFFFF,
        shadow: Boolean = true,
        center: Boolean = false,
    ) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.translate(x, y, 0f)
        GlStateManager.scale(scale, scale, scale)
        var yOffset = y - MC.fontRendererObj.FONT_HEIGHT
        text.split("\n").forEach {
            yOffset += MC.fontRendererObj.FONT_HEIGHT
            val xOffset =
                if (center) {
                    MC.fontRendererObj.getStringWidth(it) / -2f
                } else {
                    0f
                }
            MC.fontRendererObj.drawString(
                it,
                xOffset,
                0f,
                color,
                shadow,
            )
        }
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }
}
