package com.pouffydev.create_arcanus.content.aureal.battery;

import com.pouffydev.create_arcanus.content.aureal.util.AurealProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeBatteryBlockEntity extends BatteryBlockEntity implements AurealProducer {
    
    static final int MILLION = 1000000;
    
    public CreativeBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public int getProducedAureal() {
        return MILLION;
    }
    
    @Override
    public int getMaxStorage() {
        return MILLION;
    }
    
    @Override
    public int getStoredAureal() {
        return MILLION;
    }
}
