package com.pouffydev.create_arcanus.content.runes.activator;

import com.stal111.forbidden_arcanus.common.block.ArcaneCrystalObeliskBlock;
import com.stal111.forbidden_arcanus.common.block.entity.forge.MagicCircle;
import com.stal111.forbidden_arcanus.common.block.properties.ObeliskPart;
import com.stal111.forbidden_arcanus.common.network.NetworkHandler;
import com.stal111.forbidden_arcanus.common.network.clientbound.UpdatePedestalPacket;
import com.stal111.forbidden_arcanus.core.init.ModBlockEntities;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActivatorBlockEntity extends BlockEntity {
    private static final int DEFAULT_ITEM_HEIGHT = 120;
    private ItemStack stack;
    private final float hoverStart;
    private int ticksExisted;
    private int itemHeight;
    private final ChangedCallback onChanged;
    private MagicCircle magicCircle;
    public boolean hasMagicCircle() {
        return this.magicCircle != null;
    }
    public MagicCircle getMagicCircle() {
        return this.magicCircle;
    }
    
    public void setMagicCircle(@NotNull MagicCircle magicCircle) {
        this.magicCircle = magicCircle;
    }
    
    public void removeMagicCircle() {
        this.magicCircle = null;
    }
    public ActivatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PEDESTAL.get(), pos, state);
        this.stack = ItemStack.EMPTY;
        this.itemHeight = 120;
        this.onChanged = (level, stack) -> {
            this.hasObelisks(level, this.getBlockPos());
        };
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0);
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, ActivatorBlockEntity blockEntity) {
        ++blockEntity.ticksExisted;
        
        if (blockEntity.hasMagicCircle()) {
            blockEntity.magicCircle.tick();
        }
    }
    
    public void setStack(ItemStack stack) {
        this.stack = stack;
        this.setChanged();
    }
    
    public void setStackAndSync(ItemStack stack) {
        this.setStackAndSync(stack, true);
    }
    public void setStackAndSync(ItemStack stack, boolean runOnChanged) {
        this.stack = stack;
        Level var4 = this.level;
        if (var4 instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToTrackingChunk(serverLevel.getChunkAt(this.getBlockPos()), new UpdatePedestalPacket(this.getBlockPos(), stack, this.itemHeight));
            if (runOnChanged) {
                this.onChanged.run(serverLevel, stack);
            }
        }
        
        this.setChanged();
    }
    
    public ItemStack getStack() {
        return this.stack;
    }
    
    public boolean hasStack() {
        return !this.stack.isEmpty();
    }
    
    public void clearStack(Level level) {
        this.clearStack(level, true);
    }
    
    public void clearStack(Level level, boolean runOnChanged) {
        this.setItemHeight(120);
        this.setStackAndSync(ItemStack.EMPTY, runOnChanged);
    }
    
    public float getItemHover(float partialTicks) {
        return ((float)this.ticksExisted + partialTicks) / 20.0F + this.hoverStart;
    }
    
    public int getItemHeight() {
        return this.itemHeight;
    }
    
    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }
    
    private boolean hasObelisks(ServerLevel level, BlockPos pos) {
        return this.obeliskCount(level, pos) >= 4;
    }
    private int obeliskCount(ServerLevel level, BlockPos pos) {
        int obeliskCount = 0;
        ObeliskPart obeliskBottom = ObeliskPart.LOWER;
        BlockPos eastObelisk = pos.east(3);
        BlockPos westObelisk = pos.west(3);
        BlockPos northObelisk = pos.north(3);
        BlockPos southObelisk = pos.south(3);
        if (level.getBlockState(eastObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(eastObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                ++obeliskCount;
            }
        } else if (level.getBlockState(westObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(westObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                ++obeliskCount;
            }
        } else if (level.getBlockState(northObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(northObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                ++obeliskCount;
            }
        } else if (level.getBlockState(southObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(southObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                ++obeliskCount;
            }
        }
        return obeliskCount;
    }
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Stack")) {
            this.stack = ItemStack.of(compound.getCompound("Stack"));
            this.itemHeight = compound.getInt("ItemHeight");
        }
        
    }
    
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.stack != ItemStack.EMPTY) {
            compound.put("Stack", this.stack.save(new CompoundTag()));
            compound.putInt("ItemHeight", this.itemHeight);
        }
        
    }
    
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
    
    public AABB getRenderBoundingBox() {
        return (new AABB(this.getBlockPos())).expandTowards(0.0, 1.0, 0.0);
    }
    
    @FunctionalInterface
    private interface ChangedCallback {
        void run(ServerLevel var1, ItemStack var2);
    }
}
