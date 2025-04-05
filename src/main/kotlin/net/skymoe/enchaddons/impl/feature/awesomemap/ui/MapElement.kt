package net.skymoe.enchaddons.impl.feature.awesomemap.ui

import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRenderList
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location

class MapElement : MovableGuiElement() {
    override var x: Int by AwesomeMap.config::mapX
    override var y: Int by AwesomeMap.config::mapY
    override val h: Int
        get() = if (AwesomeMap.config.mapShowRunInformation) 142 else 128
    override val w: Int
        get() = 128
    override var scale: Float by AwesomeMap.config::mapScale
    override var x2: Int = (x + w * scale).toInt()
    override var y2: Int = (y + h * scale).toInt()

    override fun render() {
        if (AwesomeMap.config.renderBeta) {
            MapRenderList.renderMap()
        } else {
            MapRender.renderMap()
        }
    }

    override fun shouldRender(): Boolean {
        if (!AwesomeMap.config.mapEnabled) return false
        if (AwesomeMap.config.mapHideInBoss && Location.inBoss) return false
        return super.shouldRender()
    }
}
