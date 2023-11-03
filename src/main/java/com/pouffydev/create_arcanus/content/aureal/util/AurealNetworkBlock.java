package com.pouffydev.create_arcanus.content.aureal.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface AurealNetworkBlock {
    default void onNetworkBlockRemove(BlockState state, Level world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof AurealNetworkElement element))
            return;
        AurealNetwork network = element.getAetherNetwork();
        if (network != null) {
            network.removeChild(element);
            network.validateIntegrity(world);
        }
    }
}
