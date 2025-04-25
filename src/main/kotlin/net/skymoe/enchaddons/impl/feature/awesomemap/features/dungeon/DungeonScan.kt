package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.*
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.DungeonScan.scan
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location.dungeonFloor
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.equalsOneOf
import net.skymoe.enchaddons.util.LogLevel
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.buildComponent
import net.skymoe.enchaddons.util.modMessage
import kotlin.math.ceil

/**
 * Handles everything related to scanning the dungeon. Running [scan] will update the instance of [Dungeon].
 */
object DungeonScan {
    /**
     * The size of each dungeon room in blocks.
     */
    const val ROOM_SIZE = 32

    /**
     * The starting coordinates to start scanning (the north-west corner).
     */
    const val START_X = -185
    const val START_Z = -185

    private var lastScanTime = 0L
    var isScanning = false
    var hasScanned = false

    val shouldScan: Boolean
        get() =
            AwesomeMap.config.autoScan &&
                !isScanning &&
                !hasScanned &&
                System.currentTimeMillis() - lastScanTime >= 250 &&
                dungeonFloor != -1

    fun scan() {
        isScanning = true
        var allChunksLoaded = true

        // Scans the dungeon in a 11x11 grid.
        for (x in 0..10) {
            for (z in 0..10) {
                // Translates the grid index into world position.
                val xPos = START_X + x * (ROOM_SIZE shr 1)
                val zPos = START_Z + z * (ROOM_SIZE shr 1)

                if (!MC.theWorld.getChunkFromChunkCoords(xPos shr 4, zPos shr 4).isLoaded) {
                    // The room being scanned has not been loaded in.
                    allChunksLoaded = false
                    continue
                }

                // This room has already been added in a previous scan.
                if (Dungeon.Info.dungeonList[x + z * 11].run {
                        this !is Unknown && (this as? Room)?.data?.name != "Unknown"
                    }
                ) {
                    continue
                }

                scanRoom(xPos, zPos, z, x)?.let {
                    val prev = Dungeon.Info.dungeonList[z * 11 + x]
                    if (it is Room) {
                        if ((prev as? Room)?.uniqueRoom != null) {
                            prev.uniqueRoom?.addTile(x, z, it)
                        } else if (Dungeon.Info.uniqueRooms.none { unique -> unique.name == it.data.name }) {
                            UniqueRoom(x, z, it)
                        }
                        MapUpdate.roomAdded = true
                    }
                    Dungeon.Info.dungeonList[z * 11 + x] = it
                    MapRenderList.renderUpdated = true
                }
            }
        }

        if (MapUpdate.roomAdded) {
            MapUpdate.updateUniques()
        }

        if (allChunksLoaded) {
            if (AwesomeMap.config.scanChatInfo) {
                val maxSecrets = ceil(Dungeon.Info.secretCount * ScoreCalculation.getSecretPercent())
                var maxBonus = 5
                if (dungeonFloor.equalsOneOf(6, 7)) maxBonus += 2
                if (ScoreCalculation.paul) maxBonus += 10
                val minSecrets = ceil(maxSecrets * (40 - maxBonus) / 40).toInt()

                buildComponent {
                    "Scan Finished!\n".green
                    "Puzzles (".green
                    "${Dungeon.Info.puzzles.size}"
                        .red
                    "):".green
                    Dungeon.Info.puzzles.entries.forEach { puzzle ->
                        "\n- ".aqua
                        puzzle.key.roomDataName.lightPurple
                    }
                    "\nTrap: ".gold
                    Dungeon.Info.trapType.green
                    "\nWither Doors: ".darkGray
                    "${Dungeon.Info.witherDoors - 1}".gray
                    "\nTotal Crypts: ".gray
                    "${Dungeon.Info.cryptCount}".gold
                    "\nTotal Secrets: ".gray
                    "${Dungeon.Info.secretCount}".aqua
                    "\nMinimum Secrets: ".gray
                    "$minSecrets".yellow
                }.also {
                    modMessage(it, LogLevel.INFO)
                }
            }
            Dungeon.Info.roomCount =
                Dungeon.Info.dungeonList
                    .filter { it is Room && !it.isSeparator }
                    .size
            hasScanned = true
        }

        lastScanTime = System.currentTimeMillis()
        isScanning = false
    }

    private fun scanRoom(
        x: Int,
        z: Int,
        row: Int,
        column: Int,
    ): Tile? {
        val height = MC.theWorld.getChunkFromChunkCoords(x shr 4, z shr 4).getHeightValue(x and 15, z and 15)
        if (height == 0) return null

        val rowEven = row and 1 == 0
        val columnEven = column and 1 == 0

        return when {
            // Scanning a room
            rowEven && columnEven -> {
                val roomCore = ScanUtils.getCore(x, z)
                Room(x, z, ScanUtils.getRoomData(roomCore) ?: return null).apply {
                    core = roomCore
                }
            }

            // Can only be the center "block" of a 2x2 room.
            !rowEven && !columnEven -> {
                Dungeon.Info.dungeonList[column - 1 + (row - 1) * 11].let {
                    if (it is Room) {
                        Room(x, z, it.data).apply {
                            isSeparator = true
                        }
                    } else {
                        null
                    }
                }
            }

            // Doorway between rooms
            // Old trap has a single block at 82
            height.equalsOneOf(74, 82) -> {
                Door(
                    x,
                    z,
                    // Finds door type from door block
                    type =
                        when (MC.theWorld.getBlockState(BlockPos(x, 69, z)).block) {
                            Blocks.coal_block -> {
                                Dungeon.Info.witherDoors++
                                DoorType.WITHER
                            }

                            Blocks.monster_egg -> DoorType.ENTRANCE
                            Blocks.stained_hardened_clay -> DoorType.BLOOD
                            else -> DoorType.NORMAL
                        },
                )
            }

            // Connection between large rooms
            else -> {
                Dungeon.Info.dungeonList[if (rowEven) row * 11 + column - 1 else (row - 1) * 11 + column].let {
                    if (it !is Room) {
                        null
                    } else if (it.data.type == RoomType.ENTRANCE) {
                        Door(x, z, DoorType.ENTRANCE)
                    } else {
                        Room(x, z, it.data).apply { isSeparator = true }
                    }
                }
            }
        }
    }
}
