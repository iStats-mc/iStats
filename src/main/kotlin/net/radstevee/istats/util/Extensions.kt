package net.radstevee.istats.util

import kotlinx.datetime.Instant
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.time.Duration
import kotlin.math.floor

fun Screen.getPosition(
    xPercentage: Int,
    yPercentage: Int,
    width: Int,
    height: Int,
): Pair<Int, Int> {
    val screenWidth = this.width
    val screenHeight = this.height

    val x = floor((screenWidth / 2 - width / 2) * ((xPercentage / 100.0) * 2.0)).toInt()
    val y = floor((screenHeight / 2 - height / 2) * ((yPercentage / 100.0) * 2.0)).toInt()

    return x to y
}

fun DrawContext.getPosition(
    xPercentage: Int,
    yPercentage: Int,
    width: Int,
    height: Int,
): Pair<Int, Int> {
    val screenWidth = scaledWindowWidth
    val screenHeight = scaledWindowHeight

    val x = floor((screenWidth / 2 - width / 2) * ((xPercentage / 100.0) * 2.0)).toInt()
    val y = floor((screenHeight / 2 - height / 2) * ((yPercentage / 100.0) * 2.0)).toInt()

    return x to y
}

fun DrawContext.getPosition(
    renderer: TextRenderer,
    text: Text,
    xPercentage: Int,
    yPercentage: Int,
): Pair<Int, Int> {
    val textWidth = renderer.getWidth(text)
    val textHeight = renderer.fontHeight
    val screenWidth = scaledWindowWidth
    val screenHeight = scaledWindowHeight

    val x = floor((screenWidth / 2 - textWidth / 2) * ((xPercentage / 100.0) * 2.0)).toInt()
    val y = floor((screenHeight / 2 - textHeight / 2) * ((yPercentage / 100.0) * 2.0)).toInt()

    return x to y
}

fun DrawContext.drawTextRelative(
    renderer: TextRenderer,
    text: Text,
    xPercentage: Int,
    yPercentage: Int,
    color: Int,
    shadow: Boolean,
) {
    val (x, y) = getPosition(renderer, text, xPercentage, yPercentage)
    drawText(renderer, text, x, y, color, shadow)
}

fun Instant.relative(): String {
    val now = java.time.Instant.now()
    val instant = java.time.Instant.ofEpochSecond(this.epochSeconds)
    val duration = Duration.between(instant, now)

    return when {
        duration.toDays() > 0 -> {
            val isPlural = duration.toDays() > 1
            "${duration.toDays()} day${if (isPlural) "s" else ""} ago"
        }
        duration.toHours() > 0 -> {
            val isPlural = duration.toHours() > 1
            "${duration.toHours()} hour${if (isPlural) "s" else ""} ago"
        }
        duration.toMinutes() > 0 -> {
            val isPlural = duration.toMinutes() > 1
            "${duration.toMinutes()} minute${if (isPlural) "s" else ""} ago"
        }
        else -> "just now"
    }
}
