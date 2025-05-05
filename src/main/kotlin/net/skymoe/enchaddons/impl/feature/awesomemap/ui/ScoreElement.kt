package net.skymoe.enchaddons.impl.feature.awesomemap.ui

import net.minecraft.client.gui.FontRenderer
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.RunInformation
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.ScoreCalculation
import net.skymoe.enchaddons.util.MC

class ScoreElement {
    companion object {
        val fr: FontRenderer = MC.fontRendererObj

        fun getScoreLines(): List<String> {
            val list: MutableList<String> = mutableListOf()

            when (AwesomeMap.config.scoreTotalScore) {
                1 -> list.add(getScore(AwesomeMap.config.scoreMinimizedName, false))
                2 -> list.add(getScore(AwesomeMap.config.scoreMinimizedName, true))
            }

            when (AwesomeMap.config.scoreSecrets) {
                1 -> list.add(getSecrets(AwesomeMap.config.scoreMinimizedName, false))
                2 -> list.add(getSecrets(AwesomeMap.config.scoreMinimizedName, true))
            }

            if (AwesomeMap.config.scoreCrypts) {
                list.add(getCrypts(AwesomeMap.config.scoreMinimizedName))
            }

            if (AwesomeMap.config.scoreMimic) {
                list.add(getMimic(AwesomeMap.config.scoreMinimizedName))
            }

            if (AwesomeMap.config.scoreDeaths) {
                list.add(getDeaths(AwesomeMap.config.scoreMinimizedName))
            }

            when (AwesomeMap.config.scorePuzzles) {
                1 -> list.add(getPuzzles(AwesomeMap.config.scoreMinimizedName, false))
                2 -> list.add(getPuzzles(AwesomeMap.config.scoreMinimizedName, true))
            }

            return list
        }

        fun runInformationLines(): List<String> {
            val list: MutableList<String> = mutableListOf()

            if (AwesomeMap.config.runInformationScore) {
                list.add(getScore(minimized = false, expanded = false))
            }

            when (AwesomeMap.config.runInformationSecrets) {
                1 -> list.add(getSecrets(minimized = false, missing = false))
                2 -> list.add(getSecrets(minimized = false, missing = true))
            }

            list.add("split")

            if (AwesomeMap.config.runInformationCrypts) {
                list.add(getCrypts())
            }

            if (AwesomeMap.config.runInformationMimic) {
                list.add(getMimic())
            }

            if (AwesomeMap.config.runInformationDeaths) {
                list.add(getDeaths())
            }

            return list
        }

        private fun getScore(
            minimized: Boolean = false,
            expanded: Boolean,
        ): String {
            val scoreColor =
                when {
                    ScoreCalculation.score < 270 -> "§c"
                    ScoreCalculation.score < 300 -> "§e"
                    else -> "§a"
                }
            var line =
                if (minimized) {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedScore}: "
                } else {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textScore}: "
                }
            if (expanded) {
                line += "§b${ScoreCalculation.getSkillScore()}§7/" +
                    "§a${ScoreCalculation.getExplorationScore()}§7/" +
                    "§3${ScoreCalculation.getSpeedScore(RunInformation.timeElapsed)}§7/" +
                    "§d${ScoreCalculation.getBonusScore()} §7: "
            }
            line += "$scoreColor${ScoreCalculation.score}"

            return line
        }

        private fun getSecrets(
            minimized: Boolean = false,
            missing: Boolean,
        ): String {
            var line =
                if (minimized) {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedSecrets}: "
                } else {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textSecrets}: "
                }
            line += "§b${RunInformation.secretsFound}§7/"
            if (missing) {
                val missingSecrets = (RunInformation.minSecrets - RunInformation.secretsFound).coerceAtLeast(0)
                line += "§e$missingSecrets§7/"
            }
            line += "§c${RunInformation.secretTotal}"

            return line
        }

        private fun getCrypts(minimized: Boolean = false): String {
            var line =
                if (minimized) {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedCrypts}: "
                } else {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textCrypts}: "
                }
            line += if (RunInformation.cryptsCount >= 5) "§a${RunInformation.cryptsCount}" else "§c${RunInformation.cryptsCount}"
            return line
        }

        private fun getMimic(minimized: Boolean = false): String {
            var line =
                if (minimized) {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedMimic}: "
                } else {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMimic}: "
                }
            line +=
                if (RunInformation.mimicKilled) {
                    if (minimized) {
                        "§a${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedMimicYes}"
                    } else {
                        "§a${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMimicYes}"
                    }
                } else {
                    if (minimized) {
                        "§c${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedMimicNo}"
                    } else {
                        "§c${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMimicNo}"
                    }
                }
            return line
        }

        private fun getDeaths(minimized: Boolean = false): String {
            var line =
                if (minimized) {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedDeaths}: "
                } else {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textDeaths}: "
                }
            line += "§c${RunInformation.deathCount}"
            return line
        }

        private fun getPuzzles(
            minimized: Boolean = false,
            total: Boolean,
        ): String {
            val color = if (RunInformation.completedPuzzles == RunInformation.totalPuzzles) "§a" else "§c"
            var line =
                if (minimized) {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textMinimizedPuzzles}: "
                } else {
                    "§7${EnchAddonsConfig.dungeonConfig.awesomeMapConfig.textPuzzles}: "
                }
            line += "$color${RunInformation.completedPuzzles}"
            if (total) line += "§7/$color${RunInformation.totalPuzzles}"
            return line
        }
    }
}
