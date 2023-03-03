package com.pouffy.create_arcanus.content.contraptions.components.converter;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pouffy.create_arcanus.registry.BlockPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.components.mixer.MechanicalMixerTileEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ConverterRenderer extends KineticTileEntityRenderer {
    public ConverterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRenderOffScreen(KineticTileEntity te) {
        return true;
    }

    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(te.getLevel())) {
            BlockState blockState = te.getBlockState();
            MechanicalMixerTileEntity mixer = (MechanicalMixerTileEntity)te;
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            SuperByteBuffer superBuffer = CachedBufferer.partial(BlockPartials.DARKSTONE_COGWHEEL, blockState);
            standardKineticRotationTransform(superBuffer, te, light).renderInto(ms, vb);
            float renderedHeadOffset = mixer.getRenderedHeadOffset(partialTicks);
            float speed = mixer.getRenderedHeadRotationSpeed(partialTicks);
            float time = AnimationTickHolder.getRenderTime(te.getLevel());
            float angle = time * speed * 6.0F / 10.0F % 360.0F / 180.0F * 3.1415927F;
            SuperByteBuffer poleRender = CachedBufferer.partial(AllBlockPartials.MECHANICAL_MIXER_POLE, blockState);
            poleRender.translate(0.0, (double)(-renderedHeadOffset), 0.0).light(light).renderInto(ms, vb);
            SuperByteBuffer headRender = CachedBufferer.partial(BlockPartials.CONVERTER_HEAD, blockState);
            headRender.rotateCentered(Direction.UP, angle).translate(0.0, (double)(-renderedHeadOffset), 0.0).light(light).renderInto(ms, vb);
        }
    }
}
