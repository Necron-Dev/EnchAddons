package net.skymoe.enchaddons.impl.feature.awesomemap.core.map

import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
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
                AwesomeMap.config.colorUnopenedDoor.toJavaColor()
            } else {
                when (type) {
                    DoorType.BLOOD -> AwesomeMap.config.colorBloodDoor.toJavaColor()
                    DoorType.ENTRANCE -> AwesomeMap.config.colorEntranceDoor.toJavaColor()
                    DoorType.WITHER ->
                        if (opened) {
                            AwesomeMap.config.colorOpenWitherDoor.toJavaColor()
                        } else {
                            AwesomeMap.config.colorWitherDoor.toJavaColor()
                        }
                    else -> AwesomeMap.config.colorRoomDoor.toJavaColor()
                }
            }
}
