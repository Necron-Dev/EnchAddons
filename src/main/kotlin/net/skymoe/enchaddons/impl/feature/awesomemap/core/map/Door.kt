package net.skymoe.enchaddons.impl.feature.awesomemap.core.map

import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import java.awt.Color

class Door(
    override val x: Int,
    override val z: Int,
    var type: DoorType,
) : Tile {
    var opened = false
    override var state: RoomState = RoomState.UNDISCOVERED
    override val color: Color
        get() =
            if (MapRender.legitRender && state == RoomState.UNOPENED) {
                EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorUnopenedDoor
                    .toJavaColor()
            } else {
                when (type) {
                    DoorType.BLOOD ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorBloodDoor
                            .toJavaColor()
                    DoorType.ENTRANCE ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorEntranceDoor
                            .toJavaColor()
                    DoorType.WITHER ->
                        if (opened) {
                            EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorOpenWitherDoor
                                .toJavaColor()
                        } else {
                            EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorWitherDoor
                                .toJavaColor()
                        }
                    else ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorRoomDoor
                            .toJavaColor()
                }
            }
}
