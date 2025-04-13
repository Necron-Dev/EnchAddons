package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap

object MapRender {
    var dynamicRotation = 0f
    var legitPeek = false
        set(value) {
            if (field != value && AwesomeMap.config.legitMode) {
                MapRenderList.renderUpdated = true
            }
            field = value
        }

    val legitRender: Boolean
        get() = AwesomeMap.config.legitMode && !legitPeek
}
