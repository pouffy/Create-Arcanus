package com.pouffydev.create_arcanus.content.aureal.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AurealAccumulator extends AurealNetworkElement {
    int storedAether = 0;
    
    public AurealAccumulator(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public abstract int getMaxStorage();
    
    public int getStoredAureal() {
        return storedAether;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        int accumulatorDischarge = getAetherNetwork().getAccumulatorDischarge();
        
        if (accumulatorDischarge > 0) {
            
            int discharge = Math.min(accumulatorDischarge, getStoredAureal());
            
            storedAether -= discharge;
            onContentChange(-discharge);
            getAetherNetwork().pushRequiredAccumulatorAether(discharge);
            
        } else if (accumulatorDischarge < 0) {
            
            int oldStored = getStoredAureal();
            
            int charge = Math.min(-accumulatorDischarge, getMaxStorage() - getStoredAureal());
            
            storedAether += charge;
            onContentChange(charge);
            getAetherNetwork().pullAccumulatorAether(oldStored - getStoredAureal());
            
        }
        
    }
    
    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        
        tag.putInt("StoredAureal", getStoredAureal());
        
        super.write(tag, clientPacket);
    }
    
    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        
        storedAether = tag.getInt("StoredAureal");
        
        super.read(tag, clientPacket);
    }
    
    protected void onContentChange(int change) { }
}
