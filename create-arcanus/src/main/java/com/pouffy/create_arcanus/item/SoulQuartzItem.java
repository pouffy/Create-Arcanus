package com.pouffy.create_arcanus.item;

import com.pouffy.create_arcanus.CreateArcanus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SoulQuartzItem extends MagikFloatyThingItem {
    public SoulQuartzItem(Item.Properties properties) {
        super(properties.tab(CreateArcanus.BASE_CREATIVE_TAB));
    }

    public boolean isFoil(ItemStack stack) {
        return false;
    }

    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        super.onCreated(entity, persistentData);
        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.25, 0.0));
    }
}
