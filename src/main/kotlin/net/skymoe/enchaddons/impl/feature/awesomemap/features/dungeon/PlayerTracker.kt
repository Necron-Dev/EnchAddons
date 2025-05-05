package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatStyle
import net.minecraft.util.IChatComponent
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.DungeonPlayer
import net.skymoe.enchaddons.impl.feature.awesomemap.core.RoomData
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.Room
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomState
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomType
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.Tile
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.APIUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.equalsOneOf
import net.skymoe.enchaddons.util.LogLevel
import net.skymoe.enchaddons.util.buildComponent
import net.skymoe.enchaddons.util.modMessage
import kotlin.time.Duration.Companion.milliseconds

object PlayerTracker {
    val roomClears: MutableMap<RoomData, Set<String>> = mutableMapOf()

    fun roomStateChange(
        room: Tile,
        state: RoomState,
        newState: RoomState,
    ) {
        if (room !is Room) return
        if (newState.equalsOneOf(RoomState.CLEARED, RoomState.GREEN) && state != RoomState.CLEARED) {
            val currentRooms =
                Dungeon.dungeonTeammates.map { Pair(it.value.formattedName, it.value.getCurrentRoom()) }
            roomClears[room.data] =
                currentRooms.filter { it.first != "" && it.second == room.data.name }.map { it.first }.toSet()
        }
    }

    fun onDungeonEnd() {
        val time = System.currentTimeMillis() - Dungeon.Info.startTime
        Dungeon.dungeonTeammates.forEach {
            it.value.roomVisits.add(Pair(time - it.value.lastTime, it.value.lastRoom))
        }

        AwesomeMap.scope.launch {
            Dungeon.dungeonTeammates
                .map { (_, player) ->
                    async(Dispatchers.IO) {
                        Triple(
                            player.formattedName,
                            player,
                            player.uuid?.let { APIUtils.getSecrets(it) } ?: 0,
                        )
                    }
                }.map {
                    val (name, player, secrets) = it.await()
                    getStatMessage(name, player, secrets)
                }.forEach {
                    modMessage(it, LogLevel.INFO)
                }
        }
    }

    fun getStatMessage(
        name: String,
        player: DungeonPlayer,
        secrets: Int,
    ): IChatComponent {
        val allClearedRooms = roomClears.filter { it.value.contains(name) }
        val soloClearedRooms = allClearedRooms.filter { it.value.size == 1 }
        val max = allClearedRooms.size
        val min = soloClearedRooms.size

        val secretsComponent =
            buildComponent {
                "${secrets - player.startingSecrets} ".aqua
                "secrets".darkAqua
            }

        val roomComponent =
            buildComponent {
                "${if (soloClearedRooms.size != allClearedRooms.size) "$min-$max" else max} ".aqua
                "rooms cleared".darkAqua
            }.apply {
                chatStyle =
                    ChatStyle().setChatHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            buildComponent {
                                roomClears.entries
                                    .filter {
                                        it.key.type !in arrayOf(RoomType.BLOOD, RoomType.ENTRANCE, RoomType.FAIRY) &&
                                            it.value.contains(name)
                                    }.joinToString(
                                        separator = "\n",
                                        prefix = "$name's §eRooms Cleared:\n",
                                    ) { (room, players) ->
                                        if (players.size == 1) {
                                            "§6${room.name}"
                                        } else {
                                            "§6${room.name} §7with ${
                                                players.filter { it != name }.joinToString(separator = "§r, ")
                                            }"
                                        }
                                    }.append
                            },
                        ),
                    )
            }

        var lastTime = 0L

        val splitsComponent =
            buildComponent {
                "Splits".darkAqua
            }.apply {
                chatStyle =
                    ChatStyle().setChatHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            buildComponent {
                                player.roomVisits
                                    .joinToString(
                                        separator = "\n",
                                        prefix = "$name's §eRoom Splits:\n",
                                    ) { (elapsed, room) ->
                                        val start = lastTime.milliseconds
                                        lastTime += elapsed
                                        val end = lastTime.milliseconds
                                        "§b$start §7- §b$end §6$room"
                                    }.append
                            },
                        ),
                    )
            }

        val roomTimeComponent =
            buildComponent {
                "Times".darkAqua
            }.apply {
                chatStyle =
                    ChatStyle().setChatHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            buildComponent {
                                player.roomVisits
                                    .groupBy { it.second }
                                    .entries
                                    .joinToString(
                                        separator = "\n",
                                        prefix = "$name's §eRoom Times:\n",
                                    ) { (room, times) ->
                                        "§6$room §a- §b${times.sumOf { it.first }.milliseconds}"
                                    }.append
                            },
                        ),
                    )
            }

        return buildComponent {
            name.darkAqua
            " > ".white
            secretsComponent.append
            " | ".gold
            roomComponent.append
            " | ".gold
            splitsComponent.append
            " | ".gold
            roomTimeComponent.append
        }
    }
}
