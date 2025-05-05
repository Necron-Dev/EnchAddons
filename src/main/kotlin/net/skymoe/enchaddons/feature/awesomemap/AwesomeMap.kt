package net.skymoe.enchaddons.feature.awesomemap

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.skymoe.enchaddons.api.setDefault
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.*
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.config.invoke
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.ensureSkyBlockMode
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.*
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils
import net.skymoe.enchaddons.util.scope.longrun
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val AWESOME_MAP_INFO = featureInfo<AwesomeMapConfig>("awesome_map", "Awesome Map")

object AwesomeMap : FeatureBase<AwesomeMapConfig>(AWESOME_MAP_INFO) {
    val scope = CoroutineScope(EmptyCoroutineContext)
    private var runningTick = atomic(false)

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.Load> {
                Dungeon
//                GuiRenderer
                Location
                DungeonScan
                RenderUtils
//                MapRender
//                MapRenderList
                MapUpdate
                MimicDetector
                PlayerTracker
                RunInformation
                ScanUtils
                ScoreCalculation
                WitherDoorESP
            }

            register<MinecraftEvent.Tick.Pre> {
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    scope.launch {
                        if (runningTick.getAndSet(true)) return@launch
                        try {
                            Dungeon.onTick()
//                        GuiRenderer.onTick()
                            Location.onTick()
                        } finally {
                            runningTick.value = false
                        }
                    }
                }
            }

            register<ChatEvent.Normal> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    Dungeon.onChat(event)
                    Location.onChat(event)
                    RunInformation.onChat(event)
                }
            }

//            register<GUIEvent.HUD.Post> { event ->
//                longrun {
//                    ensureEnabled()
//                    ensureSkyBlockMode("dungeon")
//
//                    glStateScope {
//                        NanoVGHelper.INSTANCE.setupAndDraw { vg ->
//                            GuiRenderer.onOverlay(vg)
//                        }
//                    }
//                }
//            }

            register<MinecraftEvent.World.Unload> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    Location.onWorldUnload(event)
                    Dungeon.onWorldLoad(event)
                }
            }

            register<TabListEvent.Pre> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    RunInformation.onTabList(event)
                }
            }

            register<LivingEntityEvent.Death> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    RunInformation.onEntityDeath(event)
                }
            }

            register<TeamEvent.Pre.Update> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    RunInformation.onScoreboard(event)
                }
            }

            register<MapEvent.Pre> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    MapUtils.onUpdateMapData(event)
                }
            }

            register<RenderEvent.World.Last> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    WitherDoorESP.onRender(event)
                }
            }

            register<AwesomeMapEvent.Score> { event ->
                when (event) {
                    is AwesomeMapEvent.Score.Reach270 ->
                        config.notification.onScore270(logger) {
                            setDefault()
                            this["time"] = event.timeElapsed.toDuration(DurationUnit.SECONDS)
                        }
                    is AwesomeMapEvent.Score.Reach300 ->
                        config.notification.onScore300(logger) {
                            setDefault()
                            this["time"] = event.timeElapsed.toDuration(DurationUnit.SECONDS)
                        }
                }
            }

            register<AwesomeMapEvent.MimicKilled> { event ->
                config.notification.onMimicKilled(logger) {
                    setDefault()
                    this["time"] = event.timeElapsed.toDuration(DurationUnit.SECONDS)
                }
            }
        }
    }
}
