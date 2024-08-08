package net.radstevee.istats.ui

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.text.Text
import net.radstevee.istats.util.currentGame

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
            currentGame()
                .getPlayerNames(
                    MinecraftClient
                        .getInstance()
                        .networkHandler!!
                        .playerList
                        .mapNotNull(PlayerListEntry::getDisplayName),
                ).also {
                    MinecraftClient.getInstance().player?.sendMessage(Text.literal("Alive players ingame: $it"))
                }
        },
        DEFAULT_NARRATION_SUPPLIER,
    )
