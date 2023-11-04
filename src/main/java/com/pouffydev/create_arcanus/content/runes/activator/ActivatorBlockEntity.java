package com.pouffydev.create_arcanus.content.runes.activator;

import com.pouffydev.create_arcanus.CABlockEntities;
import com.pouffydev.create_arcanus.content.runes.RuneItem;
import com.pouffydev.create_arcanus.content.runes.RuneMagicCircleType;
import com.pouffydev.create_arcanus.content.runes.types.RuneTypes;
import com.stal111.forbidden_arcanus.common.block.ArcaneCrystalObeliskBlock;
import com.stal111.forbidden_arcanus.common.block.HephaestusForgeBlock;
import com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeBlockEntity;
import com.stal111.forbidden_arcanus.common.block.entity.forge.MagicCircle;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;
import com.stal111.forbidden_arcanus.common.block.properties.ObeliskPart;
import com.stal111.forbidden_arcanus.common.network.NetworkHandler;
import com.stal111.forbidden_arcanus.common.network.clientbound.UpdatePedestalPacket;
import com.stal111.forbidden_arcanus.core.init.ModBlockEntities;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActivatorBlockEntity extends BlockEntity {
    private static final ResourceLocation DEFAULT_INNER_TEXTURE = new ResourceLocation("forbidden_arcanus", "textures/effect/magic_circle/inner/union.png");
    private static final ResourceLocation DEFAULT_OUTER_TEXTURE = new ResourceLocation("forbidden_arcanus", "textures/effect/magic_circle/outer/pure.png");
    private static final int DEFAULT_ITEM_HEIGHT = 120;
    private static ItemStack stack;
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
    public ActivatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        stack = ItemStack.EMPTY;
        this.itemHeight = 120;
        this.onChanged = (level, stack) -> {
            this.hasObelisks(level, pos);
        };
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0);
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, ActivatorBlockEntity blockEntity) {
        ++blockEntity.ticksExisted;
        
        if (blockEntity.hasMagicCircle()) {
            blockEntity.magicCircle.tick();
        }
    }
    public static void serverTick(Level level, BlockPos pos, BlockState state, ActivatorBlockEntity blockEntity) {
        if (blockEntity.hasObelisks((ServerLevel)level, pos) && blockEntity.hasStack() && stack.getItem() instanceof RuneItem) {
            blockEntity.hasMagicCircle();
            blockEntity.magicCircle = new MagicCircle(level, pos, blockEntity.circleInnerTexture(), blockEntity.circleOuterTexture());
            level.setBlockAndUpdate(pos, state.setValue(ActivatorBlock.ACTIVE, true));
            runRune(level, pos);
        }
        if (blockEntity.ticksExisted >= 400) {
            blockEntity.corruptObelisks(level, pos);
            blockEntity.ticksExisted = 0;
        }
        blockEntity.updateObelisk((ServerLevel)level, pos);
    }
    
    public static void runRune(Level level, BlockPos pos) {
        RuneTypes type = RuneTypes.getFromRuneItem(stack.getItem().getDefaultInstance());
        if (stack.getItem() instanceof RuneItem && type != null) {
        type.getRuneEffect().applyEffect(level, pos);
        }
    }
    public void corruptObelisks(Level level, BlockPos pos) {
        ObeliskPart obeliskBottom = ObeliskPart.LOWER;
        //Get obelisks from 3 blocks away from the pedestal
        BlockPos eastObelisk = pos.east(3);
        BlockPos westObelisk = pos.west(3);
        BlockPos northObelisk = pos.north(3);
        BlockPos southObelisk = pos.south(3);
        if (level.getBlockState(eastObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(eastObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(eastObelisk, ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.LOWER));
                level.setBlockAndUpdate(eastObelisk.above(), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.MIDDLE));
                level.setBlockAndUpdate(eastObelisk.above(2), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.UPPER));
            }
        }
        if (level.getBlockState(westObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(westObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(westObelisk, ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.LOWER));
                level.setBlockAndUpdate(westObelisk.above(), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.MIDDLE));
                level.setBlockAndUpdate(westObelisk.above(2), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.UPPER));
            }
        }
        if (level.getBlockState(northObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(northObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(northObelisk, ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.LOWER));
                level.setBlockAndUpdate(northObelisk.above(), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.MIDDLE));
                level.setBlockAndUpdate(northObelisk.above(2), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.UPPER));
            }
        }
        if (level.getBlockState(southObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(southObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(southObelisk, ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.LOWER));
                level.setBlockAndUpdate(southObelisk.above(), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.MIDDLE));
                level.setBlockAndUpdate(southObelisk.above(2), ModBlocks.CORRUPTED_ARCANE_CRYSTAL_OBELISK.get().defaultBlockState().setValue(ArcaneCrystalObeliskBlock.PART, ObeliskPart.UPPER));
            }
        }
    }
    public void setStack(ItemStack stack) {
        ActivatorBlockEntity.stack = stack;
        this.setChanged();
    }
    
    public void setStackAndSync(ItemStack stack) {
        this.setStackAndSync(stack, true);
    }
    public void setStackAndSync(ItemStack stack, boolean runOnChanged) {
        ActivatorBlockEntity.stack = stack;
        Level var4 = this.level;
        if (var4 instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToTrackingChunk(serverLevel.getChunkAt(this.getBlockPos()), new UpdatePedestalPacket(this.getBlockPos(), stack, this.itemHeight));
            if (runOnChanged) {
                this.onChanged.run(serverLevel, stack);
            }
        }
        
        this.setChanged();
    }
    
    public ResourceLocation circleInnerTexture() {
        RuneMagicCircleType type = RuneMagicCircleType.getFromRuneItem(stack.getItem().getDefaultInstance());
        return type != null ? type.getInnerTexture() : DEFAULT_INNER_TEXTURE;
    }
    public ResourceLocation circleOuterTexture() {
        RuneMagicCircleType type = RuneMagicCircleType.getFromRuneItem(stack.getItem().getDefaultInstance());
        return type != null ? type.getOuterTexture() : DEFAULT_OUTER_TEXTURE;
    }
    
    public ItemStack getStack() {
        return stack;
    }
    
    public boolean hasStack() {
        return !stack.isEmpty();
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
        return level.getBlockState(pos).getValue(ActivatorBlock.eastObelisk) && level.getBlockState(pos).getValue(ActivatorBlock.westObelisk) && level.getBlockState(pos).getValue(ActivatorBlock.northObelisk) && level.getBlockState(pos).getValue(ActivatorBlock.southObelisk);
    }
    private void updateObelisk(ServerLevel level, BlockPos pos) {
        ObeliskPart obeliskBottom = ObeliskPart.LOWER;
        BlockPos eastObelisk = pos.east(3);
        BlockPos westObelisk = pos.west(3);
        BlockPos northObelisk = pos.north(3);
        BlockPos southObelisk = pos.south(3);
        if (level.getBlockState(eastObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(eastObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ActivatorBlock.eastObelisk, true));
            }
        }
        if (level.getBlockState(westObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(westObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ActivatorBlock.westObelisk, true));
            }
        }
        if (level.getBlockState(northObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(northObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ActivatorBlock.northObelisk, true));
            }
        }
        if (level.getBlockState(southObelisk).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get()) {
            if (level.getBlockState(southObelisk).getValue(ArcaneCrystalObeliskBlock.PART) == obeliskBottom) {
                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ActivatorBlock.southObelisk, true));
            }
        }
    }
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Stack")) {
            stack = ItemStack.of(compound.getCompound("Stack"));
            this.itemHeight = compound.getInt("ItemHeight");
        }
        
    }
    
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        if (stack != ItemStack.EMPTY) {
            compound.put("Stack", stack.save(new CompoundTag()));
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
