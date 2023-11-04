package com.pouffydev.create_arcanus.content.runes.activator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ActivatorBlockItemRenderer extends CustomRenderedItemModelRenderer {
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        float worldTime = AnimationTickHolder.getRenderTime() / 10;
        float angle = worldTime * -2 / 10.0F;
        if (!stack.isEmpty()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("BlockEntityTag")) {
                CompoundTag blockEntityTag = compoundnbt.getCompound("BlockEntityTag");
                if (blockEntityTag.contains("ItemHeight") && blockEntityTag.contains("Stack")) {
                    int itemHeight = 60;
                    ItemStack itemStack = ItemStack.of(blockEntityTag.getCompound("Stack"));
                    ms.pushPose();
                    ms.translate(0, (float)itemHeight / 100.0F, 0);
                    ms.mulPose(Axis.YP.rotation(angle));
                    ms.scale(0.5F, 0.5F, 0.5F);
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, Minecraft.getInstance().level, 0);
                    ms.popPose();
                }
            }
        }
        renderer.render(model.getOriginalModel(), light);
    }
}
