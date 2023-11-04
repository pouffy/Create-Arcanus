package com.pouffydev.create_arcanus.content.runes.activator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.stal111.forbidden_arcanus.client.model.MagicCircleModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ActivatorRenderer implements BlockEntityRenderer<ActivatorBlockEntity> {
    public ActivatorRenderer(BlockEntityRendererProvider.Context context) {
        this.magicCircleModel = new MagicCircleModel(context);
    }
    private final MagicCircleModel magicCircleModel;
    public void render(@Nonnull ActivatorBlockEntity blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = blockEntity.getStack();
        if (blockEntity.hasMagicCircle()) {
            blockEntity.getMagicCircle().render(poseStack, partialTicks, bufferSource, packedLight, this.magicCircleModel);
        }
        
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, (float)blockEntity.getItemHeight() / 100.0F, 0.5);
            poseStack.mulPose(Axis.YP.rotation(blockEntity.getItemHover(partialTicks)));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
        
    }
}
