package com.pouffydev.create_arcanus.content.aureal.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AurealNetwork {
    public static boolean SHOW_NETWORK_DEBUG = true;
    public static final List<AurealNetwork> ALL_NETWORKS = new ArrayList<>();
    
    //Stored for unloading
    LevelAccessor level;
    boolean isUnloaded;
    
    //Simulated values, is not used for calculation,
    int production;
    int consumption;
    int storedAether;
    int maxStorage;
    
    //Tracks how much energy is needed from and is available to the accumulators in the current tick
    int accumulatorDischarge;
    
    //All elements regardless of type
    List<AurealNetworkElement> networkElements = new ArrayList<>();
    
    //Quick Access for Subtypes
    List<AurealConsumer> networkConsumers = new ArrayList<>();
    List<AurealProducer> networkProducers = new ArrayList<>();
    List<AurealAccumulator> networkAccumulators = new ArrayList<>();
    
    //For debugging connectivity
    UUID networkId = UUID.randomUUID();
    
    public AurealNetwork(LevelAccessor level) {
        ALL_NETWORKS.add(this);
        this.level = level;
    }
    
    //Processing
    
    public void tickNetwork() {
        accumulatorDischarge = consumption - production;
    }
    
    public int getAccumulatorDischarge() {
        return accumulatorDischarge;
    }
    
    
    public void pushRequiredAccumulatorAether(int change) {
        this.storedAether -= change;
        this.accumulatorDischarge -= change;
    }
    
    public void pullAccumulatorAether(int change) {
        this.storedAether += change;
        this.accumulatorDischarge -= change;
    }
    
    public boolean isOverloaded() {
        return consumption > (production + storedAether);
    }
    
    public boolean canProvide(int amount) {
        return (consumption + amount) <= (production + storedAether);
    }
    
    //Content queries Need to optimise
    @Deprecated
    public void updateConsumption() {
        consumption = 0;
        for (AurealConsumer consumer : networkConsumers) {
            consumption += consumer.getRequestedAureal();
        }
    }
    
    @Deprecated
    public void updateProduction() {
        production = 0;
        for (AurealProducer producer : networkProducers) {
            production += producer.getProducedAureal();
        }
    }
    
    @Deprecated
    public void updateAccumulation() {
        storedAether = 0;
        maxStorage = 0;
        for (AurealAccumulator accumulator : networkAccumulators) {
            storedAether += accumulator.getStoredAureal();
            maxStorage += accumulator.getMaxStorage();
        }
    }
    
    //Connectivity handling
    
    public void validateIntegrity(LevelAccessor accessor) {
        if (networkElements.size() == 0) return;
        
        List<AurealNetworkElement> visited = new ArrayList<>();
        
        seekIntegrity(networkElements.get(0).getBlockPos(), accessor, visited);
        
        List<AurealNetworkElement> safeChildren = new ArrayList<>(networkElements);
        
        safeChildren.forEach(networkBlockEntity -> {
            if (!visited.contains(networkBlockEntity))
                networkBlockEntity.clearNetwork();
        });
        safeChildren.forEach(networkBlockEntity -> {
            if (!visited.contains(networkBlockEntity))
                networkBlockEntity.getOrCreateAetherNetwork();
        });
        
        //Initialise all
        networkElements = new ArrayList<>();
        networkProducers = new ArrayList<>();
        networkConsumers = new ArrayList<>();
        networkAccumulators = new ArrayList<>();
        
        visited.forEach(this::addChild);
    }
    
    private void seekIntegrity(BlockPos blockPos, LevelAccessor accessor, List<AurealNetworkElement> visited) {
        for (Direction direction : Direction.values()) {
            
            BlockEntity otherEntity = accessor.getBlockEntity(blockPos.relative(direction));
            
            if (otherEntity instanceof AurealNetworkElement networkBlockEntity && !visited.contains(networkBlockEntity)) {
                visited.add(networkBlockEntity);
                seekIntegrity(blockPos.relative(direction), accessor, visited);
            }
            
        }
    }
    
    public void addChild(AurealNetworkElement element) {
        if (element.hasAetherNetwork())
            element.getAetherNetwork().removeChild(element);
        
        element.setAetherNetwork(this);
        networkElements.add(element);
        if (element instanceof AurealConsumer consumer) {
            networkConsumers.add(consumer);
            updateConsumption();
        }
        if (element instanceof AurealProducer producer) {
            networkProducers.add(producer);
            updateProduction();
        }
        if (element instanceof AurealAccumulator consumer) {
            networkAccumulators.add(consumer);
            updateAccumulation();
        }
    }
    
    public void removeChild(AurealNetworkElement element) {
        networkElements.remove(element);
        if (element instanceof AurealConsumer consumer) {
            networkConsumers.remove(consumer);
            updateConsumption();
        }
        if (element instanceof AurealProducer producer) {
            networkProducers.remove(producer);
            updateProduction();
        }
        if (element instanceof AurealAccumulator accumulator) {
            networkAccumulators.remove(accumulator);
            updateAccumulation();
        }
    }
    
    public List<AurealNetworkElement> getNetworkElements() {
        return networkElements;
    }
    
    public LevelAccessor getLevel() {
        return level;
    }
    
}
