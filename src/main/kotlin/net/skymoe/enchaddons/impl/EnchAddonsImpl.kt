package net.skymoe.enchaddons.impl

import net.skymoe.enchaddons.EnchAddons
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.dungeon.fastdraft.FastDraft
import net.skymoe.enchaddons.feature.dynamickeybind.DynamicKeyBinding
import net.skymoe.enchaddons.feature.dynamicspot.DynamicSpot
import net.skymoe.enchaddons.feature.invincibilitytimer.InvincibilityTimer
import net.skymoe.enchaddons.feature.teamspeakconnect.TeamSpeakConnect
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.impl.api.APIImpl
import net.skymoe.enchaddons.impl.cache.ResourceCacheImpl
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.event.EventDispatcherImpl
import net.skymoe.enchaddons.impl.feature.awesomemap.AwesomeMapHUD
import net.skymoe.enchaddons.impl.feature.dynamickeybinding.DynamicKeyBindingHUD
import net.skymoe.enchaddons.impl.feature.dynamicspot.DynamicSpotHUD
import net.skymoe.enchaddons.impl.feature.invincibilitytimer.InvincibilityTimerHUD
import net.skymoe.enchaddons.impl.feature.teamspeakconnect.TeamSpeakConnectHUD
import net.skymoe.enchaddons.impl.hypixel.loadHypixelModAPI
import net.skymoe.enchaddons.impl.nanovg.GUIEvent
import net.skymoe.enchaddons.impl.nanovg.NanoVGUIContext
import net.skymoe.enchaddons.impl.nanovg.Widget
import net.skymoe.enchaddons.impl.oneconfig.fuckOneConfigFont
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.theEA
import net.skymoe.enchaddons.util.math.Vec2D
import kotlin.reflect.KClass

lateinit var theEAImpl: EnchAddonsImpl

val EAImpl by ::theEAImpl

val LOGGER = getLogger("Main")

val initEnchAddonsImpl by lazy {
    LOGGER.info("Initializing EnchAddonsImpl...")
    EnchAddonsImpl()
    LOGGER.info("Initialized EnchAddonsImpl")
}

const val MOD_ID: String = "@ID@"
const val MOD_NAME: String = "@NAME@"
const val MOD_VERSION: String = "@VER@"

class EnchAddonsImpl : EnchAddons {
    override val modID = MOD_ID
    override val modName: String = MOD_NAME
    override val modVersion: String = MOD_VERSION
    override val workingDirectory: String = "."

    override val api = APIImpl()
    override val eventDispatcher = EventDispatcherImpl()

    override var configVersion = 0

    var fuckedOneConfig = false

    override fun <T : FeatureConfig> getConfigImpl(type: KClass<T>) = EnchAddonsConfig.getConfigImpl(type)

    init {
        theEA = this
        theEAImpl = this

        EnchAddonsConfig
        ResourceCacheImpl

        DynamicSpot
        DynamicSpotHUD

        DynamicKeyBinding
        DynamicKeyBindingHUD

        InvincibilityTimer
        InvincibilityTimerHUD

        TeamSpeakConnect
        TeamSpeakConnectHUD

        FastDraft

        AwesomeMap
        AwesomeMapHUD

        eventDispatcher.register<MinecraftEvent.Load.Post> {
            loadHypixelModAPI
        }

        eventDispatcher.register<GUIEvent.HUD> { event ->
            if (!fuckedOneConfig) {
                object : Widget<Nothing> {
                    override fun draw(context: NanoVGUIContext) {
                        context.run {
                            nvg.fuckOneConfigFont(vg)
                        }
                    }

                    override fun alphaScale(alpha: Double): Nothing = throw IllegalStateException()

                    override fun scale(
                        scale: Double,
                        origin: Vec2D,
                    ): Nothing = throw IllegalStateException()
                }.also(event.widgets::add)

                fuckedOneConfig = true
            }
        }
    }
}
