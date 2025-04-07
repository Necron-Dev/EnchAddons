package net.skymoe.enchaddons.impl.mixincallback

import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minecraft.network.play.server.S34PacketMaps
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraft.network.play.server.S3EPacketTeams
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.event.hypixel.SkyblockEvent
import net.skymoe.enchaddons.event.minecraft.ChatEvent
import net.skymoe.enchaddons.event.minecraft.MapEvent
import net.skymoe.enchaddons.event.minecraft.TabListEvent
import net.skymoe.enchaddons.event.minecraft.TeamEvent
import net.skymoe.enchaddons.util.math.int
import net.skymoe.enchaddons.util.trimStyle

object NetHandlerPlayClientCallback {
    fun onS02PacketChatPre(packet: S02PacketChat) {
        when (packet.type.int) {
            2 -> {}
            else -> {
                ChatEvent.Normal
                    .Pre(
                        packet.chatComponent.formattedText.trimStyle,
                        packet.chatComponent.formattedText,
                        packet.chatComponent,
                    ).also(EA.eventDispatcher)
            }
        }
    }

    fun onS32PacketConfirmTransactionPre(packet: S32PacketConfirmTransaction) {
        SkyblockEvent.ServerTick
            .also(EA.eventDispatcher)
    }

    fun onS38PacketPlayerListItemPre(packet: S38PacketPlayerListItem) {
        TabListEvent
            .Pre(packet)
            .also(EA.eventDispatcher)
    }

    fun onS3EPacketTeamsPre(packet: S3EPacketTeams) {
        when (packet.action) {
            0 -> TeamEvent.Pre.Create(packet)
            1 -> TeamEvent.Pre.Remove(packet)
            2 -> TeamEvent.Pre.Update(packet)
            3 -> TeamEvent.Pre.AddPlayer(packet)
            4 -> TeamEvent.Pre.RemovePlayer(packet)
            else -> null
        }?.also(EA.eventDispatcher)
    }

    fun onS34PacketMapsPre(packet: S34PacketMaps) {
        MapEvent
            .Pre(packet)
            .also(EA.eventDispatcher)
    }
}
