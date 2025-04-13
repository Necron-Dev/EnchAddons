package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.client.renderer.GlStateManager
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.*
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender.dynamicRotation
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender.legitRender
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location.inBoss
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils.connectorSize
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils.halfRoomSize
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils.roomSize
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils.darken
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils.grayScale
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.equalsOneOf
import org.lwjgl.opengl.GL11

object MapRenderList {
    var renderUpdated = false
    private var borderGlList = -1
    private var roomGlList = -1

//    fun updateRenderMap() {
//        if (borderGlList == -1) {
//            borderGlList = GL11.glGenLists(1)
//            GL11.glNewList(borderGlList, GL11.GL_COMPILE)
//            RenderUtilsGL.renderRect(
//                0.0,
//                0.0,
//                128.0,
//                if (AwesomeMap.config.mapShowRunInformation) 142.0 else 128.0,
//                AwesomeMap.config.mapBackground.toJavaColor(),
//            )
//            RenderUtilsGL.renderRectBorder(
//                0.0,
//                0.0,
//                128.0,
//                if (AwesomeMap.config.mapShowRunInformation) 142.0 else 128.0,
//                AwesomeMap.config.mapBorderWidth.toDouble(),
//                AwesomeMap.config.mapBorder.toJavaColor(),
//            )
//            GL11.glEndList()
//        }
//
//        if (renderUpdated && AwesomeMap.config.renderBeta) {
//            if (roomGlList >= 0) {
//                GL11.glDeleteLists(roomGlList, 1)
//                roomGlList = -1
//            }
//            roomGlList = GL11.glGenLists(1)
//            renderUpdated = false
//
//            GL11.glNewList(roomGlList, GL11.GL_COMPILE)
//            renderRooms()
//            renderText()
//            GL11.glEndList()
//        }
//    }
//
//    fun renderMap() {
//        if (roomGlList == -1 || borderGlList == -1 || renderUpdated) {
//            updateRenderMap()
//        }
//
//        GlStateManager.pushMatrix()
//        RenderUtils.preDraw()
//        RenderUtilsGL.preDraw()
//
//        if (borderGlList != -1) GL11.glCallList(borderGlList)
//
//        if (AwesomeMap.config.mapRotate) {
//            GlStateManager.pushMatrix()
//            MapRender.setupRotate()
//        } else if (AwesomeMap.config.mapDynamicRotate) {
//            GlStateManager.translate(64.0, 64.0, 0.0)
//            GlStateManager.rotate(dynamicRotation, 0f, 0f, 1f)
//            GlStateManager.translate(-64.0, -64.0, 0.0)
//        }
//
//        if (roomGlList != -1) GL11.glCallList(roomGlList)
//
//        RenderUtilsGL.unbindTexture()
//        RenderUtils.postDraw()
//        RenderUtilsGL.postDraw()
//        GlStateManager.popMatrix()
//
//        if (!inBoss) {
//            MapRender.renderPlayerHeads()
//        }
//
//        if (AwesomeMap.config.mapRotate) {
//            GL11.glDisable(GL11.GL_SCISSOR_TEST)
//            GlStateManager.popMatrix()
//        } else if (AwesomeMap.config.mapDynamicRotate) {
//            GlStateManager.translate(64.0, 64.0, 0.0)
//            GlStateManager.rotate(-dynamicRotation, 0f, 0f, 1f)
//            GlStateManager.translate(-64.0, -64.0, 0.0)
//        }
//
//        if (AwesomeMap.config.mapShowRunInformation) {
//            MapRender.renderRunInformation()
//        }
//    }
//
//    private fun renderRooms() {
//        GlStateManager.translate(MapUtils.startCorner.first.toFloat(), MapUtils.startCorner.second.toFloat(), 0f)
//
//        var yPos = 0
//        var yStep = 0
//
//        for (y in 0..10) {
//            val yEven = y % 2 == 0
//            yPos += yStep
//            yStep = if (yEven) roomSize else connectorSize
//            var xPos = 0
//            var xStep = 0
//            for (x in 0..10) {
//                val xEven = x % 2 == 0
//                xPos += xStep
//                xStep = if (xEven) roomSize else connectorSize
//
//                val tile = Dungeon.Info.dungeonList[y * 11 + x]
//                if (tile is Unknown) continue
//                if (legitRender && tile.state == RoomState.UNDISCOVERED) continue
//
//                var color = tile.color
//
//                if (tile.state.equalsOneOf(RoomState.UNDISCOVERED, RoomState.UNOPENED) &&
//                    !legitRender &&
//                    Dungeon.Info.startTime != 0L
//                ) {
//                    if (AwesomeMap.config.mapDarkenUndiscovered) {
//                        color = color.darken(1 - AwesomeMap.config.mapDarkenPercent)
//                    }
//                    if (AwesomeMap.config.mapGrayUndiscovered) {
//                        color = color.grayScale()
//                    }
//                }
//
//                when (tile) {
//                    is Room -> {
//                        RenderUtilsGL.renderRect(xPos, yPos, xStep, yStep, color)
//                        if (legitRender && tile.state == RoomState.UNOPENED) {
//                            RenderUtilsGL.drawCheckmark(xPos.toFloat(), yPos.toFloat(), tile.state)
//                        }
//                    }
//
//                    is Door -> {
//                        val doorOffset = if (roomSize == 16) 5 else 6
//                        if (xEven) {
//                            RenderUtilsGL.renderRect(xPos + doorOffset, yPos, xStep - doorOffset * 2, yStep, color)
//                        } else {
//                            RenderUtilsGL.renderRect(xPos, yPos + doorOffset, xStep, yStep - doorOffset * 2, color)
//                        }
//                    }
//                }
//            }
//        }
//        GlStateManager.translate(-MapUtils.startCorner.first.toFloat(), -MapUtils.startCorner.second.toFloat(), 0f)
//    }
//
//    private fun renderText() {
//        GlStateManager.translate(MapUtils.startCorner.first.toFloat(), MapUtils.startCorner.second.toFloat(), 0f)
//
//        Dungeon.Info.uniqueRooms.forEach { unique ->
//            val room = unique.mainRoom
//            if (legitRender && room.state.equalsOneOf(RoomState.UNDISCOVERED, RoomState.UNOPENED)) return@forEach
//            val checkPos = unique.getCheckmarkPosition()
//            val namePos = unique.getNamePosition()
//            val xPosCheck = (checkPos.first / 2f) * (roomSize + connectorSize)
//            val yPosCheck = (checkPos.second / 2f) * (roomSize + connectorSize)
//            val xPosName = (namePos.first / 2f) * (roomSize + connectorSize)
//            val yPosName = (namePos.second / 2f) * (roomSize + connectorSize)
//
//            if (AwesomeMap.config.mapCheckmark != 0 && AwesomeMap.config.mapRoomSecrets != 2) {
//                RenderUtilsGL.drawCheckmark(xPosCheck, yPosCheck, room.state)
//            }
//
//            val color =
//                if (AwesomeMap.config.mapColorText) {
//                    when (room.state) {
//                        RoomState.GREEN -> AwesomeMap.config.colorTextGreen
//                        RoomState.CLEARED -> AwesomeMap.config.colorTextCleared
//                        RoomState.FAILED -> AwesomeMap.config.colorTextFailed
//                        else -> AwesomeMap.config.colorTextUncleared
//                    }
//                } else {
//                    AwesomeMap.config.colorTextCleared
//                }
//
//            if (AwesomeMap.config.mapRoomSecrets == 2) {
//                GlStateManager.pushMatrix()
//                GlStateManager.translate(
//                    xPosCheck + halfRoomSize,
//                    yPosCheck + 2 + halfRoomSize,
//                    0f,
//                )
//                GlStateManager.scale(2f, 2f, 1f)
//                RenderUtilsGL.renderCenteredText(listOf(room.data.secrets.toString()), 0, 0, color.toJavaColor())
//                GlStateManager.popMatrix()
//            }
//
//            val name = mutableListOf<String>()
//
//            if (AwesomeMap.config.mapRoomNames != 0 &&
//                room.data.type.equalsOneOf(
//                    RoomType.PUZZLE,
//                    RoomType.TRAP,
//                ) ||
//                AwesomeMap.config.mapRoomNames == 2 &&
//                room.data.type.equalsOneOf(
//                    RoomType.NORMAL,
//                    RoomType.RARE,
//                    RoomType.CHAMPION,
//                )
//            ) {
//                name.addAll(room.data.name.split(" "))
//            }
//            if (room.data.type == RoomType.NORMAL && AwesomeMap.config.mapRoomSecrets == 1) {
//                name.add(room.data.secrets.toString())
//            }
//            // Offset + half of roomsize
//            RenderUtilsGL.renderCenteredText(
//                name,
//                xPosName.toInt() + halfRoomSize,
//                yPosName.toInt() + halfRoomSize,
//                color.toJavaColor(),
//            )
//        }
//        GlStateManager.translate(-MapUtils.startCorner.first.toFloat(), -MapUtils.startCorner.second.toFloat(), 0f)
//    }
}
