package net.skymoe.enchaddons.impl.feature.awesomemap

import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.config.feature.AwesomeMapConfigImpl
import net.skymoe.enchaddons.impl.hud.FeatureHUDBase
import net.skymoe.enchaddons.impl.nanovg.Transformation
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.oneconfig.NanoVGImageCache
import net.skymoe.enchaddons.util.math.Vec2D

object AwesomeMapHUD : FeatureHUDBase<AwesomeMapConfigImpl, AwesomeMap>(AwesomeMap, { config.hud }) {
    override val width = 128.0
    override val height get() = if (config.mapShowRunInformation) 146.0 else 128.0

    private val cache = NanoVGImageCache()

    override fun ensureShow() {
    }

    override fun reset() {
        cache.clear()
    }

    override fun draw(
        widgets: MutableList<Widget<*>>,
        box: Vec2D,
        tr: Transformation,
    ) {
        config.background.addTo(AwesomeMapHUD.widgets, tr, size)
        widgets.add(AwesomeMapWidget(cache, tr pos (Vec2D(0.0, 0.0)), tr size 1.0))
    }
}
