package net.skymoe.enchaddons.impl.config.gui

import cc.polyfrost.oneconfig.config.annotations.Color
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.nanovg.widget.backgroundWidget
import net.skymoe.enchaddons.util.Colors
import net.skymoe.enchaddons.util.alphaScale
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double

class GUIBackground {
    @Switch(
        name = "Show Background",
        size = 2,
    )
    var enabledOption = true

    @Color(
        name = "Background Color",
        size = 2,
    )
    var backgroundColorOption = OneColor(Colors.GRAY[9].rgb alphaScale 0.8)

    @Number(
        name = "Rounded Corner Radius",
        min = 0.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var radiusOption = 6.0F

    @Number(
        name = "Shadow Blur",
        min = 0.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var shadowBlur = 2.0F

    @Number(
        name = "X Padding",
        min = 0.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var paddingXOption = 6.0F

    @Number(
        name = "Y Padding",
        min = 0.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var paddingYOption = 6.0F

    fun addTo(
        widgets: MutableList<Widget<*>>,
        tr: Transformation,
        size: Vec2D,
    ) {
        if (!enabledOption) return
        widgets.run {
            add(
                backgroundWidget(
                    tr pos Vec2D(0.0, 0.0),
                    tr size size,
                    tr size Vec2D(paddingXOption, paddingYOption),
                    backgroundColorOption.rgb,
                    tr size radiusOption.double,
                    tr size shadowBlur.double,
                ),
            )
        }
    }
}
