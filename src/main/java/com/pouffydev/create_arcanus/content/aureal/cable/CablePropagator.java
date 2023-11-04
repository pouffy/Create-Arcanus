package com.pouffydev.create_arcanus.content.aureal.cable;

import com.pouffydev.create_arcanus.CABlocks;
import com.pouffydev.create_arcanus.CACapabilities;
import com.pouffydev.create_arcanus.content.aureal.util.AurealTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.stal111.forbidden_arcanus.common.aureal.capability.IAureal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CablePropagator {
    public static void propagateChangedCable(LevelAccessor world, BlockPos cablePos, BlockState cableState) {
        List<BlockPos> frontier = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Set<Pair<BlockEntity, Direction>> holders = new HashSet<>();
        Set<Pair<AurealTransportBehaviour, BlockEntity>> terminals = new HashSet<>();
        
        frontier.add(cablePos);
        
        // Visit all connected cables to update their network
        while (!frontier.isEmpty()) {
            BlockPos currentPos = frontier.remove(0);
            if (visited.contains(currentPos))
                continue;
            visited.add(currentPos);
            
            BlockState currentState = currentPos.equals(cablePos) ? cableState : world.getBlockState(currentPos);
            AurealTransportBehaviour cable = getCable(world, currentPos);
            if (cable == null)
                continue;
            
            for (Direction direction : getCableConnections(currentState, cable)) {
                BlockPos target = currentPos.relative(direction);
                if (world instanceof Level l && !l.isLoaded(target))
                    continue;
                
                BlockEntity tileEntity = world.getBlockEntity(target);
                BlockState targetState = world.getBlockState(target);
                if (tileEntity != null
                        && !(tileEntity instanceof CableBlockEntity)
                        && tileEntity.getCapability(CACapabilities.aureal, direction.getOpposite())
                        .isPresent()) { // if neighbour is a cap provider
                    holders.add(Pair.of(tileEntity, direction.getOpposite()));
                    terminals.add(Pair.of(cable, tileEntity));
                    continue;
                }
                if (visited.contains(target))
                    continue;
                AurealTransportBehaviour targetCable = getCable(world, target);
                if (targetCable == null)
                    continue;
                if (targetCable.canFluxToward(targetState, direction.getOpposite()))
                    frontier.add(target);
            }
        }
        
        terminals.forEach(pair -> {
            AurealTransportBehaviour cable = pair.getFirst();
            BlockEntity holder = pair.getSecond();
            List<IAureal> cells = new ArrayList<>();
            holders.forEach(holderPair -> {
                if (holderPair.getFirst() != holder)
                    holderPair.getFirst().getCapability(CACapabilities.aureal, holderPair.getSecond())
                            .ifPresent(cells::add);
            });
            cable.setStorage();
        });
        
    }
    
    public static void resetAffectedCableNetworks(Level world, BlockPos start) {
        List<BlockPos> frontier = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        frontier.add(start);
        
        while (!frontier.isEmpty()) {
            BlockPos pos = frontier.remove(0);
            if (visited.contains(pos))
                continue;
            visited.add(pos);
            
            AurealTransportBehaviour cable = getCable(world, pos);
            if (cable == null)
                continue;
            cable.resetNetwork();
            
            for (Direction d : Iterate.directions) {
//                if (pos.equals(start) && d != side)
//                    continue;
                BlockPos target = pos.relative(d);
                if (visited.contains(target))
                    continue;
                if (cable.canFluxToward(world.getBlockState(target),d))
                    frontier.add(target);
            }
        }
    }
    
    public static Direction validateNeighbourChange(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos, boolean isMoving) {
        if (world.isClientSide)
            return null;
        if (world.getBlockState(neighborPos).getBlock() instanceof AxisPipeBlock)
            return null;
        if (getStraightCableAxis(state) == null && !CABlocks.encasedAurealCable.has(state))
            return null;
        for (Direction d : Iterate.directions) {
            if (!pos.relative(d)
                    .equals(neighborPos))
                continue;
            return d;
        }
        return null;
    }
    
    public static AurealTransportBehaviour getCable(BlockGetter reader, BlockPos pos) {
        return BlockEntityBehaviour.get(reader, pos, AurealTransportBehaviour.TYPE);
        
    }
    
    public static boolean isOpenEnd(BlockGetter reader, BlockPos pos, Direction side) {
        BlockPos connectedPos = pos.relative(side);
        BlockState connectedState = reader.getBlockState(connectedPos);
        AurealTransportBehaviour cable = CablePropagator.getCable(reader, connectedPos);
        if (cable != null && cable.canFluxToward(connectedState, side.getOpposite()))
            return false;
        if (hasAetherCapability(reader, connectedPos, side.getOpposite()))
            return false;
        return true;
    }
    
    public static List<Direction> getCableConnections(BlockState state, AurealTransportBehaviour cable) {
        List<Direction> list = new ArrayList<>();
        for (Direction d : Iterate.directions)
            if (cable.canFluxToward(state, d))
                list.add(d);
        return list;
    }
    
    public static boolean hasAetherCapability(BlockGetter world, BlockPos pos, Direction side) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        return tileEntity != null && tileEntity.getCapability(CACapabilities.aureal, side)
                .isPresent();
    }
    
    @Nullable
    public static Direction.Axis getStraightCableAxis(BlockState state) {
        if (state.getBlock() instanceof AxisPipeBlock)
            return state.getValue(AxisPipeBlock.AXIS);
        if (!CableBlock.isCable(state))
            return null;
        Direction.Axis axisFound = null;
        int connections = 0;
        for (Direction.Axis axis : Iterate.axes) {
            Direction d1 = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
            Direction d2 = Direction.get(Direction.AxisDirection.POSITIVE, axis);
            boolean openAt1 = CableBlock.isOpenAt(state, d1);
            boolean openAt2 = CableBlock.isOpenAt(state, d2);
            if (openAt1)
                connections++;
            if (openAt2)
                connections++;
            if (openAt1 && openAt2)
                if (axisFound != null)
                    return null;
                else
                    axisFound = axis;
        }
        return connections == 2 ? axisFound : null;
    }
    
}