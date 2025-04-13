package net.skymoe.enchaddons.feature.awesomemap

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.*
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.ensureSkyBlockMode
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.*
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.MapUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.scope.longrun
import org.lwjgl.input.Keyboard
import kotlin.coroutines.EmptyCoroutineContext

val AWESOME_MAP_INFO = featureInfo<AwesomeMapConfig>("awesome_map", "Awesome Map")

object AwesomeMap : FeatureBase<AwesomeMapConfig>(AWESOME_MAP_INFO) {
    private var display: GuiScreen? = null
    private val toggleLegitKey = KeyBinding("Legit Peek", Keyboard.KEY_NONE, "Funny Map")
    val scope = CoroutineScope(EmptyCoroutineContext)
    private var runningTick = atomic(false)

    fun onKey(event: InputEvent.KeyInputEvent) {
        if (config.peekMode == 0 && toggleLegitKey.isPressed) {
//            MapRender.legitPeek = !MapRender.legitPeek
        }
    }

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
                ClientRegistry.registerKeyBinding(toggleLegitKey)
            }

            register<MinecraftEvent.Tick.Pre> {
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    if (display != null) {
                        MC.displayGuiScreen(display)
                        display = null
                    }

                    if (config.peekMode == 1) {
//                        MapRender.legitPeek = toggleLegitKey.isKeyDown
                    }

                    scope.launch {
                        if (runningTick.getAndSet(true)) return@launch
                        Dungeon.onTick()
//                        GuiRenderer.onTick()
                        Location.onTick()
                        runningTick.value = false
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
        }
    }
}
