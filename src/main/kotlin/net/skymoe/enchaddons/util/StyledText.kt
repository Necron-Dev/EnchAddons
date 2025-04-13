package net.skymoe.enchaddons.util

import net.skymoe.enchaddons.util.math.int
import java.lang.StringBuilder

data class StyledSegment(
    val text: String,
    val color: Int?,
)

fun String.toStyledSegments(): List<StyledSegment> =
    buildList {
        val stringBuilder = StringBuilder()
        var color: Int? = null

        fun setColor(newColor: Int?) {
            if (color != newColor && stringBuilder.isNotEmpty()) {
                add(StyledSegment(stringBuilder.toString(), color))
                stringBuilder.clear()
            }
            color = newColor
        }

        run {
            var i = 0
            while (i < length) {
                when (val c = this@toStyledSegments[i]) {
                    '\u00A7' -> {
                        ++i
                        if (i < length) {
                            val code = this@toStyledSegments[i]
                            ++i
                            when (code.lowercaseChar()) {
                                'r' -> setColor(null)
                                '0' -> setColor(0xFF000000.int)
                                '1' -> setColor(0xFF0000AA.int)
                                '2' -> setColor(0xFF00AA00.int)
                                '3' -> setColor(0xFF00AAAA.int)
                                '4' -> setColor(0xFFAA0000.int)
                                '5' -> setColor(0xFFAA00AA.int)
                                '6' -> setColor(0xFFFFAA00.int)
                                '7' -> setColor(0xFFAAAAAA.int)
                                '8' -> setColor(0xFF555555.int)
                                '9' -> setColor(0xFF5555FF.int)
                                'a' -> setColor(0xFF55FF55.int)
                                'b' -> setColor(0xFF55FFFF.int)
                                'c' -> setColor(0xFFFF5555.int)
                                'd' -> setColor(0xFFFF55FF.int)
                                'e' -> setColor(0xFFFFFF55.int)
                                'f' -> setColor(0xFFFFFFFF.int)
                            }
                        }
                    }
                    else -> {
                        ++i
                        stringBuilder.append(c)
                    }
                }
            }
        }

        add(StyledSegment(stringBuilder.toString(), color))
    }
