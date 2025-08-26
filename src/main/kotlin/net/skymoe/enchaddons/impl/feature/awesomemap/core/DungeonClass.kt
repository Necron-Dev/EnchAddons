package net.skymoe.enchaddons.impl.feature.awesomemap.core

enum class DungeonClass(
    val code: Char,
) {
    TANK('T'),
    ARCHER('A'),
    BERSERK('B'),
    MAGE('M'),
    HEALER('H'),
    UNKNOWN('?');

    companion object {
        fun fromCode(code: Char): DungeonClass = entries.find { it.code == code } ?: UNKNOWN
        fun fromDisplayName(name: String): DungeonClass =
            when (name) {
                "Tank" -> TANK
                "Archer" -> ARCHER
                "Berserk" -> BERSERK
                "Mage" -> MAGE
                "Healer" -> HEALER
                else -> UNKNOWN
            }
    }
}

