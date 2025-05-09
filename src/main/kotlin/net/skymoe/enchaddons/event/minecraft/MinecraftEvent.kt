package net.skymoe.enchaddons.event.minecraft

import net.minecraft.client.multiplayer.WorldClient
import net.skymoe.enchaddons.event.Event

sealed interface MinecraftEvent : Event {
    sealed interface Load : MinecraftEvent {
        data object Post : Load
    }

    sealed interface Loop : MinecraftEvent {
        data object Pre : Loop
    }

    sealed interface Tick : MinecraftEvent {
        data object Pre : Tick
    }

    sealed interface World : MinecraftEvent {
        sealed interface Unload : World {
            data class Pre(
                val world: WorldClient,
            ) : Unload
        }
    }
}
