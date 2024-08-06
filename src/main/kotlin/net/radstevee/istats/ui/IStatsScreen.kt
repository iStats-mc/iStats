package net.radstevee.istats.ui

import kotlinx.coroutines.runBlocking
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.radstevee.istats.getPlayer

// TODO translation
object IStatsScreen : Screen(Text.literal("MCC Island Statistics")) {
    var isError = false

    fun submit(input: String) {
        isError = false
        val player =
            runBlocking {
                getPlayer(input)
            }

        if (player == null) {
            isError = true
            return
        }

        client!!.execute {
            client!!.setScreen(PlayerStatisticScreen(input, player))
        }
    }

    override fun init() {
        val textField =
            TextFieldWidget(
                textRenderer,
                150,
                35,
                120,
                20,
                Text.literal("test"),
            )
        val submitButton =
            ButtonWidget
                .builder(Text.literal("Submit")) { _ ->
                    submit(textField.text)
                }.dimensions(
                    275,
                    35,
                    40,
                    20,
                ).tooltip(Tooltip.of(Text.literal("Fetch the statistics.")))
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
        context.drawText(
            textRenderer,
            Text.literal("MCC Island Statistics"),
            180,
            40 - this.textRenderer.fontHeight - 10,
            Colors.WHITE,
            true,
        )
        if (isError) {
            context.drawText(
                textRenderer,
                Text.literal("Error: Player not found"),
                175,
                80 - this.textRenderer.fontHeight - 10,
                Colors.RED,
                true,
            )
        }
    }
}
