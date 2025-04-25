package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import net.minecraft.entity.Entity
import java.awt.Color
import kotlin.math.roundToInt

object RenderUtils {
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
}
