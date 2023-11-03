package com.pouffydev.create_arcanus.content.aureal.util;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AurealNetworkElement extends SmartBlockEntity implements IHaveGoggleInformation {
    
    @Nullable
    AurealNetwork aetherNetwork;
    
    public AurealNetworkElement(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (aetherNetwork == null) {
            aetherNetwork = getOrCreateAetherNetwork();
        }
    }
    
    /**Adds debug info about the network if enabled
     * In game the alternative would be a display link in an accumulator or something*/
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (AurealNetwork.SHOW_NETWORK_DEBUG && aetherNetwork != null) {
            Lang.text("Network ID: " + aetherNetwork.networkId.toString()).forGoggles(tooltip);
            //Lang.text("Network Producers: " + aetherNetwork.networkProducers.size()).forGoggles(tooltip);
            //Lang.text("Network Consumers: " + aetherNetwork.networkConsumers.size()).forGoggles(tooltip);
            //Lang.text("Network Accumulators: " + aetherNetwork.networkAccumulators.size()).forGoggles(tooltip);
            Lang.text("Network Total Elements: " + aetherNetwork.networkElements.size()).forGoggles(tooltip);
            
            Lang.text("Aureal: " + aetherNetwork.storedAether + "/" + aetherNetwork.maxStorage).color(0x9fe2fd).forGoggles(tooltip);
            //Lang.text("Network storedAether: " + aetherNetwork.storedAether).forGoggles(tooltip);
            Lang.text("Production: " + aetherNetwork.production).color(0xc8ff8f).forGoggles(tooltip);
            Lang.text("Consumption: " + aetherNetwork.consumption).color(0xff8181).forGoggles(tooltip);
            return true;
        }
        return false;
    }
    
    /**Will look around for available networks, and resolve issues such as merging networks*/
    public void updateConnectedNetwork() {
        for (Direction direction : Direction.values()) {
            
            BlockPos otherPos = getBlockPos().relative(direction);
            BlockEntity otherEntity = level.getBlockEntity(otherPos);
            
            if (otherEntity instanceof AurealNetworkElement otherElement) {
                if (aetherNetwork == null && otherElement.hasAetherNetwork()) {
                    otherElement.getAetherNetwork().addChild(this);
                } else if (aetherNetwork != otherElement.getAetherNetwork()) {
                    aetherNetwork.addChild(otherElement);
                    otherElement.updateConnectedNetwork();
                }
            }
            
        }
    }
    
    @Override
    public void remove() {
        if (hasAetherNetwork()) {
            getAetherNetwork().removeChild(this);
        }
        super.remove();
    }
    
    /**Returns the current network, or will look for a valid one to connect to, or will create one*/
    public AurealNetwork getOrCreateAetherNetwork() {
        if (aetherNetwork == null)
            updateConnectedNetwork();
        if (aetherNetwork == null)
            new AurealNetwork(level).addChild(this);
        
        return aetherNetwork;
    }
    
    public @Nullable AurealNetwork getAetherNetwork() {
        return aetherNetwork;
    }
    
    public boolean hasAetherNetwork() {
        return aetherNetwork != null;
    }
    
    public void setAetherNetwork(AurealNetwork aetherNetwork) {
        this.aetherNetwork = aetherNetwork;
    }
    
    /**Nullify the current network*/
    public void clearNetwork() {
        aetherNetwork = null;
    }
    
}
