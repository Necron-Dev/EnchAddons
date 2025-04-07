package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.settings.GameSettings
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.util.tickCounter

fun startGamePost() {
    MinecraftEvent.Load.Post
        .also(EA.eventDispatcher)
}

fun onRunTickPre() {
    tickCounter++
    MinecraftEvent.Tick.Pre
        .also(EA.eventDispatcher)
}

fun onWorldUnloadPre(world: WorldClient) {
    MinecraftEvent.World.Unload
        .Pre(world)
        .also(EA.eventDispatcher)
}

fun onHideGUIKeyDetect(gameSettings: GameSettings) {
    if (EnchAddonsConfig.main.disableF1) {
        gameSettings.hideGUI = false
    }
}
