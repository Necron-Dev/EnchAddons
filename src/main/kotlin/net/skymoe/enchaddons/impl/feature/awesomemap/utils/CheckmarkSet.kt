package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.util.ResourceLocation
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomState
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.MapRender
import net.skymoe.enchaddons.util.MC

class CheckmarkSet(
    val size: Int,
    location: String,
) {
    private val crossResource = ResourceLocation("funnymap", "$location/cross.png")
    private val greenResource = ResourceLocation("funnymap", "$location/green_check.png")
    private val questionResource = ResourceLocation("funnymap", "$location/question.png")
    private val whiteResource = ResourceLocation("funnymap", "$location/white_check.png")

    init {
        listOf(crossResource, greenResource, questionResource, whiteResource).forEach {
            MC.textureManager.loadTexture(it, SimpleTexture(it))
        }
    }

    fun getCheckmark(state: RoomState): ResourceLocation? =
        when (state) {
            RoomState.CLEARED -> whiteResource
            RoomState.GREEN -> greenResource
            RoomState.FAILED -> crossResource
            RoomState.UNOPENED -> if (MapRender.legitRender) questionResource else null
            else -> null
        }
}
