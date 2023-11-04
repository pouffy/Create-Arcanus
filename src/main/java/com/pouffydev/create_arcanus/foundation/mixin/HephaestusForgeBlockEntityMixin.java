package com.pouffydev.create_arcanus.foundation.mixin;

import com.pouffydev.create_arcanus.foundation.CALang;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.stal111.forbidden_arcanus.common.block.HephaestusForgeBlock;
import com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeBlockEntity;
import com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeLevel;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceManager;
import com.stal111.forbidden_arcanus.util.ValueNotifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HephaestusForgeBlockEntity.class)
public abstract class HephaestusForgeBlockEntityMixin implements IHaveGoggleInformation {
    @Shadow @Final private EssenceManager essenceManager;
    @Shadow @Final private ValueNotifier<HephaestusForgeLevel> forgeLevel;
    @Shadow @Final private ContainerData hephaestusForgeData;
    @Unique
    private static int create_arcanus$tooltipReload = 0;
    @Unique
    
    @Override
    public boolean addToGoggleTooltip(java.util.List<net.minecraft.network.chat.Component> tooltip, boolean isPlayerSneaking) {
        EssenceManager manager = this.essenceManager;
        if (manager == null)
            return false;
        CALang.translate("gui.goggles.hephaestus_forge").forGoggles(tooltip);
            CALang.builder()
                    .add(CALang.translate("gui.goggles.aureal"))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            CALang.builder()
                    .add(CALang.number(manager.getAureal())
                            .style(ChatFormatting.GOLD)
                            .text(ChatFormatting.GRAY, " / ")
                            .add(CALang.number(this.forgeLevel.get().getMaxAureal()).style(ChatFormatting.DARK_GRAY)))
                    .forGoggles(tooltip, 1);
            CALang.builder()
                    .add(CALang.translate("gui.goggles.souls"))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            CALang.builder()
                    .add(CALang.number(manager.getSouls())
                            .style(ChatFormatting.GOLD)
                            .text(ChatFormatting.GRAY, " / ")
                            .add(CALang.number(this.forgeLevel.get().getMaxSouls()).style(ChatFormatting.DARK_GRAY)))
                    .forGoggles(tooltip, 1);
            CALang.builder()
                    .add(CALang.translate("gui.goggles.blood"))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            CALang.builder()
                    .add(CALang.number(manager.getBlood())
                            .style(ChatFormatting.GOLD)
                            .text(ChatFormatting.GRAY, " / ")
                            .add(CALang.number(this.forgeLevel.get().getMaxBlood()).style(ChatFormatting.DARK_GRAY)))
                    .forGoggles(tooltip, 1);
            CALang.builder()
                    .add(CALang.translate("gui.goggles.experience"))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            CALang.builder()
                    .add(CALang.number(manager.getExperience())
                            .style(ChatFormatting.GOLD)
                            .text(ChatFormatting.GRAY, " / ")
                            .add(CALang.number(this.forgeLevel.get().getMaxExperience()).style(ChatFormatting.DARK_GRAY)))
                    .forGoggles(tooltip, 1);
        return true;
    }
    
    @Inject(method = "load", at = @At(value = "TAIL"), remap = false)
    public void load(CompoundTag tag, CallbackInfo ci) {
        create_arcanus$tooltipReload = tag.getInt("TooltipReload");
    }
    
    @Inject(method = "onSlotChanged", at = @At(value = "TAIL"), remap = false)
    public void onSlotChanged(int slot, CallbackInfo ci) {
        create_arcanus$tooltipReload += 1;
    }
    @Inject(method = "serverTick", at = @At(value = "TAIL"), remap = false)
    private static void serverTick(Level level, BlockPos pos, BlockState state, HephaestusForgeBlockEntity blockEntity, CallbackInfo ci) {
        if (create_arcanus$tooltipReload >= 1) {
            create_arcanus$tooltipReload -= 1;
            level.setBlockAndUpdate(pos, state.setValue(HephaestusForgeBlock.ACTIVATED, false));
        }
    }
}
