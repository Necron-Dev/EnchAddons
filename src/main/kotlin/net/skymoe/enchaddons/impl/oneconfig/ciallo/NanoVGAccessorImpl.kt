package net.skymoe.enchaddons.impl.oneconfig.ciallo

import cc.polyfrost.oneconfig.renderer.font.Font
import net.skymoe.enchaddons.impl.MOD_ID
import net.skymoe.enchaddons.impl.oneconfig.NanoVGAccessor
import net.skymoe.enchaddons.impl.oneconfig.NanoVGImageCacheEntry
import net.skymoe.enchaddons.impl.oneconfig.nvg
import net.skymoe.enchaddons.util.StyledSegment
import net.skymoe.enchaddons.util.convertARGBToDoubleArray
import net.skymoe.enchaddons.util.convertDoubleArrayToARGB
import net.skymoe.enchaddons.util.math.Vec2D
import net.skymoe.enchaddons.util.math.double
import net.skymoe.enchaddons.util.math.float
import net.skymoe.enchaddons.util.math.int
import net.skymoe.enchaddons.util.scope.withscope
import net.skymoe.enchaddons.util.toBuffer
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.NVG_IMAGE_NODELETE
import org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle
import java.nio.ByteBuffer
import java.util.*
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.min

private fun NVGColor.fill(argb: Int): NVGColor {
    val (rv, gv, bv, av) = convertARGBToDoubleArray(argb)
    r(rv.float)
    g(gv.float)
    b(bv.float)
    a(av.float)
    return this
}

object NanoVGAccessorImpl : NanoVGAccessor {
    init {
        nvg = this
    }

    override fun loadFont(
        vg: Long,
        name: String,
    ): Font {
        val byteArray = javaClass.getResource("/assets/$MOD_ID/font/$name")!!.readBytes()
        val byteBuffer =
            ByteBuffer.allocateDirect(byteArray.size).apply {
                put(byteArray)
                flip()
            }
        val fontFace = UUID.randomUUID().toString()
        nvgCreateFontMem(vg, fontFace, byteBuffer, 0)
        return object : Font(fontFace, "") {
            val byteArray = byteBuffer
        }
    }

    override fun setFallbackFont(
        vg: Long,
        base: Font,
        fallback: Font,
    ) {
        nvgAddFallbackFont(vg, base.name, fallback.name)
    }

    override fun deleteImages(
        vg: Long,
        images: Set<Int>,
    ) {
        images.forEach {
            nvgDeleteImage(vg, it)
        }
    }

    override fun drawCheckMark(
        vg: Long,
        x: Double,
        y: Double,
        size: Double,
        lineWidth: Double,
        color: Int,
    ) {
        withscope {
            val nvgColor = NVGColor.calloc().using().fill(color)
            nvgBeginPath(vg)
            nvgStrokeColor(vg, nvgColor)
            nvgStrokeWidth(vg, lineWidth.float)

            nvgLineCap(vg, NVG_ROUND)
            nvgLineJoin(vg, NVG_ROUND)

            nvgMoveTo(vg, (x + .17 * size).float, (y + .6325 * size).float)
            nvgLineTo(vg, (x + .475 * size).float, (y + .8125 * size).float)
            nvgLineTo(vg, (x + .85 * size).float, (y + .225 * size).float)

            nvgStroke(vg)
            nvgClosePath(vg)
        }
    }

    override fun drawCrossMark(
        vg: Long,
        x: Double,
        y: Double,
        size: Double,
        lineWidth: Double,
        color: Int,
    ) {
        withscope {
            val nvgColor = NVGColor.calloc().using().fill(color)
            nvgBeginPath(vg)
            nvgStrokeColor(vg, nvgColor)
            nvgStrokeWidth(vg, lineWidth.float)

            nvgLineCap(vg, NVG_ROUND)
            nvgLineJoin(vg, NVG_ROUND)

            nvgMoveTo(vg, (x + .2 * size).float, (y + .2 * size).float)
            nvgLineTo(vg, (x + .8 * size).float, (y + .8 * size).float)
            nvgMoveTo(vg, (x + .2 * size).float, (y + .8 * size).float)
            nvgLineTo(vg, (x + .8 * size).float, (y + .2 * size).float)

            nvgStroke(vg)
            nvgClosePath(vg)
        }
    }

    override fun drawRingRectRounded(
        vg: Long,
        x: Double,
        y: Double,
        radius: Double,
        width: Double,
        height: Double,
        progress: Double,
        lineWidth: Double,
        color: Int,
    ) {
        if (progress < 0.0) return

        withscope {
            val nvgColor = NVGColor.calloc().using().fill(color)

            val totalLength = 2 * (width + height) - (8 - 2 * PI) * radius
            val widthWithoutR = (width - 2 * radius) / totalLength
            val heightWithoutR = (height - 2 * radius) / totalLength
            val r = (PI * radius) / (2 * totalLength)

            val progressList =
                listOf(
                    0.0,
                    widthWithoutR * 0.5,
                    widthWithoutR * 0.5 + r,
                    widthWithoutR * 0.5 + heightWithoutR + r,
                    widthWithoutR * 0.5 + heightWithoutR + r * 2.0,
                    widthWithoutR * 1.5 + heightWithoutR + r * 2.0,
                    widthWithoutR * 1.5 + heightWithoutR + r * 3.0,
                    widthWithoutR * 1.5 + heightWithoutR * 2.0 + r * 3.0,
                    widthWithoutR * 1.5 + heightWithoutR * 2.0 + r * 4.0,
                    widthWithoutR * 2.0 + heightWithoutR * 2.0 + r * 4.0,
                    Double.POSITIVE_INFINITY,
                )

            val (segmentIndex, segmentStart) = progressList.withIndex().first { (i, v) -> v <= progress && progress < progressList[i + 1] }

            val progressInSegment = (progress - segmentStart) / (progressList[segmentIndex + 1] - segmentStart)

            nvgBeginPath(vg)
            nvgStrokeColor(vg, nvgColor)
            nvgStrokeWidth(vg, lineWidth.float)
            nvgLineCap(vg, NVG_ROUND)
            nvgLineJoin(vg, NVG_ROUND)

            // Calculate the coordinates for the starting point (top-middle)
            val startX = x + width / 2.0
            val startY = y

            nvgMoveTo(vg, startX.float, startY.float)

            // Helper function to draw segments
            fun drawSegment(
                segment: Int,
                segmentProgress: Double,
            ) {
                when (segment) {
                    0 -> { // Top segment
                        val segmentLength = width - 2 * radius
                        val currentX = x + width / 2.0 + (segmentLength / 2) * segmentProgress
                        nvgLineTo(vg, currentX.float, y.float)
                    }
                    1 -> { // Top-Right Corner
                        val angle = (segmentProgress - 1.0) * PI / 2
                        nvgArc(
                            vg,
                            (x + width - radius).float,
                            (y + radius).float,
                            radius.float,
                            (-PI / 2).float,
                            angle.float,
                            NVG_CW,
                        )
                    }
                    2 -> { // Right segment
                        val segmentLength = height - 2 * radius
                        val currentY = y + radius + segmentLength * segmentProgress
                        nvgLineTo(vg, (x + width).float, currentY.float)
                    }
                    3 -> { // Bottom-Right Corner
                        val angle = segmentProgress * PI / 2
                        nvgArc(
                            vg,
                            (x + width - radius).float,
                            (y + height - radius).float,
                            radius.float,
                            0f,
                            angle.float,
                            NVG_CW,
                        )
                    }
                    4 -> { // Bottom segment
                        val segmentLength = width - 2 * radius
                        val currentX = x + width - radius - segmentLength * segmentProgress
                        nvgLineTo(vg, currentX.float, (y + height).float)
                    }
                    5 -> { // Bottom-Left Corner
                        val angle = (segmentProgress + 1.0) * PI / 2
                        nvgArc(
                            vg,
                            (x + radius).float,
                            (y + height - radius).float,
                            radius.float,
                            (PI / 2).float,
                            angle.float,
                            NVG_CW,
                        )
                    }
                    6 -> { // Left segment
                        val segmentLength = height - 2 * radius
                        val currentY = y + height - radius - segmentLength * segmentProgress
                        nvgLineTo(vg, x.float, currentY.float)
                    }
                    7 -> { // Top-Left Corner
                        val angle = (segmentProgress + 2.0) * PI / 2
                        nvgArc(
                            vg,
                            (x + radius).float,
                            (y + radius).float,
                            radius.float,
                            PI.float,
                            angle.float,
                            NVG_CW,
                        )
                    }
                    8 -> { // Top segment (partial, completing the circle)
                        val segmentLength = width / 2.0 - radius // Half of the width to the center
                        val currentX = x + radius + segmentLength * segmentProgress
                        nvgLineTo(vg, currentX.float, y.float)
                    }
                }
            }

            // Draw all the complete segments up to the current one
            for (i in 0 until segmentIndex) {
                drawSegment(i, 1.0)
            }

            // Draw the last segment, the incomplete one
            drawSegment(segmentIndex, progressInSegment)

            nvgStroke(vg)
            nvgClosePath(vg)
        }
    }

    override fun scissor(
        vg: Long,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
    ) {
        withscope {
            nvgScissor(vg, x.float, y.float, width.float, height.float)
        }
    }

    override fun resetScissor(vg: Long) {
        withscope {
            nvgResetScissor(vg)
        }
    }

    override fun drawRingArc(
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
    ) {
        withscope {
            val nvgColor = NVGColor.calloc().using().fill(color)

            val lpo = asin(arcPaddingFrom / outerRadius)
            val lpi = asin(arcPaddingFrom / innerRadius)
            val rpo = asin(arcPaddingTo / outerRadius)
            val rpi = asin(arcPaddingTo / innerRadius)

            nvgBeginPath(vg)
            nvgArc(
                vg,
                x.float,
                y.float,
                outerRadius.float,
                (fromRadian + lpo).float,
                (toRadian - rpo).float,
                NVG_CW,
            )
            nvgArc(
                vg,
                x.float,
                y.float,
                innerRadius.float,
                (toRadian - rpi).float,
                (fromRadian + lpi).float,
                NVG_CCW,
            )
            nvgClosePath(vg)
            nvgFillColor(vg, nvgColor)
            nvgFill(vg)
        }
    }

    override fun drawRoundedImage(
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
    ) {
        withscope {
            val nvgPaint = NVGPaint.calloc().using()
            val imageW = width / imageWRel
            val imageH = height / imageHRel
            val imageX = x - imageW * imageXRel
            val imageY = y - imageH * imageYRel
            nvgImagePattern(
                vg,
                imageX.float,
                imageY.float,
                imageW.float,
                imageH.float,
                0.0F,
                image,
                alpha.float,
                nvgPaint,
            )
            nvgBeginPath(vg)
            nvgRoundedRect(
                vg,
                x.float,
                y.float,
                width.float,
                height.float,
                radius.float,
            )
            nvgFillPaint(vg, nvgPaint)
            nvgFill(vg)
        }
    }

    override fun loadImageFromByteArray(
        vg: Long,
        image: ByteArray,
    ): Int = nvgCreateImageMem(vg, NVG_IMAGE_NEAREST, image.toBuffer())

    override fun drawRoundedPlayerAvatar(
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
    ) {
        imageCache.cleanup(this, vg)

        val nvgImage =
            imageCache.cache.getOrPut(texture) {
                nvglCreateImageFromHandle(vg, texture, 64, 64, NVG_IMAGE_NEAREST or NVG_IMAGE_NODELETE)
                    .also { if (it == -1) return@drawRoundedPlayerAvatar }
            }

        drawRoundedImage(
            vg,
            nvgImage,
            0.125,
            0.125,
            0.125,
            0.125,
            x,
            y,
            width,
            height,
            alpha,
            radius,
        )

        if (hat) {
            val offset = if (scaleHat) 1.0 / 144.0 else 0.0

            drawRoundedImage(
                vg,
                nvgImage,
                0.625 + offset,
                0.125 + offset,
                0.125 - 2.0 * offset,
                0.125 - 2.0 * offset,
                x,
                y,
                width,
                height,
                alpha,
                radius,
            )
        }
    }

    override fun drawAccarc(
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
    ) {
        nvgSave(vg)
        nvgTranslate(vg, x.float, y.float)

        if (lCorner != 0) {
            when (lCorner) {
                1 -> {
                    nvgTranslate(vg, width.float / 2, height.float / 2)
                    nvgRotate(vg, PI.float / 2)
                    nvgTranslate(vg, -width.float / 2, -height.float / 2)
                    drawAccarc(
                        vg,
                        0,
                        0.0,
                        0.0,
                        height,
                        width,
                        lHeight,
                        lWidth,
                        borderRadius,
                        lRadius,
                        color,
                    )
                }
                2 -> {
                    nvgTranslate(vg, width.float / 2, height.float / 2)
                    nvgRotate(vg, PI.float)
                    nvgTranslate(vg, -width.float / 2, -height.float / 2)
                    drawAccarc(
                        vg,
                        0,
                        0.0,
                        0.0,
                        width,
                        height,
                        lWidth,
                        lHeight,
                        borderRadius,
                        lRadius,
                        color,
                    )
                }
                3 -> {
                    nvgTranslate(vg, width.float / 2, height.float / 2)
                    nvgRotate(vg, -PI.float / 2)
                    nvgTranslate(vg, -width.float / 2, -height.float / 2)
                    drawAccarc(
                        vg,
                        0,
                        0.0,
                        0.0,
                        height,
                        width,
                        lHeight,
                        lWidth,
                        borderRadius,
                        lRadius,
                        color,
                    )
                }
            }
        } else {
            val w = width.float
            val h = height.float
            val lw = min(lWidth.float, w)
            val lh = min(lHeight.float, h)
            val br = minOf(borderRadius.float, lw / 2, lh / 2, w - lw, h - lh)
            val lr = minOf(lRadius.float, w - lw, h - lh)

            withscope {
                val nvgColor = NVGColor.calloc().using().fill(color)

                nvgBeginPath(vg)

                nvgMoveTo(vg, br, 0F)
                nvgLineTo(vg, w - br, 0F)
                nvgArcTo(vg, w, 0F, w, br, br)
                nvgLineTo(vg, w, lh - br)
                nvgArcTo(vg, w, lh, w - br, lh, br)
                nvgLineTo(vg, lw + lr, lh)
                nvgArcTo(vg, lw, lh, lw, lh + lr, lr)
                nvgLineTo(vg, lw, h - br)
                nvgArcTo(vg, lw, h, lw - br, h, br)
                nvgLineTo(vg, br, h)
                nvgArcTo(vg, 0F, h, 0F, h - br, br)
                nvgLineTo(vg, 0F, br)
                nvgArcTo(vg, 0F, 0F, br, 0F, br)

                nvgClosePath(vg)
                nvgFillColor(vg, nvgColor)
                nvgFill(vg)
            }
        }

        nvgRestore(vg)
    }

    override fun drawTextSegments(
        vg: Long,
        segments: List<StyledSegment>,
        x: Double,
        y: Double,
        size: Double,
        font: Font,
        anchor: Vec2D,
        color: Int,
        colorMultiplier: Double,
        shadow: Pair<Vec2D, Double>?,
    ) {
        withscope {
            shadow?.let {
                drawTextSegments(
                    vg,
                    segments,
                    x + shadow.first.x * size,
                    y + shadow.first.y * size,
                    size,
                    font,
                    anchor,
                    color,
                    colorMultiplier * shadow.second,
                    null,
                )
            }
            val alphaBits = (color and 0xFF000000.int) or 0xFFFFFF
            val nvgColor = NVGColor.calloc().using().fill(shadowColor(color, colorMultiplier))
            nvgFontSize(vg, size.float)
            nvgFontFace(vg, font.name)
            nvgTextAlign(vg, NVG_ALIGN_TOP or NVG_ALIGN_LEFT)
            val totalWidth = segments.sumOf { nvgTextBounds(vg, 0.0f, 0.0f, it.text, FloatArray(4)).double }
            val posX = x - totalWidth * anchor.x
            val posY = y - size * anchor.y
            var currentX = posX
            segments.forEach { (text, color) ->
                nvgFillColor(
                    vg,
                    color?.let {
                        val colorWithAlpha = color and alphaBits
                        NVGColor.calloc().using().fill(shadowColor(colorWithAlpha, colorMultiplier))
                    } ?: nvgColor,
                )
                currentX = nvgText(vg, currentX.float, posY.float, text).double
            }
        }
    }

    override fun drawRoundedTexture(
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
    ) {
        imageCache.cleanup(this, vg)

        val nvgImage =
            imageCache.cache.getOrPut(texture) {
                nvglCreateImageFromHandle(vg, texture, 64, 64, NVG_IMAGE_NEAREST or NVG_IMAGE_NODELETE)
                    .also { if (it == -1) return@drawRoundedTexture }
            }

        drawRoundedImage(
            vg,
            nvgImage,
            0.0,
            0.0,
            1.0,
            1.0,
            x,
            y,
            width,
            height,
            alpha,
            radius,
        )
    }

    private fun shadowColor(
        color: Int,
        multiplier: Double,
    ): Int {
        val rgba = convertARGBToDoubleArray(color)
        rgba[0] = rgba[0] * multiplier
        rgba[1] = rgba[1] * multiplier
        rgba[2] = rgba[2] * multiplier
        return convertDoubleArrayToARGB(*rgba)
    }

    override fun save(vg: Long) {
        nvgSave(vg)
    }

    override fun restore(vg: Long) {
        nvgRestore(vg)
    }

    override fun translate(
        vg: Long,
        pos: Vec2D,
    ) {
        nvgTranslate(vg, pos.x.float, pos.y.float)
    }

    override fun scale(
        vg: Long,
        factor: Vec2D,
    ) {
        nvgScale(vg, factor.x.float, factor.y.float)
    }

    override fun rotate(
        vg: Long,
        angle: Double,
    ) {
        nvgRotate(vg, angle.float)
    }
}
