package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.world.WorldSettings
import net.skymoe.enchaddons.util.MC
import java.util.UUID

object TabList {
    private val tabListOrder =
        Ordering.from<NetworkPlayerInfo> { o1, o2 ->
            if (o1 == null) return@from -1
            if (o2 == null) return@from 0
            return@from ComparisonChain
                .start()
                .compareTrueFirst(
                    o1.gameType != WorldSettings.GameType.SPECTATOR,
                    o2.gameType != WorldSettings.GameType.SPECTATOR,
                ).compare(
                    o1.playerTeam?.registeredName ?: "",
                    o2.playerTeam?.registeredName ?: "",
                ).compare(o1.gameProfile.name, o2.gameProfile.name)
                .result()
        }

    fun getTabList(): List<Pair<NetworkPlayerInfo, String>> =
        MC.thePlayer
            ?.sendQueue
            ?.playerInfoMap
            ?.let { tabListOrder.immutableSortedCopy(it) }
            ?.map { Pair(it, MC.ingameGUI.tabList.getPlayerName(it)) } ?: emptyList()

    fun getDungeonTabList(): List<Pair<NetworkPlayerInfo, String>>? =
        getTabList().let {
            if (it.size > 18 && it[0].second.contains("§r§b§lParty §r§f(")) it else null
        }

    fun getPlayerUUIDByName(name: String): UUID? =
        getTabList()
            .firstOrNull {
                it.first.gameProfile.name == name
            }?.first
            ?.gameProfile
            ?.id
}
