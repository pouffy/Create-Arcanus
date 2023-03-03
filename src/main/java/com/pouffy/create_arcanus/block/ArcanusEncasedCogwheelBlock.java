package com.pouffy.create_arcanus.block;

import com.pouffy.create_arcanus.registry.AllBlocks;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.content.contraptions.relays.elementary.SimpleKineticTileEntity;
import com.simibubi.create.content.contraptions.relays.encased.EncasedCogwheelBlock;
import com.simibubi.create.content.schematics.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.ItemRequirement;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ArcanusEncasedCogwheelBlock  extends RotatedPillarKineticBlock implements ICogWheel, ITE<SimpleKineticTileEntity>, ISpecialBlockItemRequirement {
    public static final BooleanProperty TOP_SHAFT = BooleanProperty.create("top_shaft");
    public static final BooleanProperty BOTTOM_SHAFT = BooleanProperty.create("bottom_shaft");
    boolean isLarge;
    private BlockEntry<ArcanusCasingBlock> casing;

    public static ArcanusEncasedCogwheelBlock edelwood(boolean large, BlockBehaviour.Properties properties) {
        return new ArcanusEncasedCogwheelBlock(large, properties, AllBlocks.EDELWOOD_CASING);
    }

    public ArcanusEncasedCogwheelBlock(boolean large, BlockBehaviour.Properties properties, BlockEntry<ArcanusCasingBlock> casing) {
        super(properties);
        this.isLarge = large;
        this.casing = casing;
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(TOP_SHAFT, false)).setValue(BOTTOM_SHAFT, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(new Property[]{TOP_SHAFT, BOTTOM_SHAFT}));
    }

    public void fillItemCategory(CreativeModeTab pTab, NonNullList<ItemStack> pItems) {
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult) {
            return ((BlockHitResult)target).getDirection().getAxis() != this.getRotationAxis(state) ? (this.isLarge ? com.simibubi.create.AllBlocks.LARGE_COGWHEEL.asStack() : com.simibubi.create.AllBlocks.COGWHEEL.asStack()) : this.getCasing().asStack();
        } else {
            return super.getCloneItemStack(state, target, world, pos, player);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placedOn = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (ICogWheel.isSmallCog(placedOn)) {
            stateForPlacement = (BlockState)stateForPlacement.setValue(AXIS, ((IRotate)placedOn.getBlock()).getRotationAxis(placedOn));
        }

        return stateForPlacement;
    }

    public BlockEntry<ArcanusCasingBlock> getCasing() {
        return this.casing;
    }

    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return pState.getBlock() == pAdjacentBlockState.getBlock() && pState.getValue(AXIS) == pAdjacentBlockState.getValue(AXIS);
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getClickedFace().getAxis() != state.getValue(AXIS)) {
            return super.onWrenched(state, context);
        } else {
            Level level = context.getLevel();
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockPos pos = context.getClickedPos();
                KineticTileEntity.switchToBlockState(level, pos, (BlockState)state.cycle(context.getClickedFace().getAxisDirection() == Direction.AxisDirection.POSITIVE ? TOP_SHAFT : BOTTOM_SHAFT));
                this.playRotateSound(level, pos);
                return InteractionResult.SUCCESS;
            }
        }
    }

    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(state));
            KineticTileEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), (BlockState)(this.isLarge ? com.simibubi.create.AllBlocks.LARGE_COGWHEEL : com.simibubi.create.AllBlocks.COGWHEEL).getDefaultState().setValue(AXIS, (Direction.Axis)state.getValue(AXIS)));
            return InteractionResult.SUCCESS;
        }
    }

    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS) && (Boolean)state.getValue(face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? TOP_SHAFT : BOTTOM_SHAFT);
    }

    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        if (newState.getBlock() instanceof EncasedCogwheelBlock && oldState.getBlock() instanceof EncasedCogwheelBlock) {
            if (newState.getValue(TOP_SHAFT) != oldState.getValue(TOP_SHAFT)) {
                return false;
            }

            if (newState.getValue(BOTTOM_SHAFT) != oldState.getValue(BOTTOM_SHAFT)) {
                return false;
            }
        }

        return super.areStatesKineticallyEquivalent(oldState, newState);
    }

    public boolean isSmallCog() {
        return !this.isLarge;
    }

    public boolean isLargeCog() {
        return this.isLarge;
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return CogWheelBlock.isValidCogwheelPosition(ICogWheel.isLargeCog(state), worldIn, pos, (Direction.Axis)state.getValue(AXIS));
    }

    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis)state.getValue(AXIS);
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        return ItemRequirement.of(this.isLarge ? com.simibubi.create.AllBlocks.LARGE_COGWHEEL.getDefaultState() : com.simibubi.create.AllBlocks.COGWHEEL.getDefaultState(), te);
    }

    public Class<SimpleKineticTileEntity> getTileEntityClass() {
        return SimpleKineticTileEntity.class;
    }

    public BlockEntityType<? extends SimpleKineticTileEntity> getTileEntityType() {
        return this.isLarge ? (BlockEntityType) AllTileEntities.ENCASED_LARGE_COGWHEEL.get() : (BlockEntityType)AllTileEntities.ENCASED_COGWHEEL.get();
    }
}
