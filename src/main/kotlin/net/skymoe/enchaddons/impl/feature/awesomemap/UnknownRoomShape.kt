package net.skymoe.enchaddons.impl.feature.awesomemap

import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Transformation

object UnknownRoomShape : RoomShape {
    override fun draw(
        context: NanoVGUIContext,
        tr: Transformation,
        color: Int,
    ) {
    }
}
