package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import net.minecraft.scoreboard.ScorePlayerTeam
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.removeFormatting
import net.skymoe.enchaddons.util.MC

object Scoreboard {
    fun cleanLine(scoreboard: String): String = scoreboard.removeFormatting().filter { it.code in 32..126 }

    fun getLines(): List<String> {
        return MC.theWorld?.scoreboard?.run {
            getSortedScores(getObjectiveInDisplaySlot(1) ?: return emptyList())
                .filter { it?.playerName?.startsWith("#") == false }
                .let { if (it.size > 15) it.drop(15) else it }
                .map { ScorePlayerTeam.formatPlayerName(getPlayersTeam(it.playerName), it.playerName) }
        } ?: emptyList()
    }
}
