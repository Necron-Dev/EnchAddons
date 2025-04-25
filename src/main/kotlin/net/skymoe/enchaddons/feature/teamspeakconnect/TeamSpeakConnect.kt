package net.skymoe.enchaddons.feature.teamspeakconnect

import kotlinx.atomicfu.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import net.skymoe.enchaddons.event.RegistryEventDispatcher
import net.skymoe.enchaddons.event.minecraft.MinecraftEvent
import net.skymoe.enchaddons.event.register
import net.skymoe.enchaddons.feature.FeatureBase
import net.skymoe.enchaddons.feature.featureInfo
import net.skymoe.enchaddons.feature.teamspeakconnect.wrapper.*
import net.skymoe.enchaddons.impl.MOD_ID
import net.skymoe.enchaddons.impl.MOD_NAME
import net.skymoe.enchaddons.impl.MOD_VERSION
import net.skymoe.enchaddons.util.property.versionedLazy
import net.skymoe.enchaddons.util.tickCounter

val TEAMSPEAK_CONNECT_INFO = featureInfo<TeamSpeakConnectConfig>("teamspeak_connect", "TeamSpeak Connect")

object TeamSpeakConnect : FeatureBase<TeamSpeakConnectConfig>(TEAMSPEAK_CONNECT_INFO) {
    private var tsRemote: TeamSpeakRemoteAppWrapper? = null
    private var job: CompletableJob = SupervisorJob()
    private val moduleScope: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO + job)

    private val updateInstanceState by versionedLazy({ tickCounter / 20 }) {
        if (config.enabled) {
            if (tsRemote == null) connectTeamSpeak()
        } else {
            if (tsRemote != null) disconnectTeamSpeak()
        }
    }

    private var connectionState: ConnectStatusInfo? = null
    private var nowChannel: String? = null
    private var serverProperties: ServerProperties? = null

    private var channelProperties: MutableMap<String, ChannelProperties> = mutableMapOf()
    private var clientProperties: MutableMap<Int, ClientProperties> = mutableMapOf()
    private var channelClientList: MutableMap<String, MutableList<Int>> = mutableMapOf()

    enum class TeamSpeakEventType {
        JOINED_CHANNEL,
        LEFT_CHANNEL,
    }

    private val teamSpeakEventAtomicInt: AtomicInt = atomic(0)

    data class TeamSpeakEvent(
        val type: TeamSpeakEventType,
        val name: String,
        var aliveTicks: Int,
        val id: Int = teamSpeakEventAtomicInt.getAndIncrement(),
    )

    val teamSpeakEvent: MutableList<TeamSpeakEvent> = mutableListOf()

    override fun registerEvents(dispatcher: RegistryEventDispatcher) {
        dispatcher.run {
            register<MinecraftEvent.Tick.Pre> {
                updateInstanceState

                teamSpeakEvent.removeIf { event ->
                    event.aliveTicks--
                    event.aliveTicks <= 0
                }
            }
        }
    }

    @Synchronized
    private fun connectTeamSpeak() {
        job = SupervisorJob()

        if (tsRemote != null) {
            logger.warn("Already connected. Ignoring...")
            return
        }

        tsRemote =
            TeamSpeakRemoteAppWrapper(
                websocketUrl = config.address,
                identifier = MOD_ID,
                version = MOD_VERSION,
                name = MOD_NAME,
                description =
                    "A Hypixel Skyblock QoL mod.",
                apiKey = config.apiKey,
                coroutineScope = moduleScope,
            )

        tsRemote?.apply {
            onConnect = {
                resetState()
            }
            onDisconnect = {
                resetState()
            }
            onAuthSuccess = {
                config.apiKey = it.apiKey
                config.save()
            }
            onConnectStatusChanged = { payload ->
                when (payload.status) {
                    1 -> connectionState = null
                    2 -> connectionState = payload.info
                    3 -> connectionState?.clientId = payload.info?.clientId
                }
            }
            onServerPropertiesUpdated = {
                serverProperties = it.properties
            }
            onError = {
                it.printStackTrace()
                disconnectTeamSpeak()
            }
            onChannels = { payload ->
                payload.info.rootChannels.forEach {
                    channelProperties[it.id] = it.properties
                }

                payload.info.subChannels.forEach { parent ->
                    parent.value.forEach {
                        channelProperties[it.id] = it.properties
                    }
                }
            }
            onChannelEdited = { payload ->
                channelProperties[payload.channelId] = payload.properties
            }
            onChannelCreated = { payload ->
                channelProperties[payload.channelId] = payload.properties
            }
            onClientMoved = { payload ->
                channelClientList[payload.oldChannelId]?.remove(payload.clientId)
                channelClientList[payload.newChannelId]?.add(payload.clientId)
                if (connectionState?.clientId == payload.clientId) nowChannel = payload.newChannelId
                payload.properties?.let { clientProperties[payload.clientId] = it }

                if (payload.newChannelId == nowChannel) {
                    teamSpeakEvent.add(
                        TeamSpeakEvent(
                            type = TeamSpeakEventType.JOINED_CHANNEL,
                            name = (clientProperties[payload.clientId]?.nickname ?: ""),
                            aliveTicks = config.dynamicSpot.displayTime,
                        ),
                    )
                } else if (payload.oldChannelId == nowChannel) {
                    teamSpeakEvent.add(
                        TeamSpeakEvent(
                            type = TeamSpeakEventType.LEFT_CHANNEL,
                            name = (clientProperties[payload.clientId]?.nickname ?: ""),
                            aliveTicks = config.dynamicSpot.displayTime,
                        ),
                    )
                }
            }
            onClientPropertiesUpdated = { payload ->
                clientProperties[payload.clientId] = payload.properties
            }
            onTalkStatusChanged = { payload ->
                nowChannel = channelClientList.entries.firstOrNull { it.value.contains(payload.clientId) }?.key
            }
            connect()
        }
    }

    @Synchronized
    private fun resetState() {
        serverProperties = null
        nowChannel = null
        channelProperties.clear()
        clientProperties.clear()
        channelClientList.clear()
    }

    @Synchronized
    private fun disconnectTeamSpeak() {
        tsRemote?.disconnect()
        tsRemote = null
        job.cancel()
    }
}
