package net.skymoe.enchaddons.impl.feature.awesomemap

import net.minecraft.util.ResourceLocation
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
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
import net.skymoe.enchaddons.util.toStyledSegments
import kotlin.math.PI

data class AwesomeMapWidget(
    private val cache: NanoVGImageCache,
    private val pos: Vec2D,
    private val mapScale: Double,
    private val mapAlpha: Double = 1.0,
) : Widget<AwesomeMapWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            if (AwesomeMap.config.mapCenter) {
                nvg.save(vg)
                nvg.translate(
                    vg,
                    Vec2D(
                        -(
                            (MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier +
                                MapUtils.startCorner.first -
                                2
                        ),
                        -(
                            (MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier +
                                MapUtils.startCorner.second -
                                2
                        ),
                    ) * mapScale,
                )
            }

            if (AwesomeMap.config.mapRotate) {
                nvg.save(vg)
                nvg.translate(vg, Vec2D(pos.x + 64.0 * mapScale, pos.y + 64.0 * mapScale))

                nvg.rotate(vg, (-MC.thePlayer.rotationYaw + 180.0) * PI / 180.0)
            }
        }

//        if (AwesomeMap.config.mapRotate || AwesomeMap.config.mapDynamicRotate) {
//            context.run {
//                nvg.save(vg)
//                nvg.translate(vg, Vec2D(pos.x + 64.0, pos.y + 64.0))
//                if (AwesomeMap.config.mapDynamicRotate) {
//                    nvg.rotate(vg, -MapRender.dynamicRotation * PI / 180.0)
//                } else {
//                    nvg.rotate(vg, (-MC.thePlayer.rotationYaw + 180.0) * PI / 180.0)
//                }
//                if (AwesomeMap.config.mapCenter) {
//                    nvg.translate(
//                        vg,
//                        Vec2D(
//                            -((MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first - 2),
//                            -((MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second - 2),
//                        ),
//                    )
//                } else {
//                    nvg.translate(vg, Vec2D(-64.0, -64.0))
//                }
//            }
//        }

        val tr = (Transformation() + pos) * mapScale
        context.run {
            if (AwesomeMap.config.mapRotate) {
                nvg.translate(vg, Vec2D(-pos.x - 64.0 * mapScale, -pos.y - 64.0 * mapScale))
            }

            if (AwesomeMap.config.mapCenter) {
                nvg.translate(
                    vg,
                    Vec2D(
                        (
                            (MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier +
                                MapUtils.startCorner.first -
                                2
                        ),
                        (
                            (MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier +
                                MapUtils.startCorner.second -
                                2
                        ),
                    ) * mapScale,
                )
            }

            drawMap(tr)
            if (!Location.inBoss) {
                drawPlayerHeads(tr)
            }

            if (AwesomeMap.config.mapRotate) {
                nvg.restore(vg)
            }

            if (AwesomeMap.config.mapCenter) {
                nvg.restore(vg)
            }

            if (AwesomeMap.config.mapShowRunInformation) {
                drawRunInfo(tr)
            }
        }
    }

    //    fun setupRotate() {
//        val scale = ScaledResolution(MC).scaleFactor
//        GL11.glEnable(GL11.GL_SCISSOR_TEST)
//        GL11.glScissor(
//            (AwesomeMap.config.mapX * scale),
//            (MC.displayHeight - AwesomeMap.config.mapY * scale - 128 * scale * AwesomeMap.config.mapScale).toInt(),
//            (128 * scale * AwesomeMap.config.mapScale).toInt(),
//            (128 * scale * AwesomeMap.config.mapScale).toInt(),
//        )
//        GlStateManager.translate(64.0, 64.0, 0.0)
//        GlStateManager.rotate(-MC.thePlayer.rotationYaw + 180f, 0f, 0f, 1f)
//
//        if (AwesomeMap.config.mapCenter) {
//            GlStateManager.translate(
//                -((MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first - 2),
//                -((MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second - 2),
//                0.0,
//            )
//        } else {
//            GlStateManager.translate(-64.0, -64.0, 0.0)
//        }
//    }

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
                if (MapRender.legitRender && tile.state == RoomState.UNDISCOVERED) continue
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

//            if (AwesomeMap.config.mapCheckmark != 0 && AwesomeMap.config.mapRoomSecrets != 2) {
//                RenderUtils.drawCheckmark(xOffsetCheck, yOffsetCheck, uniqueRoom.mainRoom.state)
//            }

            val color =
                (
                    if (AwesomeMap.config.mapColorText) {
                        when (uniqueRoom.mainRoom.state) {
                            RoomState.GREEN -> AwesomeMap.config.colorTextGreen
                            RoomState.CLEARED -> AwesomeMap.config.colorTextCleared
                            RoomState.FAILED -> AwesomeMap.config.colorTextFailed
                            else -> AwesomeMap.config.colorTextUncleared
                        }
                    } else {
                        AwesomeMap.config.colorTextCleared
                    }
                ).rgb alphaScale mapAlpha

//            if (AwesomeMap.config.mapRotate) {
//                GlStateManager.rotate(MC.thePlayer.rotationYaw + 180f, 0f, 0f, 1f)
//            } else if (AwesomeMap.config.mapDynamicRotate) {
//                GlStateManager.rotate(-MapRender.dynamicRotation, 0f, 0f, 1f)
//            }

            if (AwesomeMap.config.mapRoomSecrets == 2) {
                nvg.drawTextSegments(
                    vg,
                    "${uniqueRoom.mainRoom.data.secrets}".toStyledSegments(),
                    ttr posX (xOffsetName + MapUtils.halfRoomSize).double,
                    ttr posY (yOffsetName + MapUtils.halfRoomSize + 6 * AwesomeMap.config.textScale).double,
                    ttr size 16.0 * AwesomeMap.config.textScale,
                    fontMedium(),
                    color = color,
                    anchor = Vec2D(0.5, 0.0),
                )
            }

            val name = mutableListOf<String>()

            if (AwesomeMap.config.mapRoomNames != 0 &&
                uniqueRoom.mainRoom.data.type.equalsOneOf(
                    RoomType.PUZZLE,
                    RoomType.TRAP,
                ) ||
                AwesomeMap.config.mapRoomNames == 2 &&
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

            if (uniqueRoom.mainRoom.data.type == RoomType.NORMAL && AwesomeMap.config.mapRoomSecrets == 1) {
                name.add(
                    uniqueRoom.mainRoom.data.secrets
                        .toString(),
                )
            }

            name.forEachIndexed { i, it ->
                val size = AwesomeMap.config.textScale * 8.0
                nvg.drawTextSegments(
                    vg,
                    it.toStyledSegments(),
                    ttr posX (xOffsetName + MapUtils.halfRoomSize).double,
                    ttr posY (yOffsetName + MapUtils.halfRoomSize).double - size * name.size / 2 + size * (i + 0.5),
                    ttr size size,
                    fontSemiBold(),
                    color = color,
                    anchor = Vec2D(0.5, 0.0),
                )
            }

//            if (AwesomeMap.config.mapDynamicRotate) {
//                GlStateManager.rotate(MapRender.dynamicRotation, 0f, 0f, 1f)
//            }
        }
    }

    private fun getTileColor(tile: Tile): Int {
        var color = tile.color
        if (tile.state.equalsOneOf(
                RoomState.UNDISCOVERED,
                RoomState.UNOPENED,
            ) &&
            !MapRender.legitRender &&
            Dungeon.Info.startTime != 0L
        ) {
            if (AwesomeMap.config.mapDarkenUndiscovered) {
                color = color.darken(1 - AwesomeMap.config.mapDarkenPercent)
            }
            if (AwesomeMap.config.mapGrayUndiscovered) {
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
                    DungeonPlayer(MC.thePlayer.locationSkin).apply {
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
            // Translates to the player's location which is updated every tick.
            if (player.isPlayer || name == MC.thePlayer.name) {
                nvg.translate(
                    vg,
                    Vec2D(
                        (MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first,
                        (MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second,
                    ),
                )
            } else {
                nvg.translate(vg, Vec2D(player.mapX.toFloat(), player.mapZ.toFloat()))
            }

            // Apply head rotation and scaling
            nvg.rotate(vg, (player.yaw + 180.0) * PI / 180.0)
            nvg.scale(vg, Vec2D(1 / tr.scale, 1 / tr.scale))
            val ttr = Transformation() * tr.scale * AwesomeMap.config.playerHeadScale.double

            if (AwesomeMap.config.mapVanillaMarker && (player.isPlayer || name == MC.thePlayer.name)) {
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
                    ttr size EnchAddonsConfig.dungeonConfig.awesomeMapConfig.playerHeadRadius.double,
                )
            }

            // Handle player names
            if (AwesomeMap.config.playerHeads == 2 ||
                AwesomeMap.config.playerHeads == 1 &&
                MC.thePlayer.heldItem?.itemID.equalsOneOf(
                    "SPIRIT_LEAP",
                    "INFINITE_SPIRIT_LEAP",
                    "HAUNT_ABILITY",
                )
            ) {
                if (!AwesomeMap.config.mapRotate) {
                    nvg.save(vg)
                    nvg.rotate(vg, (-player.yaw + 180.0) * PI / 180.0)
                }

                nvg.drawTextSegments(
                    vg,
                    name.toStyledSegments(),
                    ttr posX 0.0,
                    ttr posY 12.0,
                    ttr size 8.0 * AwesomeMap.config.playerNameScale,
                    fontSemiBold(),
                    color = 0xFFFFFFFF.int alphaScale mapAlpha,
                    anchor = Vec2D(0.5, 0.0),
                    shadow = Vec2D(1 / 16.0, 1 / 16.0) to 0.25,
                )

                if (!AwesomeMap.config.mapRotate) {
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
        val ttr = tr + Vec2D(64.0, 134.0)
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
