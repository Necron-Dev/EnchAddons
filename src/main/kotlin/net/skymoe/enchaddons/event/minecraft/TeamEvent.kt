package net.skymoe.enchaddons.event.minecraft

import net.minecraft.network.play.server.S3EPacketTeams
import net.skymoe.enchaddons.event.Event

sealed interface TeamEvent : Event {
    sealed interface Pre : TeamEvent {
        data class Create(
            val packet: S3EPacketTeams,
        ) : Pre

        data class Remove(
            val packet: S3EPacketTeams,
        ) : Pre

        data class Update(
            val packet: S3EPacketTeams,
        ) : Pre

        data class AddPlayer(
            val packet: S3EPacketTeams,
        ) : Pre

        data class RemovePlayer(
            val packet: S3EPacketTeams,
        ) : Pre
    }
}
