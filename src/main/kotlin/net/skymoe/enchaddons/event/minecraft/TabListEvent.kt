package net.skymoe.enchaddons.event.minecraft

import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.skymoe.enchaddons.event.Event

sealed interface TabListEvent : Event {
    val packet: S38PacketPlayerListItem

    data class Pre(
        override val packet: S38PacketPlayerListItem,
    ) : TabListEvent
}
