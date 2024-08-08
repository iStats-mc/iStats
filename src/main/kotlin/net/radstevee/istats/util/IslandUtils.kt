package net.radstevee.istats.util

import net.minecraft.client.MinecraftClient
import net.radstevee.istats.Game

fun currentGame() =
    MinecraftClient
        .getInstance()
        .inGameHud
        .playerListHud
        .header
        ?.string
        ?.let { header ->
            Game.entries.find { it.displayName.uppercase() in header }
        } ?: Game.LOBBY
