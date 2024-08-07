package net.radstevee.istats.ui

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import net.radstevee.istats.Game
import net.radstevee.istats.IslandPlayer
import net.radstevee.istats.ServerCategory
import net.radstevee.istats.util.drawTextRelative
import net.radstevee.istats.util.getPosition
import net.radstevee.istats.util.relative

class PlayerStatisticScreen(
    val input: String,
    val islandPlayer: IslandPlayer,
) : Screen(Text.literal("Statistics for ${islandPlayer.username ?: input}")) {
    val username = islandPlayer.username ?: input

    override fun close() {
        MinecraftClient.getInstance().setScreen(IStatsScreen)
    }

    override fun init() {
        /* val skin =
            MinecraftClient
                .getInstance()
                .skinProvider
                .getSkinTexturesSupplier(GameProfile(islandPlayer.uuid, islandPlayer.username))

        println(
            "Game profile: ${
                GameProfile(
                    islandPlayer.uuid,
                    islandPlayer.username,
                )
            }, UUID: ${islandPlayer.uuid}, actual username: ${islandPlayer.username}, skin: $skin",
        )
        val skinWidget =
            PlayerSkinWidget(
                20,
                60,
                client!!.entityModelLoader,
                skin,
            )

        addDrawableChild(skinWidget) */
    }

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        super.render(context, mouseX, mouseY, delta)
        val textPosition = context.getPosition(textRenderer, Text.literal(username), 50, 5)
        context.drawText(
            textRenderer,
            Text.literal(username),
            textPosition.first,
            textPosition.second,
            Colors.WHITE,
            true,
        )

        val rankX = textPosition.first - (textPosition.first / 10)
        val rankY = textPosition.second - (textPosition.second / 3)

        context.drawTexture(
            islandPlayer.primaryRank.texture,
            rankX,
            rankY,
            0f,
            0f,
            16,
            16,
            16,
            16,
        )

        if (islandPlayer.status?.online == true) {
            if (islandPlayer.status.server?.category == ServerCategory.LOBBY) {
                val (texture, text) =
                    when (islandPlayer.status.server.associatedGame) {
                        Game.BATTLE_BOX -> Game.BATTLE_BOX.texture to Text.literal("In the Battle Box lobby")
                        Game.TGTTOS -> Game.BATTLE_BOX.texture to Text.literal("In the TGTTOS lobby")
                        Game.DYNABALL -> Game.BATTLE_BOX.texture to Text.literal("In the Dynaball Lobby")
                        Game.SKY_BATTLE -> Game.BATTLE_BOX.texture to Text.literal("In the Sky Battle Lobby")
                        Game.ROCKET_SPLEEF -> Game.BATTLE_BOX.texture to Text.literal("In the RSR Lobby")
                        Game.HOLE_IN_THE_WALL -> Game.BATTLE_BOX.texture to Text.literal("In the HITW lobby")
                        Game.PARKOUR_WARRIOR -> Game.PARKOUR_WARRIOR.texture to Text.literal("In the Parkour Warrior lobby")
                        null -> Identifier.of("istats", "textures/misc/main_island.png") to Text.literal("On the Main Island")
                    }

                val textPosition = context.getPosition(textRenderer, text, 50, 13)
                context.drawText(
                    textRenderer,
                    text,
                    textPosition.first,
                    textPosition.second,
                    Colors.WHITE,
                    true,
                )

                val textureX = textPosition.first - (textPosition.first / 10)
                val textureY = textPosition.second - (textPosition.second / 7)

                context.drawTexture(
                    texture,
                    textureX,
                    textureY,
                    0f,
                    0f,
                    16,
                    16,
                    16,
                    16,
                )
            } else if (islandPlayer.status.server?.category == ServerCategory.GAME && islandPlayer.status.server.associatedGame != null) {
                val texture = islandPlayer.status.server.associatedGame.texture
                val text = Text.literal("Playing ${islandPlayer.status.server.associatedGame.displayName}")

                val textPosition = context.getPosition(textRenderer, text, 50, 13)
                context.drawText(
                    textRenderer,
                    text,
                    textPosition.first,
                    textPosition.second,
                    Colors.WHITE,
                    true,
                )

                val textureX = textPosition.first - (textPosition.first / 10)
                val textureY = textPosition.second - (textPosition.second / 7)

                context.drawTexture(
                    texture,
                    textureX,
                    textureY,
                    0f,
                    0f,
                    16,
                    16,
                    16,
                    16,
                )
            } else {
                context.drawTextRelative(
                    textRenderer,
                    Text.literal("Online"),
                    50,
                    13,
                    Colors.WHITE,
                    true,
                )
            }
        } else {
            islandPlayer.status?.lastJoin?.let { lastJoin ->
                context.drawTextRelative(
                    textRenderer,
                    Text.literal("Last logged on ${lastJoin.relative()}"),
                    50,
                    13,
                    Colors.WHITE,
                    true,
                )
            }
        }
    }
}
