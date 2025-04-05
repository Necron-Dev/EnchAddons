package net.skymoe.enchaddons.event.minecraft

import net.minecraft.util.IChatComponent
import net.skymoe.enchaddons.event.Event

sealed interface ChatEvent : Event {
    val message: String
    val messageRaw: String
    val component: IChatComponent

    sealed interface Normal : ChatEvent {
        data class Pre(
            override val message: String,
            override val messageRaw: String,
            override val component: IChatComponent,
        ) : Normal
    }
}
