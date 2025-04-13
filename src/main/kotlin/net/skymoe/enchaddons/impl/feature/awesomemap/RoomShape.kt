package net.skymoe.enchaddons.impl.feature.awesomemap

import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.UniqueRoom
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Transformation

interface RoomShape {
    fun draw(
        context: NanoVGUIContext,
        tr: Transformation,
        color: Int,
    )
}

val UniqueRoom.roomShape: RoomShape get() {
    val minX = tiles.minOf { it.second.first }
    val maxX = tiles.maxOf { it.second.first }
    val minY = tiles.minOf { it.second.second }
    val maxY = tiles.maxOf { it.second.second }
    val w = (maxX - minX) / 2 + 1
    val h = (maxY - minY) / 2 + 1
    val nw = minX to minY
    val ne = maxX to minY
    val sw = minX to maxY
    val se = maxX to maxY
    val nwp = tiles.any { it.second == nw }
    val nep = tiles.any { it.second == ne }
    val swp = tiles.any { it.second == sw }
    val sep = tiles.any { it.second == se }
    return when {
        nwp && nep && swp && sep -> RectRoomShape(minX, minY, w, h)
        nwp && nep && swp && !sep -> LRoomShape(minX, minY, w, h, 0)
        nwp && nep && !swp && sep -> LRoomShape(minX, minY, w, h, 1)
        !nwp && nep && swp && sep -> LRoomShape(minX, minY, w, h, 2)
        nwp && !nep && swp && sep -> LRoomShape(minX, minY, w, h, 3)
        else -> UnknownRoomShape
    }
}
