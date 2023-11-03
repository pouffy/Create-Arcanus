package com.pouffydev.create_arcanus.content.aureal.util;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.stal111.forbidden_arcanus.common.aureal.capability.AurealImpl;
import com.stal111.forbidden_arcanus.common.aureal.capability.IAureal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public abstract class AurealTransportBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<AurealTransportBehaviour> TYPE = new BehaviourType<>();
    
    protected AurealImpl aureal;
    protected LazyOptional<IAureal> capability;
    
    public AurealTransportBehaviour(SmartBlockEntity te) {
        super(te);
        this.capability = LazyOptional.empty();
    }
    
    public void setStorage() {
        this.resetNetwork();
        aureal = new AurealImpl();
        capability = LazyOptional.of(() -> aureal);
    }

//    @Override
//    public void tick() {
//        super.tick();
//    }
    
    @Override
    public void destroy() {
        super.destroy();
        capability.invalidate();
    }
    
    public void resetNetwork() {
        aureal = null;
        capability.invalidate();
    }
    
    public AurealImpl getHandler() {
        return aureal;
    }
    
    public LazyOptional<IAureal> getCapability() {
        return capability;
    }
    
    public abstract boolean canFluxToward(BlockState state, Direction direction);
    
    public AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        if (!canFluxToward(state, direction))
            return AttachmentTypes.NONE;
        return AttachmentTypes.RIM;
    }
    
    public enum AttachmentTypes {
        NONE,
        NORMAL(ComponentPartials.NORMAL),
        GOLDEN(ComponentPartials.GOLDEN),
        RIM(ComponentPartials.RIM_CONNECTOR, ComponentPartials.RIM),
        PARTIAL_RIM(ComponentPartials.RIM);
        
        public final AttachmentTypes.ComponentPartials[] partials;
        
        AttachmentTypes(AttachmentTypes.ComponentPartials... partials) {
            this.partials = partials;
        }
        
        public AttachmentTypes withoutConnector() {
            if (this == AttachmentTypes.RIM)
                return AttachmentTypes.PARTIAL_RIM;
            return this;
        }
        
        public boolean hasModel() {
            return this != NONE;
        }
        
        public enum ComponentPartials {
            NORMAL, GOLDEN, RIM_CONNECTOR, RIM;
        }
    }
    
    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

}
