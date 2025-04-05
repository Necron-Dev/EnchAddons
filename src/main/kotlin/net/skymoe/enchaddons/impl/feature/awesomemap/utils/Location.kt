package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.util.MC

object Location {
    private var onHypixel = false
    var inSkyblock = false
    var island = Island.Unknown
    val inDungeons
        get() = island == Island.Dungeon
    var dungeonFloor = -1
    var masterMode = false
    var inBoss = false

    private var islandRegex = Regex("^§r§b§l(?:Area|Dungeon): §r§7(.+)§r\$")

    private val entryMessages =
        listOf(
            "[BOSS] Bonzo: Gratz for making it this far, but I'm basically unbeatable.",
            "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
            "[BOSS] The Professor: I was burdened with terrible news recently...",
            "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
            "[BOSS] Livid: Welcome, you've arrived right on time. I am Livid, the Master of Shadows.",
            "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!",
        )

    private var tickCount = 0

    fun onTick() {
        if (MC.theWorld == null) return
        tickCount++
        if (tickCount % 20 != 0) return
        if (AwesomeMap.config.forceSkyblock) {
            inSkyblock = true
            island = Island.Dungeon
            dungeonFloor = 7
            return
        }

        inSkyblock = onHypixel &&
            MC.theWorld.scoreboard
                ?.getObjectiveInDisplaySlot(1)
                ?.name == "SBScoreboard"

        if (island == Island.Unknown) {
            TabList
                .getTabList()
                .firstNotNullOfOrNull { islandRegex.find(it.second) }
                ?.groupValues
                ?.getOrNull(1)
                ?.let { areaName ->
                    Island.entries.find { it.displayName == areaName }?.let { island = it }
                }
        }

        if (island == Island.Dungeon && dungeonFloor == -1) {
            Scoreboard
                .getLines()
                .find {
                    Scoreboard.cleanLine(it).run {
                        contains("The Catacombs (") && !contains("Queue")
                    }
                }?.let {
                    val line = it.substringBefore(")")
                    dungeonFloor = line.lastOrNull()?.digitToIntOrNull() ?: 0
                    masterMode = line[line.length - 2] == 'M'
                }
        }
    }

    fun onChat(event: ChatEvent) {
        if (event.message.startsWith("[BOSS] Maxor: ")) inBoss = true
        if (entryMessages.any { it == event.message }) inBoss = true
    }

    fun onWorldUnload(event: MinecraftEvent.World.Unload) {
        island = Island.Unknown
        dungeonFloor = -1
        inBoss = false
    }

    enum class Island(
        val displayName: String,
    ) {
        PrivateIsland("Private Island"),
        Garden("Garden"),
        SpiderDen("Spider's Den"),
        CrimsonIsle("Crimson Isle"),
        TheEnd("The End"),
        GoldMine("Gold Mine"),
        DeepCaverns("Deep Caverns"),
        DwarvenMines("Dwarven Mines"),
        GlaciteMineshaft("Mineshaft"),
        CrystalHollows("Crystal Hollows"),
        FarmingIsland("The Farming Islands"),
        ThePark("The Park"),
        Dungeon("Catacombs"),
        DungeonHub("Dungeon Hub"),
        Hub("Hub"),
        DarkAuction("Dark Auction"),
        JerryWorkshop("Jerry's Workshop"),
        Kuudra("Kuudra"),
        Unknown("(Unknown)"),
    }
}
