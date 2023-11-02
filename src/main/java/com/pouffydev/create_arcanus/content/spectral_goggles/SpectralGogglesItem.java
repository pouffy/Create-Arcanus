package com.pouffydev.create_arcanus.content.spectral_goggles;

import com.pouffydev.create_arcanus.CAItems;
import com.stal111.forbidden_arcanus.core.init.ModMobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SpectralGogglesItem extends Item {
    
    
    private static final List<Predicate<Player>> IS_WEARING_PREDICATES = new ArrayList<>();
    
    static {
        addIsWearingPredicate(player -> CAItems.spectralGoggles.isIn(player.getItemBySlot(EquipmentSlot.HEAD)));
    }
    
    public void inventoryTick(ItemStack stack, Level level, LivingEntity livingEntity, int itemSlot, boolean isSelected) {
        if (stack.is(livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem())){
            livingEntity.addEffect(new MobEffectInstance(ModMobEffects.SPECTRAL_VISION.get(), 40, 0, false, false, true));
        }
        super.inventoryTick(stack, level, livingEntity, itemSlot, isSelected);
    }
    
    public SpectralGogglesItem(Item.Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }
    
    @Override
    public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
        if (entity == null)
            return;
        entity.addEffect(new MobEffectInstance(ModMobEffects.SPECTRAL_VISION.get(), 40, 0, false, false, true));
    }
    
    
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
    
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = playerIn.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            playerIn.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
        }
    }
    
    public static boolean isWearingGoggles(Player player) {
        for (Predicate<Player> predicate : IS_WEARING_PREDICATES) {
            if (predicate.test(player)) {
                return true;
            }
        }
        return false;
    }
    
    public static void addIsWearingPredicate(Predicate<Player> predicate) {
        IS_WEARING_PREDICATES.add(predicate);
    }
    
    
}
