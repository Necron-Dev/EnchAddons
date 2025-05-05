package net.skymoe.enchaddons.impl.feature.awesomemap

import net.minecraft.util.ResourceLocation
import net.skymoe.enchaddons.impl.config.feature.AwesomeMapConfigImpl
import net.skymoe.enchaddons.impl.feature.awesomemap.core.DungeonPlayer
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.*
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.Dungeon
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.DungeonScan
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender.legitRender
import net.skymoe.enchaddons.impl.feature.awesomemap.ui.ScoreElement
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils.darken
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils.grayScale
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.equalsOneOf
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.itemID
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.oneconfig.FONT_MEDIUM_Y_OFFSET
import net.skymoe.enchaddons.impl.oneconfig.NanoVGImageCache
import net.skymoe.enchaddons.impl.oneconfig.fontMedium
import net.skymoe.enchaddons.impl.oneconfig.fontSemiBold
import net.skymoe.enchaddons.impl.oneconfig.loadFonts
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.alphaScale
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double
import net.skymoe.enchaddons.util.math.float
import net.skymoe.enchaddons.util.math.int
import net.skymoe.enchaddons.util.math.lerp
import net.skymoe.enchaddons.util.partialTicks
import net.skymoe.enchaddons.util.renderPos
import net.skymoe.enchaddons.util.toStyledSegments
import kotlin.math.PI

data class AwesomeMapWidget(
    private val cache: NanoVGImageCache,
    private val pos: Vec2D,
    private val mapScale: Double,
    private val width: Double,
    private val height: Double,
    private val config: AwesomeMapConfigImpl,
    private val mapAlpha: Double = 1.0,
) : Widget<AwesomeMapWidget> {
    override fun draw(context: NanoVGUIContext) {
        val realScale = mapScale
        context.run {
            nvg.save(vg)

            if (config.mapCenter || config.mapRotateMode != 0) {
                nvg.save(vg)
            }

            if (config.mapClip) {
                nvg.scissor(vg, pos.x, pos.y, width * mapScale, height * mapScale)
            }

            if (config.mapRotateMode != 0) {
                nvg.translate(vg, Vec2D(pos.x + 64.0 * mapScale, pos.y + 64.0 * mapScale))
                nvg.rotate(
                    vg,
                    when (config.mapRotateMode) {
                        1 -> -MC.thePlayer.rotationYaw + 180.0
                        2 -> MapRender.dynamicRotation.double
                        else -> -MC.thePlayer.rotationYaw + 180.0
                    } * PI / 180.0,
                )
            }

            if (config.mapCenter) {
                nvg.translate(
                    vg,
                    (
                        Vec2D(
                            (MC.thePlayer.renderPos.x - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first,
                            (MC.thePlayer.renderPos.z - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second,
                        ) * -1.0 + Vec2D(64.0, 64.0)
                    ) * realScale,
                )
            }
        }

        val tr = (Transformation() + pos) * realScale
        context.run {
            if (config.mapRotateMode != 0) {
                nvg.translate(vg, Vec2D(-pos.x - 64.0 * mapScale, -pos.y - 64.0 * mapScale))

                if (config.mapCenter) {
                    nvg.translate(vg, Vec2D(-1.0, -1.0) * realScale)
                }
            }

            drawMap(tr)
            if (!Location.inBoss) {
                drawPlayerHeads(tr)
            }

            if (config.mapClip) {
                nvg.resetScissor(vg)
            }

            if (config.mapCenter || config.mapRotateMode != 0) {
                nvg.restore(vg)
            }

            if (config.mapShowRunInformation) {
                drawRunInfo(tr)
            }

            nvg.restore(vg)
        }
    }

    private fun NanoVGUIContext.drawMap(tr: Transformation) {
        val ttr =
            tr +
                Vec2D(
                    MapUtils.startCorner.first.double + 1,
                    MapUtils.startCorner.second.double + 1,
                )

        val uniqueRooms = mutableSetOf<UniqueRoom>()
        val doors = mutableSetOf<Pair<Door, Pair<Int, Int>>>()

        for (y in 0..10) {
            for (x in 0..10) {
                val tile = Dungeon.Info.dungeonList[y * 11 + x]
                if (tile is Unknown) continue
                if (legitRender && tile.state == RoomState.UNDISCOVERED) continue
                if (tile is Room) tile.uniqueRoom?.also(uniqueRooms::add)
                if (tile is Door) doors.add(tile to (x to y))
            }
        }

        doors.forEach { (door, pos) ->
            val (x, y) = pos
            val yEven = y and 1 == 0
            val xOffset = (x shr 1) * (MapUtils.roomSize + MapUtils.connectorSize)
            val yOffset = (y shr 1) * (MapUtils.roomSize + MapUtils.connectorSize)
            val doorwayOffset = if (MapUtils.roomSize == 16) 5 else 6
            val width = 6
            var x1 = if (yEven) xOffset + MapUtils.roomSize else xOffset
            var y1 = if (yEven) yOffset else yOffset + MapUtils.roomSize
            if (yEven) y1 += doorwayOffset else x1 += doorwayOffset
            helper.drawRect(
                vg,
                (ttr posX x1.double).float,
                (ttr posY y1.double).float,
                (ttr size (if (yEven) MapUtils.connectorSize else width).double).float,
                (ttr size (if (yEven) width else MapUtils.connectorSize).double).float,
                getTileColor(door),
            )
        }

        uniqueRooms.forEach { uniqueRoom ->
            uniqueRoom.roomShape.draw(this, ttr, getTileColor(uniqueRoom.mainRoom))
            if (legitRender && uniqueRoom.mainRoom.state.equalsOneOf(RoomState.UNDISCOVERED, RoomState.UNOPENED)) return@forEach
            val checkPos = uniqueRoom.getCheckmarkPosition()
            val namePos = uniqueRoom.getNamePosition()

            val xOffsetCheck = (checkPos.first / 2f) * (MapUtils.roomSize + MapUtils.connectorSize)
            val yOffsetCheck = (checkPos.second / 2f) * (MapUtils.roomSize + MapUtils.connectorSize)
            val xOffsetName = (namePos.first / 2f) * (MapUtils.roomSize + MapUtils.connectorSize)
            val yOffsetName = (namePos.second / 2f) * (MapUtils.roomSize + MapUtils.connectorSize)

            nvg.save(vg)
            nvg.translate(
                vg,
                Vec2D(
                    ttr posX (xOffsetCheck.double + MapUtils.roomSize / 2.0),
                    ttr posY (yOffsetCheck.double + MapUtils.roomSize / 2.0),
                ),
            )

            if (config.mapRotateMode != 0) {
                nvg.rotate(
                    vg,
                    when (config.mapRotateMode) {
                        1 -> MC.thePlayer.rotationYaw + 180.0
                        2 -> -MapRender.dynamicRotation.double
                        else -> MC.thePlayer.rotationYaw + 180.0
                    } * PI / 180.0,
                )
            }

            if (config.mapCheckmark && config.mapRoomSecrets != 2) {
                val state = uniqueRoom.mainRoom.state

                val color =
                    when (state) {
                        RoomState.CLEARED -> config.colorCheckMarkCleared.rgb
                        RoomState.FAILED -> config.colorCheckMarkFailed.rgb
                        RoomState.GREEN -> config.colorCheckMarkGreen.rgb
                        else -> 0
                    }

                if (state == RoomState.FAILED) {
                    nvg.drawCrossMark(
                        vg,
                        ttr size -6.0,
                        ttr size -6.0,
                        ttr size 12.0,
                        ttr size 1.5,
                        color,
                    )
                } else {
                    nvg.drawCheckMark(
                        vg,
                        ttr size -6.0,
                        ttr size -6.0,
                        ttr size 12.0,
                        ttr size 1.5,
                        color,
                    )
                }
            }

            val color =
                (
                    if (config.mapColorText) {
                        when (uniqueRoom.mainRoom.state) {
                            RoomState.GREEN -> config.colorTextGreen
                            RoomState.CLEARED -> config.colorTextCleared
                            RoomState.FAILED -> config.colorTextFailed
                            else -> config.colorTextUncleared
                        }
                    } else {
                        config.colorTextCleared
                    }
                ).rgb alphaScale mapAlpha

            if (config.mapRoomSecrets == 2) {
                nvg.drawTextSegments(
                    vg,
                    "${uniqueRoom.mainRoom.data.secrets}".toStyledSegments(),
                    .0,
                    ttr size FONT_MEDIUM_Y_OFFSET.double,
                    ttr size 16.0 * config.textScale,
                    fontMedium(),
                    color = color,
                    anchor = Vec2D(0.5, 0.5),
                )
            }

            nvg.restore(vg)
        }

        uniqueRooms.forEach { uniqueRoom ->
            val namePos = uniqueRoom.getNamePosition()
            val xOffsetName = (namePos.first / 2f) * (MapUtils.roomSize + MapUtils.connectorSize)
            val yOffsetName = (namePos.second / 2f) * (MapUtils.roomSize + MapUtils.connectorSize)

            val color =
                (
                    if (config.mapColorText) {
                        when (uniqueRoom.mainRoom.state) {
                            RoomState.GREEN -> config.colorTextGreen
                            RoomState.CLEARED -> config.colorTextCleared
                            RoomState.FAILED -> config.colorTextFailed
                            else -> config.colorTextUncleared
                        }
                    } else {
                        config.colorTextCleared
                    }
                ).rgb alphaScale mapAlpha

            val name = mutableListOf<String>()

            if (config.mapRoomNames != 0 &&
                uniqueRoom.mainRoom.data.type.equalsOneOf(
                    RoomType.PUZZLE,
                    RoomType.TRAP,
                ) ||
                config.mapRoomNames == 2 &&
                uniqueRoom.mainRoom.data.type.equalsOneOf(
                    RoomType.NORMAL,
                    RoomType.RARE,
                    RoomType.CHAMPION,
                )
            ) {
                name.addAll(
                    uniqueRoom.mainRoom.data.name
                        .split(" "),
                )
            }

            if (uniqueRoom.mainRoom.data.type == RoomType.NORMAL && config.mapRoomSecrets == 1) {
                name.add(
                    uniqueRoom.mainRoom.data.secrets
                        .toString(),
                )
            }

            name.forEachIndexed { i, it ->
                val size = config.textScale * 8.0

                nvg.save(vg)
                nvg.translate(
                    vg,
                    Vec2D(
                        ttr posX (xOffsetName + MapUtils.halfRoomSize).double,
                        ttr posY (yOffsetName + MapUtils.halfRoomSize).double,
                    ),
                )

                if (config.mapRotateMode != 0) {
                    nvg.rotate(
                        vg,
                        when (config.mapRotateMode) {
                            1 -> MC.thePlayer.rotationYaw + 180.0
                            2 -> -MapRender.dynamicRotation.double
                            else -> MC.thePlayer.rotationYaw + 180.0
                        } * PI / 180.0,
                    )
                }

                nvg.translate(
                    vg,
                    Vec2D(
                        .0,
                        ttr size -size * name.size / 2 + size * (i + 0.5),
                    ),
                )

                nvg.drawTextSegments(
                    vg,
                    it.toStyledSegments(),
                    0.0,
                    0.0,
                    ttr size size,
                    fontSemiBold(),
                    color = color,
                    anchor = Vec2D(0.5, 0.5),
                )

                nvg.restore(vg)
            }
        }
    }

    private fun getTileColor(tile: Tile): Int {
        var color = tile.color
        if (tile.state.equalsOneOf(
                RoomState.UNDISCOVERED,
                RoomState.UNOPENED,
            ) &&
            !legitRender &&
            Dungeon.Info.startTime != 0L
        ) {
            if (config.mapDarkenUndiscovered) {
                color = color.darken(1 - config.mapDarkenPercent)
            }
            if (config.mapGrayUndiscovered) {
                color = color.grayScale()
            }
        }
        return color.rgb alphaScale mapAlpha
    }

    private fun NanoVGUIContext.drawPlayerHeads(tr: Transformation) {
        try {
            if (Dungeon.dungeonTeammates.isEmpty()) {
                drawPlayerHead(
                    tr,
                    MC.thePlayer.name,
                    DungeonPlayer(MC.thePlayer.locationSkin, MC.thePlayer.uniqueID).apply {
                        yaw = MC.thePlayer.rotationYaw
                    },
                )
            } else {
                Dungeon.dungeonTeammates.forEach { (name, teammate) ->
                    if (!teammate.dead) {
                        drawPlayerHead(tr, name, teammate)
                    }
                }
            }
        } catch (_: ConcurrentModificationException) {
        }
    }

    private fun NanoVGUIContext.drawPlayerHead(
        tr: Transformation,
        name: String,
        player: DungeonPlayer,
    ) {
        nvg.save(vg)
        nvg.translate(vg, tr.offset)
        nvg.scale(vg, Vec2D(tr.scale, tr.scale))
        nvg.translate(vg, Vec2D(1.0, 1.0))
        try {
            val isLocalPlayer = player.isPlayer || name == MC.thePlayer.name

            // Translates to the player's location, which is updated every tick.
            if (isLocalPlayer) {
                nvg.translate(
                    vg,
                    Vec2D(
                        (MC.thePlayer.renderPos.x - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first,
                        (MC.thePlayer.renderPos.z - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second,
                    ),
                )
            } else {
                nvg.translate(
                    vg,
                    Vec2D(
                        lerp(player.prevPosition.mapX, player.position.mapX, partialTicks),
                        lerp(player.prevPosition.mapZ, player.position.mapZ, partialTicks),
                    ),
                )
            }

            // Apply head rotation
            if (isLocalPlayer) {
                nvg.rotate(vg, (MC.thePlayer.rotationYaw + 180.0) * PI / 180.0)
            } else {
                nvg.rotate(vg, (player.getRenderYaw() + 180.0) * PI / 180.0)
            }

            // Apply scaling
            nvg.scale(vg, Vec2D(1 / tr.scale, 1 / tr.scale))
            val ttr = Transformation() * tr.scale * config.playerHeadScale.double

            if (config.mapVanillaMarker && (player.isPlayer || name == MC.thePlayer.name)) {
                nvg.drawRoundedTexture(
                    vg,
                    cache["marker"],
                    MC.textureManager.getTexture(ResourceLocation("funnymap", "marker.png")).glTextureId,
                    0.0,
                    0.0,
                    1.0,
                    1.0,
                    ttr posX -6.0,
                    ttr posY -6.0,
                    ttr size 12.0,
                    ttr size 12.0,
                    mapAlpha,
                    ttr size 0.0,
                )
            } else {
//                // Render black border around the player head
//                renderRectBorder(-6.0, -6.0, 12.0, 12.0, 1.0, Color(0, 0, 0, 255))
                nvg.drawRoundedPlayerAvatar(
                    vg,
                    cache[player.uuid],
                    MC.textureManager.getTexture(player.skin).glTextureId,
                    hat = true,
                    scaleHat = true,
                    ttr posX -6.0,
                    ttr posY -6.0,
                    ttr size 12.0,
                    ttr size 12.0,
                    mapAlpha,
                    ttr size config.playerHeadRadius.double,
                )
            }

            // Handle player names
            if (config.playerHeads == 2 ||
                config.playerHeads == 1 &&
                MC.thePlayer.heldItem?.itemID.equalsOneOf(
                    "SPIRIT_LEAP",
                    "INFINITE_SPIRIT_LEAP",
                    "HAUNT_ABILITY",
                )
            ) {
                if (config.mapRotateMode != 1) {
                    nvg.save(vg)
                    val rotateAngle =
                        if (isLocalPlayer) {
                            -MC.thePlayer.rotationYaw.double
                        } else {
                            -player.getRenderYaw()
                        } +
                            if (config.mapRotateMode == 2) {
                                -MapRender.dynamicRotation.double
                            } else {
                                .0
                            }
                    nvg.rotate(vg, (rotateAngle + 180.0) * PI / 180.0)
                }

                nvg.drawTextSegments(
                    vg,
                    name.toStyledSegments(),
                    ttr posX 0.0,
                    ttr posY 8.0,
                    ttr size 8.0 * config.playerNameScale,
                    fontSemiBold(),
                    color = 0xFFFFFFFF.int alphaScale mapAlpha,
                    anchor = Vec2D(0.5, 0.0),
                    shadow = Vec2D(1 / 16.0, 1 / 16.0) to 0.25,
                )

                if (config.mapRotateMode != 1) {
                    nvg.restore(vg)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        nvg.restore(vg)
    }

    private fun NanoVGUIContext.drawRunInfo(tr: Transformation) {
        nvg.loadFonts(vg)
        val ttr = tr + Vec2D(64.0, 130.0)
        val lines = ScoreElement.runInformationLines()
        val lineOne = lines.takeWhile { it != "split" }.joinToString(separator = "  ")
        val lineTwo = lines.takeLastWhile { it != "split" }.joinToString(separator = "  ")
        nvg.drawTextSegments(
            vg,
            lineOne.toStyledSegments(),
            ttr posX 0.0,
            ttr posY 0.0,
            ttr size 8.0,
            fontSemiBold(),
            color = 0xFFFFFFFF.int alphaScale mapAlpha,
            anchor = Vec2D(0.5, 0.0),
            shadow = Vec2D(1 / 16.0, 1 / 16.0) to 0.25,
        )
        nvg.drawTextSegments(
            vg,
            lineTwo.toStyledSegments(),
            ttr posX 0.0,
            ttr posY 9.0,
            ttr size 8.0,
            fontSemiBold(),
            color = 0xFFFFFFFF.int alphaScale mapAlpha,
            anchor = Vec2D(0.5, 0.0),
            shadow = Vec2D(1 / 16.0, 1 / 16.0) to 0.25,
        )
    }

    override fun alphaScale(alpha: Double): AwesomeMapWidget =
        copy(
            mapAlpha = alpha,
        )

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ): AwesomeMapWidget =
        copy(
            pos = lerp(origin, pos, scale),
            mapScale = mapScale * scale,
        )
}
