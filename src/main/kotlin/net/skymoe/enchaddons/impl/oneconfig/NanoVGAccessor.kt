package net.skymoe.enchaddons.impl.oneconfig

import cc.polyfrost.oneconfig.renderer.font.Font
import cc.polyfrost.oneconfig.renderer.font.Fonts
import net.skymoe.enchaddons.util.StyledSegment
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.int
import net.skymoe.enchaddons.util.property.latelet

var nvg: NanoVGAccessor by latelet()

private var fallbackFontLoaded = false
private var fontLoaded = false

var fontFallbackInstance: Font by latelet()
var fontMediumInstance: Font by latelet()
var fontSemiBoldInstance: Font by latelet()

val fontMedium = { fontMediumInstance }
const val FONT_MEDIUM_Y_OFFSET = 0.5
val fontSemiBold = { fontSemiBoldInstance }
const val FONT_SEMIBOLD_Y_OFFSET = 0.1

fun NanoVGAccessor.fuckOneConfigFont(vg: Long) {
    if (!fallbackFontLoaded) {
        fontFallbackInstance = loadFont(vg, "notosanssc/medium.ttf")
        fallbackFontLoaded = true
    }

    listOf<Font>(
        Fonts.BOLD,
        Fonts.SEMIBOLD,
        Fonts.REGULAR,
        Fonts.MEDIUM,
        Fonts.MINECRAFT_BOLD,
        Fonts.MINECRAFT_REGULAR,
    ).forEach { font ->
        setFallbackFont(vg, font, fontFallbackInstance)
    }
}

fun NanoVGAccessor.loadFonts(vg: Long) {
    if (!fallbackFontLoaded) {
        fontFallbackInstance = loadFont(vg, "notosanssc/medium.ttf")
        fallbackFontLoaded = true
    }

    if (!fontLoaded) {
        fontMediumInstance = loadFont(vg, "montserrat/medium.ttf")
        setFallbackFont(vg, fontMediumInstance, fontFallbackInstance)
        fontSemiBoldInstance = loadFont(vg, "montserrat/semibold.ttf")
        setFallbackFont(vg, fontSemiBoldInstance, fontFallbackInstance)
        fontLoaded = true
    }
}

class NanoVGImageCache {
    private val cache = mutableMapOf<Any?, MutableMap<Int, Int>>()
    private val imagesToDelete = mutableSetOf<Int>()

    operator fun get(key: Any?) = NanoVGImageCacheEntry(this, cache.getOrPut(key, ::mutableMapOf))

    fun cleanup(
        accessor: NanoVGAccessor,
        vg: Long,
    ) {
        accessor.deleteImages(vg, imagesToDelete)
        imagesToDelete.clear()
    }

    fun clear() {
        cache.values.forEach { entry ->
            entry.values.forEach(imagesToDelete::add)
        }
        cache.clear()
    }

    fun clear(key: Any?) {
        cache[key]?.values?.forEach(imagesToDelete::add)
        cache.remove(key)
    }
}

class NanoVGImageCacheEntry(
    val parent: NanoVGImageCache,
    val cache: MutableMap<Int, Int>,
) {
    fun cleanup(
        accessor: NanoVGAccessor,
        vg: Long,
    ) {
        parent.cleanup(accessor, vg)
    }
}

interface NanoVGAccessor {
    fun loadFont(
        vg: Long,
        name: String,
    ): Font

    fun setFallbackFont(
        vg: Long,
        base: Font,
        fallback: Font,
    )

    fun loadImageFromByteArray(
        vg: Long,
        image: ByteArray,
    ): Int

    fun deleteImages(
        vg: Long,
        images: Set<Int>,
    )

    fun scissor(
        vg: Long,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
    )

    fun resetScissor(vg: Long)

    fun drawRoundedImage(
        vg: Long,
        image: Int,
        imageXRel: Double,
        imageYRel: Double,
        imageWRel: Double,
        imageHRel: Double,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    )

    fun drawCheckMark(
        vg: Long,
        x: Double,
        y: Double,
        size: Double,
        lineWidth: Double,
        color: Int,
    )

    fun drawCrossMark(
        vg: Long,
        x: Double,
        y: Double,
        size: Double,
        lineWidth: Double,
        color: Int,
    )

    fun drawRingRectRounded(
        vg: Long,
        x: Double,
        y: Double,
        radius: Double,
        width: Double,
        height: Double,
        progress: Double,
        lineWidth: Double,
        color: Int,
    )

    fun drawRingArc(
        vg: Long,
        x: Double,
        y: Double,
        outerRadius: Double,
        innerRadius: Double,
        fromRadian: Double,
        toRadian: Double,
        arcPaddingFrom: Double,
        arcPaddingTo: Double,
        color: Int,
    )

    fun drawRoundedPlayerAvatar(
        vg: Long,
        imageCache: NanoVGImageCacheEntry,
        texture: Int,
        hat: Boolean,
        scaleHat: Boolean,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    )

    fun drawAccarc(
        vg: Long,
        lCorner: Int,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        lWidth: Double,
        lHeight: Double,
        borderRadius: Double,
        lRadius: Double,
        color: Int,
    )

    fun drawTextSegments(
        vg: Long,
        segments: List<StyledSegment>,
        x: Double,
        y: Double,
        size: Double,
        font: Font,
        anchor: Vec2D = Vec2D(0.0, 0.0),
        color: Int = 0xFFFFFFFF.int,
        colorMultiplier: Double = 1.0,
        shadow: Pair<Vec2D, Double>? = null,
    )

    fun drawRoundedTexture(
        vg: Long,
        imageCache: NanoVGImageCacheEntry,
        texture: Int,
        imageXRel: Double,
        imageYRel: Double,
        imageWRel: Double,
        imageHRel: Double,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    )

    fun save(vg: Long)

    fun restore(vg: Long)

    fun translate(
        vg: Long,
        pos: Vec2D,
    )

    fun scale(
        vg: Long,
        factor: Vec2D,
    )

    fun rotate(
        vg: Long,
        angle: Double,
    )
}
