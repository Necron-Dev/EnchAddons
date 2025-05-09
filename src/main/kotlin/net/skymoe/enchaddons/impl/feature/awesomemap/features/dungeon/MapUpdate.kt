package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.core.DungeonPlayer
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.*
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils.mapX
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils.mapZ
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils.yaw
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.TabList
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.equalsOneOf
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.currPos
import net.skymoe.enchaddons.util.math.double

object MapUpdate {
    var roomAdded = false

    fun preloadHeads() {
        val tabEntries = TabList.getDungeonTabList() ?: return
        for (i in listOf(5, 9, 13, 17, 1)) {
            // Accessing the skin locations to load in skin
            tabEntries[i].first.locationSkin
        }
    }

    fun getPlayers() {
        val tabEntries = TabList.getDungeonTabList() ?: return
        Dungeon.dungeonTeammates.clear()
        var iconNum = 0
        for (i in listOf(5, 9, 13, 17, 1)) {
            with(tabEntries[i]) {
                val name =
                    StringUtils
                        .stripControlCodes(second)
                        .trim()
                        .substringAfterLast("] ")
                        .split(" ")[0]
                if (name != "") {
                    Dungeon.dungeonTeammates[name] =
                        DungeonPlayer(first.locationSkin, TabList.getPlayerUUIDByName(name)).apply {
                            MC.theWorld.getPlayerEntityByName(name)?.let { setData(it) }
                            colorPrefix = second.substringBefore(name, "f").last()
                            this.name = name
                            icon = "icon-$iconNum"
                        }
                    iconNum++
                }
            }
        }
    }

    fun updatePlayers(tabEntries: List<Pair<NetworkPlayerInfo, String>>) {
        if (Dungeon.dungeonTeammates.isEmpty()) return
        // Update map icons
        val time = System.currentTimeMillis() - Dungeon.Info.startTime
        var iconNum = 0
        for (i in listOf(5, 9, 13, 17, 1)) {
            val tabText = StringUtils.stripControlCodes(tabEntries[i].second).trim()
            val name = tabText.substringAfterLast("] ").split(" ")[0]
            if (name == "") continue
            Dungeon.dungeonTeammates[name]?.run {
                dead = tabText.contains("(DEAD)")
                if (dead) {
                    icon = ""
                } else {
                    icon = "icon-$iconNum"
                    iconNum++
                }
                if (!playerLoaded) {
                    MC.theWorld.getPlayerEntityByName(name)?.let { setData(it) }
                }

                val room = getCurrentRoom()
                if (room != "Error" || time > 1000) {
                    if (lastRoom == "") {
                        lastRoom = room
                    } else if (lastRoom != room) {
                        roomVisits.add(Pair(time - lastTime, lastRoom))
                        lastTime = time
                        lastRoom = room
                    }
                }
            }
        }

        val decor = MapUtils.mapData?.mapDecorations ?: return
        Dungeon.dungeonTeammates.forEach { (name, player) ->
            decor.entries.find { (icon, _) -> icon == player.icon }?.let { (_, vec4b) ->
                player.isPlayer = vec4b.func_176110_a().toInt() == 1

                val entityPlayer = MC.theWorld?.getPlayerEntityByUUID(player.uuid)

                player.prevYaw = player.yaw
                player.prevPosition.mapX = player.position.mapX
                player.prevPosition.mapZ = player.position.mapZ

                if (entityPlayer != null) {
                    player.yaw = entityPlayer.rotationYaw
                    player.position.mapX =
                        (entityPlayer.currPos.x - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first
                    player.position.mapZ =
                        (entityPlayer.currPos.z - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second
                } else {
                    player.yaw = vec4b.yaw
                    player.position.mapX = vec4b.mapX.double
                    player.position.mapZ = vec4b.mapZ.double
                }
            }
            if (player.isPlayer || name == MC.thePlayer.name) {
                player.yaw = MC.thePlayer.rotationYaw
                player.position.mapX =
                    ((MC.thePlayer.posX - DungeonScan.START_X + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first)
                        .double
                player.position.mapZ =
                    ((MC.thePlayer.posZ - DungeonScan.START_Z + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second)
                        .double
            }
        }
    }

    fun updateRooms() {
        if (Dungeon.Info.ended) return
        val map = DungeonMap(MapUtils.mapData?.colors ?: return)
        Dungeon.espDoors.clear()

        for (x in 0..10) {
            for (z in 0..10) {
                val room = Dungeon.Info.dungeonList[z * 11 + x]
                val mapTile = map.getTile(x, z)

                if (room is Unknown) {
                    MapRenderList.renderUpdated = true
                    roomAdded = true
                    Dungeon.Info.dungeonList[z * 11 + x] = mapTile
                    continue
                }

                if (mapTile.state.ordinal < room.state.ordinal) {
                    MapRenderList.renderUpdated = true
                    PlayerTracker.roomStateChange(room, room.state, mapTile.state)
                    if (room is Room && room.data.type == RoomType.BLOOD && mapTile.state == RoomState.GREEN) {
                        RunInformation.bloodDone = true
                    }
                    room.state = mapTile.state
                }

                if (mapTile is Room && room is Room) {
                    if (room.data.type != mapTile.data.type && mapTile.data.type != RoomType.NORMAL) {
                        MapRenderList.renderUpdated = true
                        room.data.type = mapTile.data.type
                    }
                }

                if (mapTile is Door && room is Door) {
                    if (mapTile.type == DoorType.WITHER && room.type != DoorType.WITHER) {
                        MapRenderList.renderUpdated = true
                        room.type = mapTile.type
                    }
                }

                if (room is Door && room.type.equalsOneOf(DoorType.ENTRANCE, DoorType.WITHER, DoorType.BLOOD)) {
                    if (mapTile is Door && mapTile.type == DoorType.WITHER) {
                        if (room.opened) {
                            MapRenderList.renderUpdated = true
                            room.opened = false
                        }
                    } else if (!room.opened &&
                        MC.theWorld
                            .getChunkFromChunkCoords(
                                room.x shr 4,
                                room.z shr 4,
                            ).isLoaded &&
                        MC.theWorld.getBlockState(BlockPos(room.x, 69, room.z)).block == Blocks.air
                    ) {
                        MapRenderList.renderUpdated = true
                        room.opened = true
                    }

                    if (!room.opened) {
                        Dungeon.espDoors.add(room)
                    }
                }
            }
        }

        if (roomAdded) {
            updateUniques()
        }
    }

    fun updateUniques() {
        val visited = BooleanArray(121)
        for (x in 0..10) {
            for (z in 0..10) {
                val index = z * 11 + x
                if (visited[index]) continue
                visited[index] = true

                val room = Dungeon.Info.dungeonList[index]
                if (room !is Room) continue

                val connected = getConnectedIndices(x, z)
                var unique = room.uniqueRoom
                if (unique == null || unique.name.startsWith("Unknown")) {
                    unique = connected
                        .firstOrNull {
                            (Dungeon.Info.dungeonList[it.second * 11 + it.first] as? Room)?.uniqueRoom?.name?.startsWith("Unknown") == false
                        }?.let {
                            (Dungeon.Info.dungeonList[it.second * 11 + it.first] as? Room)?.uniqueRoom
                        } ?: unique
                }

                val finalUnique = unique ?: UniqueRoom(x, z, room)

                finalUnique.addTiles(connected)

                connected.forEach {
                    visited[it.second * 11 + it.first] = true
                }
            }
        }
        roomAdded = false
    }

    private fun getConnectedIndices(
        arrayX: Int,
        arrayY: Int,
    ): List<Pair<Int, Int>> {
        val tile = Dungeon.Info.dungeonList[arrayY * 11 + arrayX]
        if (tile !is Room) return emptyList()
        val directions =
            listOf(
                Pair(0, 1),
                Pair(1, 0),
                Pair(0, -1),
                Pair(-1, 0),
            )
        val connected = mutableListOf<Pair<Int, Int>>()
        val queue = mutableListOf(Pair(arrayX, arrayY))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (connected.contains(current)) continue
            connected.add(current)
            directions.forEach {
                val x = current.first + it.first
                val y = current.second + it.second
                if (x !in 0..10 || y !in 0..10) return@forEach
                if (Dungeon.Info.dungeonList[y * 11 + x] is Room) {
                    queue.add(Pair(x, y))
                }
            }
        }
        return connected
    }
}
