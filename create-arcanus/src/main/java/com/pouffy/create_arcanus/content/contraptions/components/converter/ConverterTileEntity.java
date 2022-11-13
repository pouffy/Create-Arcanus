package com.pouffy.create_arcanus.content.contraptions.components.converter;

import com.pouffy.create_arcanus.content.contraptions.components.converter.recipe.ConvertingRecipe;
import com.pouffy.create_arcanus.registry.AllSoundEvents;
import com.simibubi.create.content.contraptions.fluids.FluidFX;
import com.simibubi.create.content.contraptions.fluids.recipe.PotionMixingRecipes;
import com.simibubi.create.content.contraptions.processing.BasinOperatingTileEntity;
import com.simibubi.create.content.contraptions.processing.BasinTileEntity;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ConverterTileEntity extends BasinOperatingTileEntity {
    private static final Object shapelessOrConvertingRecipesKey = new Object();
    public int runningTicks;
    public int processingTicks;
    public boolean running;

    public ConverterTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public float getRenderedHeadOffset(float partialTicks) {
        float offset = 0.0F;
        if (this.running) {
            int localTick;
            float num;
            if (this.runningTicks < 20) {
                localTick = this.runningTicks;
                num = ((float)localTick + partialTicks) / 20.0F;
                num = (2.0F - Mth.cos((float)((double)num * Math.PI))) / 2.0F;
                offset = num - 0.5F;
            } else if (this.runningTicks <= 20) {
                offset = 1.0F;
            } else {
                localTick = 40 - this.runningTicks;
                num = ((float)localTick - partialTicks) / 20.0F;
                num = (2.0F - Mth.cos((float)((double)num * Math.PI))) / 2.0F;
                offset = num - 0.5F;
            }
        }

        return offset + 0.4375F;
    }

    public float getRenderedHeadRotationSpeed(float partialTicks) {
        float speed = this.getSpeed();
        if (this.running) {
            if (this.runningTicks < 15) {
                return speed;
            } else {
                return this.runningTicks <= 20 ? speed * 2.0F : speed;
            }
        } else {
            return speed / 2.0F;
        }
    }

    protected AABB createRenderBoundingBox() {
        return (new AABB(this.worldPosition)).expandTowards(0.0, -1.5, 0.0);
    }

    protected void read(CompoundTag compound, boolean clientPacket) {
        this.running = compound.getBoolean("Running");
        this.runningTicks = compound.getInt("Ticks");
        super.read(compound, clientPacket);
        if (clientPacket && this.hasLevel()) {
            this.getBasin().ifPresent((bte) -> {
                bte.setAreFluidsMoving(this.running && this.runningTicks <= 20);
            });
        }

    }

    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putInt("Ticks", this.runningTicks);
        super.write(compound, clientPacket);
    }

    public void tick() {
        super.tick();
        if (this.runningTicks >= 40) {
            this.running = false;
            this.runningTicks = 0;
            this.basinChecker.scheduleUpdate();
        } else {
            float speed = Math.abs(this.getSpeed());
            if (this.running && this.level != null) {
                if (this.level.isClientSide && this.runningTicks == 20) {
                    this.renderParticles();
                }

                if ((!this.level.isClientSide || this.isVirtual()) && this.runningTicks == 20) {
                    if (this.processingTicks < 0) {
                        float recipeSpeed = 1.0F;
                        if (this.currentRecipe instanceof ProcessingRecipe) {
                            int t = ((ProcessingRecipe)this.currentRecipe).getProcessingDuration();
                            if (t != 0) {
                                recipeSpeed = (float)t / 100.0F;
                            }
                        }

                        this.processingTicks = Mth.clamp(Mth.log2((int)(512.0F / speed)) * Mth.ceil(recipeSpeed * 15.0F) + 1, 1, 512);
                        Optional<BasinTileEntity> basin = this.getBasin();
                        if (basin.isPresent()) {
                            Couple<SmartFluidTankBehaviour> tanks = ((BasinTileEntity)basin.get()).getTanks();
                            if (!((SmartFluidTankBehaviour)tanks.getFirst()).isEmpty() || !((SmartFluidTankBehaviour)tanks.getSecond()).isEmpty()) {
                                this.level.playSound((Player)null, this.worldPosition, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 0.75F, speed < 65.0F ? 0.75F : 1.5F);
                            }
                        }
                    } else {
                        --this.processingTicks;
                        if (this.processingTicks == 0) {
                            ++this.runningTicks;
                            this.processingTicks = -1;
                            this.applyBasinRecipe();
                            this.sendData();
                        }
                    }
                }

                if (this.runningTicks != 20) {
                    ++this.runningTicks;
                }
            }

        }
    }

    public void renderParticles() {
        Optional<BasinTileEntity> basin = this.getBasin();
        if (basin.isPresent() && this.level != null) {
            Iterator var2 = ((BasinTileEntity)basin.get()).getInvs().iterator();

            while(var2.hasNext()) {
                SmartInventory inv = (SmartInventory)var2.next();

                for(int slot = 0; slot < inv.getSlots(); ++slot) {
                    ItemStack stackInSlot = inv.getItem(slot);
                    if (!stackInSlot.isEmpty()) {
                        ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                        this.spillParticle(data);
                    }
                }
            }

            var2 = ((BasinTileEntity)basin.get()).getTanks().iterator();

            while(true) {
                SmartFluidTankBehaviour behaviour;
                do {
                    if (!var2.hasNext()) {
                        return;
                    }

                    behaviour = (SmartFluidTankBehaviour)var2.next();
                } while(behaviour == null);

                SmartFluidTankBehaviour.TankSegment[] var9 = behaviour.getTanks();
                int var10 = var9.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    SmartFluidTankBehaviour.TankSegment tankSegment = var9[var11];
                    if (!tankSegment.isEmpty(0.0F)) {
                        this.spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
                    }
                }
            }
        }
    }

    protected void spillParticle(ParticleOptions data) {
        float angle = this.level.random.nextFloat() * 360.0F;
        Vec3 offset = new Vec3(0.0, 0.0, 0.25);
        offset = VecHelper.rotate(offset, (double)angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, this.getSpeed() > 0.0F ? 25.0 : -25.0, Direction.Axis.Y).add(0.0, 0.25, 0.0);
        Vec3 center = offset.add(VecHelper.getCenterOf(this.worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), this.level.random, 0.0078125F);
        this.level.addParticle(data, center.x, center.y - 1.75, center.z, target.x, target.y, target.z);
    }

    protected List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> matchingRecipes = super.getMatchingRecipes();
        Optional<BasinTileEntity> basin = this.getBasin();
        if (!basin.isPresent()) {
            return matchingRecipes;
        } else {
            IItemHandler availableItems = (IItemHandler)((BasinTileEntity)basin.get()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse((IItemHandler) null);
            if (availableItems == null) {
                return matchingRecipes;
            }
        }
        return matchingRecipes;
    }

    public void startProcessingBasin() {
        if (!this.running || this.runningTicks > 20) {
            super.startProcessingBasin();
            this.running = true;
            this.runningTicks = 0;
        }
    }

    public boolean continueWithPreviousRecipe() {
        this.runningTicks = 20;
        return true;
    }

    protected void onBasinRemoved() {
        if (this.running) {
            this.runningTicks = 40;
            this.running = false;
        }
    }

    protected Object getRecipeCacheKey() {
        return shapelessOrConvertingRecipesKey;
    }

    protected boolean isRunning() {
        return this.running;
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        boolean slow = Math.abs(this.getSpeed()) < 65.0F;
        if (!slow || AnimationTickHolder.getTicks() % 2 != 0) {
            if (this.runningTicks == 20) {
                AllSoundEvents.CONVERTING.playAt(this.level, this.worldPosition, 0.75F, 1.0F, true);
            }

        }
    }
}
