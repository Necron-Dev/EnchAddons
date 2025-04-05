package net.skymoe.enchaddons.impl.config.subcategory

import cc.polyfrost.oneconfig.config.annotations.SubConfig
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.impl.config.ConfigCategory
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.feature.AwesomeMapConfigImpl
import net.skymoe.enchaddons.impl.config.feature.FastDraftConfigImpl
import kotlin.reflect.KClass

class DungeonConfig :
    ConfigImpl(featureInfo<FeatureConfig>("dungeon", "Dungeon")),
    ConfigCategory {
    @Transient
    override val configImplMap = mutableMapOf<KClass<*>, () -> FeatureConfig>()

    @SubConfig
    var fastDraft = FastDraftConfigImpl()

    @SubConfig
    var awesomeMapConfig = AwesomeMapConfigImpl()

    init {
        initializeMembers()
    }
}
