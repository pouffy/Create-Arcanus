package com.pouffydev.create_arcanus.foundation.mixin;

import com.pouffydev.create_arcanus.content.runes.activator.ActivatorBlockEntity;
import com.stal111.forbidden_arcanus.common.block.entity.forge.MagicCircle;
import com.stal111.forbidden_arcanus.common.network.ClientPacketHandler;
import com.stal111.forbidden_arcanus.common.network.clientbound.CreateMagicCirclePacket;
import com.stal111.forbidden_arcanus.common.network.clientbound.RemoveMagicCirclePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketHandler.class)
public abstract class HandleMagicCircleMixin {
    @Shadow
    private static ClientLevel getLevel() {
        return Minecraft.getInstance().level;
    }
    @Inject(method = "handleCreateMagicCircle", at = @At("HEAD"), cancellable = true, remap = false)
    private static void create_arcanus$handleCreateMagicCircle(CreateMagicCirclePacket packet, CallbackInfo ci) {
        if (getLevel() != null) {
            BlockEntity var3 = getLevel().getBlockEntity(packet.pos());
            if (var3 instanceof ActivatorBlockEntity blockEntity) {
                blockEntity.setMagicCircle(new MagicCircle(getLevel(), packet.pos(), blockEntity.circleInnerTexture(), blockEntity.circleOuterTexture()));
                return;
            }
        }
    }

    @Inject(method = "handleRemoveMagicCircle", at = @At("HEAD"), cancellable = true, remap = false)
    private static void create_arcanus$handleRemoveMagicCircle(RemoveMagicCirclePacket packet, CallbackInfo ci) {
        if (getLevel() != null) {
            BlockEntity var3 = getLevel().getBlockEntity(packet.pos());
            if (var3 instanceof ActivatorBlockEntity blockEntity) {
                blockEntity.removeMagicCircle();
                return;
            }
        }
    }
}
