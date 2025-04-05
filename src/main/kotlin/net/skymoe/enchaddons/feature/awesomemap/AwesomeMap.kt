package net.skymoe.enchaddons.feature.awesomemap

import kotlinx.coroutines.CoroutineScope
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.*
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.ensureEnabled
import net.skymoe.enchaddons.feature.ensureSkyBlockMode
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.impl.feature.awesomemap.commands.FunnyMapCommands
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.Dungeon
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.RunInformation
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.WitherDoorESP
import net.skymoe.enchaddons.impl.feature.awesomemap.ui.GuiRenderer
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils
import net.skymoe.enchaddons.impl.nanovg.GUIEvent
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.scope.longrun
import org.lwjgl.input.Keyboard
import kotlin.coroutines.EmptyCoroutineContext

val AWESOME_MAP_INFO = featureInfo<AwesomeMapConfig>("awesome_map", "Awesome Map")

object AwesomeMap : FeatureBase<AwesomeMapConfig>(AWESOME_MAP_INFO) {
    var display: GuiScreen? = null
    private val toggleLegitKey = KeyBinding("Legit Peek", Keyboard.KEY_NONE, "Funny Map")
    val scope = CoroutineScope(EmptyCoroutineContext)

    private fun onInit() {
        ClientCommandHandler.instance.registerCommand((FunnyMapCommands()))
        listOf(
            this,
            Dungeon,
            GuiRenderer,
            Location,
            RunInformation,
            WitherDoorESP,
        ).forEach(MinecraftForge.EVENT_BUS::register)
        RenderUtils
        ClientRegistry.registerKeyBinding(toggleLegitKey)
    }

    fun onTick() {
        MC.mcProfiler.startSection("awesome_map")

        if (display != null) {
            MC.displayGuiScreen(display)
            display = null
        }

        if (config.peekMode == 1) {
            MapRender.legitPeek = toggleLegitKey.isKeyDown
        }

        Dungeon.onTick()
        GuiRenderer.onTick()
        Location.onTick()

        MC.mcProfiler.endSection()
    }

    fun onKey(event: InputEvent.KeyInputEvent) {
        if (config.peekMode == 0 && toggleLegitKey.isPressed) {
            MapRender.legitPeek = !MapRender.legitPeek
        }
    }

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.Load> {
                onInit()
            }

            register<MinecraftEvent.Tick.Pre> {
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    onTick()
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

            register<GUIEvent.HUD> { event ->
                longrun {
                    ensureEnabled()
                    ensureSkyBlockMode("dungeon")

                    GuiRenderer.onOverlay(event)
                }
            }

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
