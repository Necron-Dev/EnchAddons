package net.skymoe.enchaddons.event.minecraft

import net.minecraft.network.play.server.S34PacketMaps
import net.skymoe.enchaddons.event.Event

interface MapEvent : Event {
    data class Pre(
        val packet: S34PacketMaps,
    ) : MapEvent
}
