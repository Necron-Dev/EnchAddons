package net.skymoe.enchaddons.impl.feature.awesomemap.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.util.ResourceLocation
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.Room
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.Dungeon
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.APIUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils
import net.skymoe.enchaddons.util.math.int
import net.skymoe.enchaddons.util.partialTicks
import java.util.UUID

data class DungeonPlayer(
    val skin: ResourceLocation,
    var uuid: UUID?,
) {
    var name = ""

    /** Minecraft formatting code for the player's name */
    var colorPrefix = 'f'

    /** The player's dungeon class (T, A, B, M, H) */
    var dungeonClass: Char = '?'

    /** The player's name with formatting code */
    val formattedName: String
        get() = "§$colorPrefix$name"

    data class PlayerPosition(
        var mapX: Double = .0,
        var mapZ: Double = .0,
    )

    val position: PlayerPosition = PlayerPosition()
    val prevPosition: PlayerPosition = PlayerPosition()

    var yaw = 0f
    var prevYaw = 0f

    fun getRenderYaw(): Double {
        val prevYaw = (prevYaw % 360 + 360) % 360
        val yaw = (yaw % 360 + 360) % 360
        var diff = yaw - prevYaw
        if (diff > 180) diff = 360 - diff
        if (diff < -180) diff = -360 - diff
        return prevYaw + diff * partialTicks
    }

    /** Has information from the player entity been loaded */
    var playerLoaded = false
    var icon = ""
    var renderHat = true
    var dead = false
    var isPlayer = false

    /** Stats for compiling player tracker information */
    var startingSecrets = 0
    var lastRoom = ""
    var lastTime = 0L
    var roomVisits: MutableList<Pair<Long, String>> = mutableListOf()

    /** Set player data that requires entity to be loaded */
    fun setData(player: EntityPlayer) {
        renderHat = player.isWearing(EnumPlayerModelParts.HAT)
        playerLoaded = true
        AwesomeMap.scope.launch(Dispatchers.IO) {
            val secrets = uuid?.let { APIUtils.getSecrets(it) } ?: 0
            Utils.runMinecraftThread {
                startingSecrets = secrets
            }
        }
    }

    /** Gets the player's room, used for room tracker */
    fun getCurrentRoom(): String {
        if (dead) return "Dead"
        if (Location.inBoss) return "Boss"
        val x = (position.mapX.int - MapUtils.startCorner.first) / (MapUtils.roomSize + MapUtils.connectorSize)
        val z = (position.mapZ.int - MapUtils.startCorner.second) / (MapUtils.roomSize + MapUtils.connectorSize)
        return (Dungeon.Info.dungeonList.getOrNull(x * 2 + z * 22) as? Room)?.data?.name ?: "Error"
    }
}
