package net.skymoe.enchaddons.impl.feature.awesomemap.core.map

import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.RoomData
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import java.awt.Color

class Room(
    override val x: Int,
    override val z: Int,
    var data: RoomData,
) : Tile {
    var core = 0
    var isSeparator = false
    var uniqueRoom: UniqueRoom? = null
    override var state: RoomState = RoomState.UNDISCOVERED
    override val color: Color
        get() =
            if (MapRender.legitRender && state == RoomState.UNOPENED) {
                AwesomeMap.config.colorUnopened.toJavaColor()
            } else {
                when (data.type) {
                    RoomType.BLOOD -> AwesomeMap.config.colorBlood.toJavaColor()
                    RoomType.CHAMPION -> AwesomeMap.config.colorMiniboss.toJavaColor()
                    RoomType.ENTRANCE -> AwesomeMap.config.colorEntrance.toJavaColor()
                    RoomType.FAIRY -> AwesomeMap.config.colorFairy.toJavaColor()
                    RoomType.PUZZLE -> AwesomeMap.config.colorPuzzle.toJavaColor()
                    RoomType.RARE -> AwesomeMap.config.colorRare.toJavaColor()
                    RoomType.TRAP -> AwesomeMap.config.colorTrap.toJavaColor()
                    else ->
                        if (uniqueRoom?.hasMimic == true) {
                            AwesomeMap.config.colorRoomMimic.toJavaColor()
                        } else {
                            AwesomeMap.config.colorRoom.toJavaColor()
                        }
                }
            }
}
