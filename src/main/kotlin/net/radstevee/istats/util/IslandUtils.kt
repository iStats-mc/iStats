package net.radstevee.istats.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.text.Text
import net.minecraft.util.Colors

val USERNAME_REGEX = Regex("\"text\":\"[a-zA-Z0-9_]{2,16}\"")
const val ELIMINATED_PLAYER_COLOR = Colors.GRAY

// words in the MCC+ tooltip which are matched by the regex.
// sorry to anyone with these usernames!
val MCC_PLUS_TOOLTIP_MATCHED_WORDS =
    listOf(
        "Displayed",
        "by",
        "users",
        "who",
        "have",
        "the",
        "Rank",
        "currently",
        "Learn",
        "more",
        "at",
    )

fun alivePlayersInGame() =
    MinecraftClient
        .getInstance()
        .networkHandler!!
        .playerList
        .asSequence()
        .mapNotNull(PlayerListEntry::getDisplayName)
        .filter { it.style.color?.rgb != ELIMINATED_PLAYER_COLOR }
        .mapNotNull { text ->
            Text.Serialization.toJsonString(text, MinecraftClient.getInstance().networkHandler!!.registryManager)
        }.mapNotNull(USERNAME_REGEX::find)
        .mapNotNull(MatchResult::value)
        .map {
            it.replace("\"text\":\"", "").replace("\"", "")
        }.filterNot(MCC_PLUS_TOOLTIP_MATCHED_WORDS::contains)
        .filter(String::isNotBlank)
        .distinct()
        .toList()
