package com.pouffy.create_arcanus.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.encased.AbstractEncasedShaftBlock;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ArcanusEncasedShaftBlock extends AbstractEncasedShaftBlock implements ITE<KineticTileEntity>, ISpecialBlockItemRequirement {
    private BlockEntry<ArcanusCasingBlock> casing;

    public static ArcanusEncasedShaftBlock edelwood(BlockBehaviour.Properties properties) {
        return new ArcanusEncasedShaftBlock(properties, com.pouffy.create_arcanus.registry.AllBlocks.EDELWOOD_CASING);
    }

    protected ArcanusEncasedShaftBlock(Properties properties, BlockEntry<ArcanusCasingBlock> casing) {
        super(properties);
        this.casing = casing;
    }

    public BlockEntry<ArcanusCasingBlock> getCasing() {
        return this.casing;
    }

    public void fillItemCategory(CreativeModeTab pTab, NonNullList<ItemStack> pItems) {
    }

    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(state));
            KineticTileEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), (BlockState) com.simibubi.create.AllBlocks.SHAFT.getDefaultState().setValue(AXIS, (Direction.Axis)state.getValue(AXIS)));
            return InteractionResult.SUCCESS;
        }
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult) {
            return ((BlockHitResult)target).getDirection().getAxis() == this.getRotationAxis(state) ? com.simibubi.create.AllBlocks.SHAFT.asStack() : this.getCasing().asStack();
        } else {
            return super.getCloneItemStack(state, target, world, pos, player);
        }
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        return ItemRequirement.of(AllBlocks.SHAFT.getDefaultState(), te);
    }

    public Class<KineticTileEntity> getTileEntityClass() {
        return KineticTileEntity.class;
    }

    public BlockEntityType<? extends KineticTileEntity> getTileEntityType() {
        return (BlockEntityType)AllTileEntities.ENCASED_SHAFT.get();
    }
}
