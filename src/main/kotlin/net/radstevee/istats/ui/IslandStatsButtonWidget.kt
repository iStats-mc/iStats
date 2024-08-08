package net.radstevee.istats.ui

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import net.radstevee.istats.util.alivePlayersInGame

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
            MinecraftClient.getInstance().player?.sendMessage(Text.literal("Alive players ingame: ${alivePlayersInGame()}"))
        },
        DEFAULT_NARRATION_SUPPLIER,
    )
