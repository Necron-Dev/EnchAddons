package net.skymoe.enchaddons.feature.invincibilitytimer

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.setDefault
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.hypixel.SkyblockEvent
import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.config.invoke
import net.skymoe.enchaddons.feature.featureInfo

val INVINCIBILITY_TIMER_INFO = featureInfo<InvincibilityTimerConfig>("invincibility_timer", "Invincibility Timer")

object InvincibilityTimer : FeatureBase<InvincibilityTimerConfig>(INVINCIBILITY_TIMER_INFO) {
    data class InvincibilityItem(
        val name: String,
        val invincibilityTicks: () -> Int,
        val coolDown: Int,
        val regex: Regex,
        val image: (Long) -> Int,
    )

    data class InvincibilityItemState(
        val itemInfo: InvincibilityItem,
        var invincibilityTicks: Int = itemInfo.invincibilityTicks(),
        var coolDown: Int = itemInfo.coolDown,
        var updateTime: Long = System.nanoTime(),
    )

    val itemStateList: MutableList<InvincibilityItemState> = mutableListOf()

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.World.Unload.Pre> {
                itemStateList.clear()
            }

            register<MinecraftEvent.Tick.Pre> {
                itemStateList.removeIf {
                    (it.coolDown-- <= 0).also { ended ->
                        if (ended) {
                            InvincibilityTimerEvent
                                .CooldownEnd(it.itemInfo)
                                .also(EA.eventDispatcher)
                        }
                    }
                }
            }

            register<SkyblockEvent.ServerTick> {
                itemStateList.forEach {
                    if (it.invincibilityTicks > 0) it.invincibilityTicks--
                    it.updateTime = System.nanoTime()
                }
            }

            register<ChatEvent.Normal.Pre> { event ->
                INVINCIBILITY_ITEMS.firstOrNull {
                    if (event.message.matches(it.regex)) {
                        InvincibilityTimerEvent
                            .Procced(it)
                            .also(EA.eventDispatcher)
                        true
                    } else {
                        false
                    }
                }
            }

            register<InvincibilityTimerEvent.Procced> { event ->
                itemStateList.add(InvincibilityItemState(event.item))
                config.notification.onItemProcced(logger) {
                    setDefault()
                    this["itemInfo"] = event.item
                }
            }

            register<InvincibilityTimerEvent.CooldownEnd> { event ->
                config.notification.onCooldownEnded(logger) {
                    setDefault()
                    this["itemInfo"] = event.item
                }
            }
        }
    }
}
