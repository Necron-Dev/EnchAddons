package net.skymoe.enchaddons.impl.feature.awesomemap

import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.util.math.double
import net.skymoe.enchaddons.util.math.float

data class RectRoomShape(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) : RoomShape {
    override fun draw(
        context: NanoVGUIContext,
        tr: Transformation,
        color: Int,
    ) {
        context.run {
            helper.drawRoundedRect(
                vg,
                (tr posX (x shr 1) * (MapUtils.roomSize + MapUtils.connectorSize).double).float,
                (tr posY (y shr 1) * (MapUtils.roomSize + MapUtils.connectorSize).double).float,
                (tr size width.double * (MapUtils.roomSize + MapUtils.connectorSize) - MapUtils.connectorSize).float,
                (tr size height.double * (MapUtils.roomSize + MapUtils.connectorSize) - MapUtils.connectorSize).float,
                color,
                (tr size EnchAddonsConfig.dungeonConfig.awesomeMapConfig.mapRoomRadius.double).float,
            )
        }
    }
}
