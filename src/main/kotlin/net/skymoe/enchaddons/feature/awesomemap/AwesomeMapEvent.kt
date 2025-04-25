package net.skymoe.enchaddons.feature.awesomemap

import net.skymoe.enchaddons.event.Event

sealed interface AwesomeMapEvent : Event {
    val timeElapsed: Int

    sealed interface Score : AwesomeMapEvent {
        data class Reach270(
            override val timeElapsed: Int,
        ) : Score

        data class Reach300(
            override val timeElapsed: Int,
        ) : Score
    }

    data class MimicKilled(
        override val timeElapsed: Int,
    ) : AwesomeMapEvent
}
