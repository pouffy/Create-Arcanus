package com.pouffydev.create_arcanus.content.aureal.battery;

import com.pouffydev.create_arcanus.content.aureal.util.AurealAccumulator;
import com.pouffydev.create_arcanus.content.aureal.util.IMultiBlockEntityContainerAureal;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public class BatteryBlockEntity extends AurealAccumulator implements IMultiBlockEntityContainerAureal {
    
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected int width;
    protected int height;
    
    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    /**Called usually when loaded, do not call if {@link #isController()} is true.*/
    protected boolean refreshTotalStored = false;
    
    /**Stored Aether in the entire structure, for {@link BatteryBlockEntity#getDisplayedChargeLevel()}*/
    private int totalStored = 0;
    
    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        updateConnectivity = false;
        width = 1;
        height = 1;
    }
    
    @Override
    public int getMaxStorage() {
        return 50000;
    }
    
    @Override
    public int getStoredAureal() {
        return super.getStoredAureal();
    }
    
    public void updateTotalStored() {
        totalStored = 0;
        for (int x = 0; x < width + 1; x++) {
            for (int z = 0; z < width + 1; z++) {
                for (int y = 0; y < height + 1; y++) {
                    BlockEntity blockEntity = level.getBlockEntity(getBlockPos().offset(x, y, z));
                    if (blockEntity instanceof BatteryBlockEntity batteryBlockEntity) {
                        totalStored += batteryBlockEntity.getStoredAureal();
                    }
                }
            }
        }
    }
    
    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide())
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
        updateTotalStored();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }
    
    @Override
    public void tick() {
        if (refreshTotalStored) {
            updateTotalStored();
        }
        
        super.tick();
        
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }
        
        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }
        
        if (updateConnectivity)
            updateConnectivity();
    }
    
    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }
    
    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }
    
    public void setShapes() {
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    
                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!BatteryBlock.isBattery(blockState))
                        continue;
                    
                    BatteryBlock.Shape shape = BatteryBlock.Shape.SINGLE;
                    // SIZE 2: Every battery is a corner
                    if (width == 2)
                        shape = xOffset == 0 ? zOffset == 0 ? BatteryBlock.Shape.NW : BatteryBlock.Shape.SW
                                : zOffset == 0 ? BatteryBlock.Shape.NE : BatteryBlock.Shape.SE;
                    
                    level.setBlock(pos, blockState.setValue(BatteryBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
    }
    
    /**Return a value from 0-8 inclusive for how much charge should be displayed*/
    public int getDisplayedChargeLevel() {
        BatteryBlockEntity blockEntity = getControllerBE();
        return (blockEntity == null ? 0 : blockEntity.getDisplayedChargeLevel(getBlockPos()));
    }
    
    /**Gets the displayed charge for a bound battery at an offset*/
    public int getDisplayedChargeLevel(BlockPos other) {
        int yOffset = other.getY() - getBlockPos().getY();
        
        float displayedStored = (float) totalStored / (getMaxStorage() * width * width);
        
        displayedStored -= yOffset;
        
        return (int) Math.max(Math.min(Math.ceil(displayedStored * 8), 8), 0);
    }
    
    @Override
    protected void onContentChange(int change) {
        if (isController())
            totalStored += change;
        else if (getControllerBE() != null)
            getControllerBE().totalStored += change;
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        Lang.text("Battery charge: " + getStoredAureal()).forGoggles(tooltip);
        Lang.text("Total battery charge: " + totalStored).forGoggles(tooltip);
        Lang.text("displayed: " + getDisplayedChargeLevel()).forGoggles(tooltip);
        Lang.text("Total battery charge: " + getControllerBE().totalStored).forGoggles(tooltip);
        return true;
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        
        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;
        
        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;
        
        if (compound.contains("LastKnownPos"))
            lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        if (compound.contains("Controller"))
            controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));
        
        if (isController()) {
            width = compound.getInt("Size");
            height = compound.getInt("Height");
        }
        
        if (!clientPacket)
            return;
        
        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            invalidateRenderBoundingBox();
        }
        
        if (isController()) {
            refreshTotalStored = true;
        }
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.putInt("Size", width);
            compound.putInt("Height", height);
        }
        
        super.write(compound, clientPacket);
    }
    
    public int getTotalBatterySize() {
        return width * width * height;
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
    
    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public BatteryBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity tileEntity = level.getBlockEntity(controller);
        if (tileEntity instanceof BatteryBlockEntity)
            return (BatteryBlockEntity) tileEntity;
        return null;
    }
    
    @Override
    public boolean isController() {
        return controller == null
                || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY()
                && worldPosition.getZ() == controller.getZ();
    }
    
    @Override
    public void setController(BlockPos controller) {
        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        getControllerBE().updateTotalStored();
        setChanged();
        sendData();
    }
    
    @Override
    public void removeController(boolean keepContents) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        controller = null;
        width = 1;
        height = 1;
        
        BlockState state = getBlockState();
        if (BatteryBlock.isBattery(state)) {
            state = state.setValue(BatteryBlock.BOTTOM, true);
            state = state.setValue(BatteryBlock.TOP, true);
            state = state.setValue(BatteryBlock.SHAPE, BatteryBlock.Shape.SINGLE);
            getLevel().setBlock(worldPosition, state, 22);
        }
        
        setChanged();
        sendData();
    }
    
    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }
    
    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }
    
    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (BatteryBlock.isBattery(state)) { // safety
            state = state.setValue(BatteryBlock.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(BatteryBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);
        }
        if (isController()) {
//            applyBatterySize(getTotalBatterySize());
            setShapes();
            updateTotalStored();
        }
//        refreshCapability();
        setChanged();
        sendData();
    }
    
    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }
    
    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return getMaxHeight(width);
        return getMaxWidth();
    }
    
    @Override
    public int getMaxWidth() {
        return 2;
    }
    
    public int getMaxHeight(int width) {
        return 1 + 2 * width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public void setHeight(int height) {
        this.height = height;
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public void setWidth(int width) {
        this.width = width;
    }
    
}
