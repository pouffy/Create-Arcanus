package com.pouffy.create_arcanus.content.contraptions.spectral_goggles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffy.create_arcanus.registry.AllItems;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpectralGoggleConfigScreen extends AbstractSimiScreen {
    private int offsetX;
    private int offsetY;
    private final List<Component> tooltip;

    public SpectralGoggleConfigScreen() {
        Component componentSpacing = new TextComponent("    ");
        this.tooltip = new ArrayList();
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay1", new Object[0])));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay2", new Object[0]).withStyle(ChatFormatting.GRAY)));
        this.tooltip.add(TextComponent.EMPTY);
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay3", new Object[0])));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay4", new Object[0])));
        this.tooltip.add(TextComponent.EMPTY);
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay5", new Object[0]).withStyle(ChatFormatting.GRAY)));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay6", new Object[0]).withStyle(ChatFormatting.GRAY)));
        this.tooltip.add(TextComponent.EMPTY);
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay7", new Object[0])));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay8", new Object[0])));
    }

    protected void init() {
        this.width = this.minecraft.getWindow().getGuiScaledWidth();
        this.height = this.minecraft.getWindow().getGuiScaledHeight();
        this.offsetX = (Integer) AllConfigs.CLIENT.overlayOffsetX.get();
        this.offsetY = (Integer)AllConfigs.CLIENT.overlayOffsetY.get();
    }

    public void removed() {
        AllConfigs.CLIENT.overlayOffsetX.set(this.offsetX);
        AllConfigs.CLIENT.overlayOffsetY.set(this.offsetY);
    }

    public boolean mouseClicked(double x, double y, int button) {
        this.updateOffset(x, y);
        return true;
    }

    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        this.updateOffset(p_mouseDragged_1_, p_mouseDragged_3_);
        return true;
    }

    private void updateOffset(double windowX, double windowY) {
        this.offsetX = (int)(windowX - (double)(this.width / 2));
        this.offsetY = (int)(windowY - (double)(this.height / 2));
        int titleLinesCount = 1;
        int tooltipTextWidth = 0;
        Iterator var7 = this.tooltip.iterator();

        while(var7.hasNext()) {
            FormattedText textLine = (FormattedText)var7.next();
            int textLineWidth = this.minecraft.font.width(textLine);
            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }

        int tooltipHeight = 8;
        if (this.tooltip.size() > 1) {
            tooltipHeight += (this.tooltip.size() - 1) * 10;
            if (this.tooltip.size() > titleLinesCount) {
                tooltipHeight += 2;
            }
        }

        this.offsetX = Mth.clamp(this.offsetX, -(this.width / 2) - 5, this.width / 2 - tooltipTextWidth - 20);
        this.offsetY = Mth.clamp(this.offsetY, -(this.height / 2) + 17, this.height / 2 - tooltipHeight + 5);
    }

    protected void renderWindow(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        int posX = this.width / 2 + this.offsetX;
        int posY = this.height / 2 + this.offsetY;
        this.renderComponentTooltip(ms, this.tooltip, posX, posY);
        ItemStack item = AllItems.SPECTRAL_GOGGLES.asStack();
        GuiGameElement.of(item).at((float)(posX + 10), (float)(posY - 16), 450.0F).render(ms);
    }
}
