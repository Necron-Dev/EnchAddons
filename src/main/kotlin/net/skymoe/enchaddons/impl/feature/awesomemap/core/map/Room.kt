package net.skymoe.enchaddons.impl.feature.awesomemap.core.map

import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
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
                EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorUnopened
                    .toJavaColor()
            } else {
                when (data.type) {
                    RoomType.BLOOD ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorBlood
                            .toJavaColor()
                    RoomType.CHAMPION ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorMiniboss
                            .toJavaColor()
                    RoomType.ENTRANCE ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorEntrance
                            .toJavaColor()
                    RoomType.FAIRY ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorFairy
                            .toJavaColor()
                    RoomType.PUZZLE ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorPuzzle
                            .toJavaColor()
                    RoomType.RARE ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorRare
                            .toJavaColor()
                    RoomType.TRAP ->
                        EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorTrap
                            .toJavaColor()
                    else ->
                        if (uniqueRoom?.hasMimic == true) {
                            EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorRoomMimic
                                .toJavaColor()
                        } else {
                            EnchAddonsConfig.dungeonConfig.awesomeMapConfig.colorRoom
                                .toJavaColor()
                        }
                }
            }
}
