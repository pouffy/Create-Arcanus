package com.pouffydev.create_arcanus.content.runes.activator;

import com.pouffydev.create_arcanus.content.runes.RuneItem;
import com.stal111.forbidden_arcanus.common.block.PedestalBlock;
import com.stal111.forbidden_arcanus.core.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.valhelsia.valhelsia_core.api.common.helper.VoxelShapeHelper;
import net.valhelsia.valhelsia_core.api.common.util.ItemStackUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActivatorBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    private static final VoxelShape SHAPE = VoxelShapeHelper.combineAll(new VoxelShape[]{box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0), box(3.0, 4.0, 3.0, 13.0, 6.0, 13.0), box(4.0, 6.0, 4.0, 12.0, 11.0, 12.0), box(2.0, 11.0, 2.0, 14.0, 14.0, 14.0)});
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public ActivatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(ACTIVE, false));
    }
    
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }
    
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ActivatorBlockEntity(pos, state);
    }
    
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
    }
    
    @Nonnull
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if ((Boolean)state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        
        return state;
    }
    
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        BlockEntity var9 = level.getBlockEntity(pos);
        if (!(var9 instanceof ActivatorBlockEntity blockEntity)) {
            return InteractionResult.PASS;
        } else {
                if (stack.isEmpty() || blockEntity.hasStack()) {
                    return InteractionResult.PASS;
                }
                if (stack.getItem() instanceof RuneItem) {
                    blockEntity.setStackAndSync(stack.copy().split(1));
                    ItemStackUtils.shrinkStack(player, stack);
                }
                
                player.displayClientMessage(Component.translatable("create_arcanus.rune.invalid", stack.getHoverName()), true);
                
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
    }
    
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!(newState.getBlock() instanceof PedestalBlock)) {
            BlockEntity var7 = level.getBlockEntity(pos);
            if (var7 instanceof ActivatorBlockEntity) {
                ActivatorBlockEntity blockEntity = (ActivatorBlockEntity)var7;
                level.addFreshEntity(new ItemEntity(level, (double)pos.getX() + 0.5, (double)pos.getY() + 1.1, (double)pos.getZ() + 0.5, blockEntity.getStack()));
                blockEntity.setStackAndSync(ItemStack.EMPTY);
            }
            
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? BaseEntityBlock.createTickerHelper(blockEntityType, (BlockEntityType) ModBlockEntities.PEDESTAL.get(), ActivatorBlockEntity::clientTick) : null;
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, ACTIVE);
    }
    
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    
    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
