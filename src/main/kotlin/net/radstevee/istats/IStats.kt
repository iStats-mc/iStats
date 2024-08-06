package net.radstevee.istats

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.radstevee.istats.ui.IStatsScreen
import net.radstevee.istats.ui.IStatsScreen.client
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory

object IStats : ModInitializer {
    val logger = LoggerFactory.getLogger("istats")

    override fun onInitialize() {
        val statsKeybind =
            KeyBindingHelper.registerKeyBinding(
                KeyBinding(
                    "key.istats.stats_menu",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_K,
                    "category.istats",
                ),
            )

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (statsKeybind.wasPressed()) {
                client.setScreen(IStatsScreen)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
inline fun launch(crossinline block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch { block() }
