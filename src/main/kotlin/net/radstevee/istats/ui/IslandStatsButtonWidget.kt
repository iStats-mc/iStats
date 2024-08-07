package net.radstevee.istats.ui

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class IslandStatsButtonWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    text: Text,
) : ButtonWidget(
        x,
        y,
        width,
        height,
        text,
        {
            MinecraftClient
                .getInstance()
                .networkHandler!!
                .playerList
                .mapNotNull {
                    it.displayName
                        ?.siblings
                        ?.getOrNull(4)
                        ?.string
                }.filter { it != "kotlin.Unit" } // LOL why is this a thing
                .forEach(::println)
        },
        DEFAULT_NARRATION_SUPPLIER,
    )
