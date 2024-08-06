package net.radstevee.istats.ui

import com.mojang.authlib.GameProfile
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.PlayerSkinWidget
import net.minecraft.text.Text
import net.radstevee.istats.Player

class PlayerStatisticScreen(
    input: String,
    val player: Player,
) : Screen(Text.literal("Statistics for ${player.username ?: input}")) {
    val skin =
        MinecraftClient
            .getInstance()
            .skinProvider
            .fetchSkinTextures(GameProfile(player.uuid, ""))
            .join()

    override fun close() {
        MinecraftClient.getInstance().setScreen(IStatsScreen)
    }

    override fun init() {
        val skinWidget =
            PlayerSkinWidget(
                20,
                60,
                MinecraftClient.getInstance().entityModelLoader,
            ) { skin }

        addDrawableChild(skinWidget)
    }
}
