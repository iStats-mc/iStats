package net.radstevee.istats.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.radstevee.istats.ui.IslandStatsButtonWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.List;

// Fully copied from ModMenu
@Mixin(GameMenuScreen.class)
public class MixinGameMenu extends Screen {
    protected MixinGameMenu(Text title) {
        super(title);
    }

    private void shiftButtons(Widget widget, boolean shiftUp, int spacing) {
        if (shiftUp) {
            widget.setY(widget.getY() - spacing / 2);
        } else if (!(widget instanceof ClickableWidget button && button.getMessage().equals(Text.translatable("title.credits")))) {
            widget.setY(widget.getY() + spacing / 2);
        }
    }

    private boolean buttonHasText(Widget widget, String... translationKeys) {
        if (widget instanceof ButtonWidget button) {
            Text text = button.getMessage();
            TextContent textContent = text.getContent();

            return textContent instanceof TranslatableTextContent && Arrays.stream(translationKeys).anyMatch(s -> ((TranslatableTextContent) textContent).getKey().equals(s));
        }
        return false;
    }

    @Inject(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget;forEachChild(Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void init(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder, Text text) {
        if (gridWidget == null) return;

        //@Nullable ServerInfo currentServer = MinecraftClient.getInstance().getCurrentServerEntry();
        //if (currentServer == null) return;
        //if (!currentServer.address.endsWith("mccisland.net")) return; // TODO is there a better way to do this?

        final List<Widget> buttons = ((AccessorGridWidget) gridWidget).getChildren();

        int islandStatisticsButtonIndex = -1;
        final int spacing = 24;
        int buttonsY = this.height / 4 + 8;
        final int fullWidthButton = 204;

        for (var widget : buttons) {
            if (!(widget instanceof ClickableWidget button) || button.visible) {
                shiftButtons(widget, islandStatisticsButtonIndex == -1 || buttonHasText(widget, "menu.reportBugs", "menu.server_links"), spacing);

                if (islandStatisticsButtonIndex == -1) buttonsY = widget.getY();
            }

            var isShortFeedback = buttonHasText(widget, "menu.feedback");
            var isLongFeedback = buttonHasText(widget, "menu.sendFeedback");

            if (isShortFeedback || isLongFeedback) {
                islandStatisticsButtonIndex += 2;

                if (!(widget instanceof ClickableWidget button) || button.visible) {
                    buttonsY = widget.getY();
                }
            }
        }

        if (islandStatisticsButtonIndex != -1) {
            buttons.add(islandStatisticsButtonIndex, new IslandStatsButtonWidget(this.width / 2 - 102, buttonsY + spacing, fullWidthButton, 20, Text.literal("Island Stats")));
        }
    }
}