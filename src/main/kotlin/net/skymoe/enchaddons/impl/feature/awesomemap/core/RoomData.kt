package net.skymoe.enchaddons.impl.feature.awesomemap.core

import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomType

data class RoomData(
    val name: String,
    var type: RoomType,
    val cores: List<Int>,
    val crypts: Int,
    val secrets: Int,
    val trappedChests: Int,
) {
    companion object {
        fun createUnknown(type: RoomType) = RoomData("Unknown", type, emptyList(), 0, 0, 0)
    }
}
