package com.pouffydev.create_arcanus.content.aureal.battery;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pouffydev.create_arcanus.CAPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class BatteryRenderer extends SafeBlockEntityRenderer<BatteryBlockEntity> {
    
    public BatteryRenderer(BlockEntityRendererProvider.Context context) { }
    
    @Override
    protected void renderSafe(BatteryBlockEntity te, float partialTicks,
                              PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        
        BlockState state = te.getBlockState();
        
        BatteryBlock.Shape shape = state.getValue(BatteryBlock.SHAPE);
        boolean top = state.getValue(BatteryBlock.TOP);
        boolean bottom = state.getValue(BatteryBlock.BOTTOM);
        
        if (state.getBlock() instanceof BatteryBlock batteryBlock && batteryBlock.isCreative()) {
            if (top) {
                CachedBufferer.partial(CAPartialModels.CREATIVE_BATTERY_TOP.get(shape), state)
                        .renderInto(ms, vb);
            }
            if (bottom) {
                CachedBufferer.partial(CAPartialModels.CREATIVE_BATTERY_BOTTOM.get(shape), state)
                        .renderInto(ms, vb);
            }
            
            CachedBufferer.partial(CAPartialModels.CREATIVE_BATTERY_BLOCK_SHAPES.get(shape), state)
                    .renderInto(ms, vb);
            
            return;
        }
        
        if (top) {
            CachedBufferer.partial(CAPartialModels.BATTERY_TOP.get(shape), state)
                    .renderInto(ms, vb);
        }
        if (bottom) {
            CachedBufferer.partial(CAPartialModels.BATTERY_BOTTOM.get(shape), state)
                    .renderInto(ms, vb);
        }
        
        int chargeLevel = te.getDisplayedChargeLevel();
        
        CachedBufferer.partial(CAPartialModels.BATTERY_BLOCK_CHARGE_SHAPES.get(chargeLevel).get(shape), state)
                .renderInto(ms, vb);
        
    }
}
