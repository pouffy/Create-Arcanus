package com.pouffy.create_arcanus.content.contraptions.spectral_goggles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffy.create_arcanus.registry.AllItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.components.structureMovement.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.logistics.trains.entity.TrainRelocator;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.config.CClient;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpectralGoggleOverlayRenderer {
    public static final IIngameOverlay OVERLAY = SpectralGoggleOverlayRenderer::renderOverlay;
    private static final Map<Object, Outliner.OutlineEntry> outlines;
    public static int hoverTicks;
    public static BlockPos lastHovered;

    public SpectralGoggleOverlayRenderer() {
    }

    public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            HitResult objectMouseOver = mc.hitResult;
            if (!(objectMouseOver instanceof BlockHitResult)) {
                lastHovered = null;
                hoverTicks = 0;
            } else {
                Iterator var7 = outlines.values().iterator();

                while(var7.hasNext()) {
                    Outliner.OutlineEntry entry = (Outliner.OutlineEntry)var7.next();
                    if (entry.isAlive()) {
                        Outline outline = entry.getOutline();
                        if (outline instanceof ValueBox && !((ValueBox)outline).isPassive) {
                            return;
                        }
                    }
                }

                BlockHitResult result = (BlockHitResult)objectMouseOver;
                ClientLevel world = mc.level;
                BlockPos pos = result.getBlockPos();
                BlockEntity te = world.getBlockEntity(pos);
                int prevHoverTicks = hoverTicks;
                if (lastHovered != null && !lastHovered.equals(pos)) {
                    hoverTicks = 0;
                } else {
                    ++hoverTicks;
                }

                lastHovered = pos;
                boolean wearingGoggles = SpectralGogglesItem.isWearingGoggles(mc.player);
                boolean hasGoggleInformation = te instanceof IHaveGoggleInformation;
                boolean hasHoveringInformation = te instanceof IHaveHoveringInformation;
                boolean goggleAddedInformation = false;
                boolean hoverAddedInformation = false;
                List<Component> tooltip = new ArrayList();
                if (hasGoggleInformation && wearingGoggles) {
                    IHaveGoggleInformation gte = (IHaveGoggleInformation)te;
                    goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isShiftKeyDown());
                }

                if (hasHoveringInformation) {
                    if (!tooltip.isEmpty()) {
                        tooltip.add(TextComponent.EMPTY);
                    }

                    IHaveHoveringInformation hte = (IHaveHoveringInformation)te;
                    hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isShiftKeyDown());
                    if (goggleAddedInformation && !hoverAddedInformation) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                }

                if (te instanceof IDisplayAssemblyExceptions) {
                    boolean exceptionAdded = ((IDisplayAssemblyExceptions)te).addExceptionToTooltip(tooltip);
                    if (exceptionAdded) {
                        hasHoveringInformation = true;
                        hoverAddedInformation = true;
                    }
                }

                if (!hasHoveringInformation && (hasHoveringInformation = hoverAddedInformation = TrainRelocator.addToTooltip(tooltip, mc.player.isShiftKeyDown()))) {
                    hoverTicks = prevHoverTicks + 1;
                }

                if (!hasGoggleInformation || goggleAddedInformation || !hasHoveringInformation || hoverAddedInformation) {
                    BlockState state = world.getBlockState(pos);
                    int poles;
                    int posY;
                    if (wearingGoggles && AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
                        Direction[] directions = Iterate.directionsInAxis(((Direction)state.getValue(PistonExtensionPoleBlock.FACING)).getAxis());
                        poles = 1;
                        boolean pistonFound = false;
                        Direction[] var22 = directions;
                        posY = directions.length;

                        for(int var24 = 0; var24 < posY; ++var24) {
                            Direction dir = var22[var24];
                            int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get().attachedPoles(world, pos, dir);
                            poles += attachedPoles;
                            pistonFound |= world.getBlockState(pos.relative(dir, attachedPoles + 1)).getBlock() instanceof MechanicalPistonBlock;
                        }

                        if (!pistonFound) {
                            return;
                        }

                        if (!tooltip.isEmpty()) {
                            tooltip.add(TextComponent.EMPTY);
                        }

                        tooltip.add(IHaveGoggleInformation.componentSpacing.plainCopy().append(Lang.translateDirect("gui.goggles.pole_length", new Object[0])).append(new TextComponent(" " + poles)));
                    }

                    if (!tooltip.isEmpty()) {
                        poseStack.pushPose();
                        int tooltipTextWidth = 0;
                        Iterator var37 = tooltip.iterator();

                        int posX;
                        while(var37.hasNext()) {
                            FormattedText textLine = (FormattedText)var37.next();
                            posX = mc.font.width(textLine);
                            if (posX > tooltipTextWidth) {
                                tooltipTextWidth = posX;
                            }
                        }

                        poles = 8;
                        if (tooltip.size() > 1) {
                            poles += 2;
                            poles += (tooltip.size() - 1) * 10;
                        }

                        CClient cfg = AllConfigs.CLIENT;
                        posX = width / 2 + (Integer)cfg.overlayOffsetX.get();
                        posY = height / 2 + (Integer)cfg.overlayOffsetY.get();
                        posX = Math.min(posX, width - tooltipTextWidth - 20);
                        posY = Math.min(posY, height - poles - 20);
                        float fade = Mth.clamp(((float)hoverTicks + partialTicks) / 12.0F, 0.0F, 1.0F);
                        Boolean useCustom = (Boolean)cfg.overlayCustomColor.get();
                        Color colorBackground = useCustom ? new Color((Integer)cfg.overlayBackgroundColor.get()) : Theme.c(Theme.Key.VANILLA_TOOLTIP_BACKGROUND).scaleAlpha(0.75F);
                        Color colorBorderTop = useCustom ? new Color((Integer)cfg.overlayBorderColorTop.get()) : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, true).copy();
                        Color colorBorderBot = useCustom ? new Color((Integer)cfg.overlayBorderColorBot.get()) : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, false).copy();
                        if (fade < 1.0F) {
                            poseStack.translate((double)((1.0F - fade) * Math.signum((float)(Integer)cfg.overlayOffsetX.get() + 0.5F) * 4.0F), 0.0, 0.0);
                            colorBackground.scaleAlpha(fade);
                            colorBorderTop.scaleAlpha(fade);
                            colorBorderBot.scaleAlpha(fade);
                        }

                        RemovedGuiUtils.drawHoveringText(poseStack, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(), colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);
                        ItemStack item = AllItems.SPECTRAL_GOGGLES.asStack();
                        GuiGameElement.of(item).at((float)(posX + 10), (float)(posY - 16), 450.0F).render(poseStack);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    static {
        outlines = CreateClient.OUTLINER.getOutlines();
        hoverTicks = 0;
        lastHovered = null;
    }
}
