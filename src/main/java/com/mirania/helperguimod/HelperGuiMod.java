package com.mirania.helperguimod;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(HelperGuiMod.MOD_ID)
public class HelperGuiMod
{
    public static final String MOD_ID = "helper_gui_mod";

    private final int textColor = 0x00E0E0E0;

    private final int backgroundColor = 0x06606060;

    private final int rightX = 8, topY = 8;

    private final GuiData guiData;

    public HelperGuiMod()
    {
        this.guiData = new GuiData();

        // register this class as an event handler
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderGuiOverlay(final RenderGuiOverlayEvent.Pre event) {
        if (Minecraft.getInstance().options.renderDebug) {
            // this mod is redundant while debug menu is open
            return;
        }

        this.drawText(event, this.guiData.getPlayerPosition(), 0, false);
        this.drawText(event, this.guiData.getPlayerDirection(), 1, false);
        this.drawText(event, this.guiData.getBiome(), 2, false);
        this.drawText(event, this.guiData.getIngameTime(), 3, false);
        this.drawText(event, this.guiData.getLight(), 4, true);
    }

    private void drawText(final RenderGuiOverlayEvent.Pre event, @Nullable final Component component,
                          final int row, final boolean isLastRow) {

        if (component == null) {
            return;
        }

        final Font font = Minecraft.getInstance().font;
        final int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        final int rowStartY = this.topY + row * (font.lineHeight + 2);
        final int rowEndY = rowStartY + font.lineHeight + (isLastRow ? 1 : 0);

        GuiComponent.fill(event.getPoseStack(), screenWidth - font.width(component) - 10, rowStartY - 2, screenWidth - this.rightX + 2, rowEndY, this.backgroundColor);
        font.drawShadow(event.getPoseStack(), component, screenWidth - font.width(component) - 8, rowStartY, this.textColor);
    }

}
