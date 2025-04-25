package net.skymoe.enchaddons.impl.feature.awesomemap

import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.math.double

data class LRoomShape(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val corner: Int,
) : RoomShape {
    override fun draw(
        context: NanoVGUIContext,
        tr: Transformation,
        color: Int,
    ) {
        context.run {
            nvg.drawAccarc(
                vg,
                corner,
                tr posX (x shr 1) * (MapUtils.roomSize + MapUtils.connectorSize).double,
                tr posY (y shr 1) * (MapUtils.roomSize + MapUtils.connectorSize).double,
                tr size width.double * (MapUtils.roomSize + MapUtils.connectorSize) - MapUtils.connectorSize,
                tr size height.double * (MapUtils.roomSize + MapUtils.connectorSize) - MapUtils.connectorSize,
                tr size MapUtils.roomSize.double,
                tr size MapUtils.roomSize.double,
                tr size EnchAddonsConfig.dungeonConfig.awesomeMapConfig.mapRoomRadius.double,
                tr size EnchAddonsConfig.dungeonConfig.awesomeMapConfig.mapLShapeRoomInnerRadius.double,
                color,
            )
        }
    }
}
