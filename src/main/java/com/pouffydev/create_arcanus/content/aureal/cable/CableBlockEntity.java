package com.pouffydev.create_arcanus.content.aureal.cable;

import com.pouffydev.create_arcanus.content.aureal.util.AurealNetworkElement;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CableBlockEntity extends AurealNetworkElement implements ITransformableBlockEntity {
    
    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
//        behaviours.add(new BracketedTileEntityBehaviour(this, this::canHaveBracket));
    }
    
    @Override
    public void transform(StructureTransform transform) {
        BracketedBlockEntityBehaviour bracketBehaviour = getBehaviour(BracketedBlockEntityBehaviour.TYPE);
        if (bracketBehaviour != null) {
            bracketBehaviour.transformBracket(transform);
        }
    }
    
    private boolean canHaveBracket(BlockState state) {
        return !(state.getBlock() instanceof EncasedCableBlock);
    }
}
