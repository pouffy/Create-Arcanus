package com.pouffy.create_arcanus.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.pouffy.create_arcanus.registry.BlockPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.util.Mth;

public class AnimatedConverter extends AnimatedKinetics {
    public AnimatedConverter() {
    }

    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate((double)xOffset, (double)yOffset, 200.0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5F));
        int scale = 23;
        this.blockElement(this.cogwheel()).rotateBlock(0.0, (double)(getCurrentAngle() * 2.0F), 0.0).atLocal(0.0, 0.0, 0.0).scale((double)scale).render(matrixStack);
        this.blockElement(com.pouffy.create_arcanus.registry.AllBlocks.CONVERTER.getDefaultState()).atLocal(0.0, 0.0, 0.0).scale((double)scale).render(matrixStack);
        float animation = (Mth.sin(AnimationTickHolder.getRenderTime() / 32.0F) + 1.0F) / 5.0F + 0.5F;
        this.blockElement(AllBlockPartials.MECHANICAL_MIXER_POLE).atLocal(0.0, (double)animation, 0.0).scale((double)scale).render(matrixStack);
        this.blockElement(BlockPartials.CONVERTER_HEAD).rotateBlock(0.0, (double)(getCurrentAngle() * 4.0F), 0.0).atLocal(0.0, (double)animation, 0.0).scale((double)scale).render(matrixStack);
        this.blockElement(AllBlocks.BASIN.getDefaultState()).atLocal(0.0, 1.65, 0.0).scale((double)scale).render(matrixStack);
        matrixStack.popPose();
    }
}
