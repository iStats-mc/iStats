package net.radstevee.istats.ui

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.radstevee.istats.getPlayer
import net.radstevee.istats.util.drawTextRelative
import net.radstevee.istats.util.getPosition
import java.net.ConnectException

// TODO translation
object IStatsScreen : Screen(Text.literal("MCC Island Statistics")) {
    var error = ""

    fun submit(input: String) {
        error = ""
        client!!.execute {
            val returnedPlayer =
                runBlocking {
                    getPlayer(input)
                }

            returnedPlayer.onFailure { exception ->
                if (exception is IllegalArgumentException) error = "Invalid player!"
                if (exception is ConnectException) error = "Failed to connect to API!"
                if (exception is SerializationException) {
                    error = "Error whilst parsing response! Check the console for more"
                    exception.printStackTrace()
                }
                return@execute
            }

            client!!.setScreen(PlayerStatisticScreen(input, returnedPlayer.getOrThrow()))
        }
    }

    override fun init() {
        val textFieldPosition =
            getPosition(
                50,
                10,
                120,
                20,
            )
        val textField =
            TextFieldWidget(
                textRenderer,
                textFieldPosition.first,
                textFieldPosition.second,
                120,
                20,
                Text.literal("Player Input Field"),
            )
        val buttonPosition =
            getPosition(
                50,
                20,
                40,
                20,
            )

        val submitButton =
            ButtonWidget
                .builder(Text.literal("Submit")) { _ ->
                    submit(textField.text)
                }.dimensions(
                    buttonPosition.first,
                    buttonPosition.second,
                    40,
                    20,
                ).tooltip(Tooltip.of(Text.literal("Fetch statistics.")))
                .build()

        addDrawableChild(submitButton)
        addDrawableChild(textField)
    }

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        super.render(context, mouseX, mouseY, delta)

        context.drawTextRelative(
            textRenderer,
            Text.literal("MCC Island Statistics"),
            50,
            5,
            Colors.WHITE,
            true,
        )

        context.drawTextRelative(
            textRenderer,
            Text.literal(error),
            50,
            30,
            Colors.RED,
            true,
        )
    }
}
