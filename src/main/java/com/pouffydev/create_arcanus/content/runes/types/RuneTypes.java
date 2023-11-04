package com.pouffydev.create_arcanus.content.runes.types;

import com.pouffydev.create_arcanus.CAItems;
import com.stal111.forbidden_arcanus.core.init.ModEntities;
import com.stal111.forbidden_arcanus.core.init.ModMobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

public enum RuneTypes {
    spectralVision(new ItemStack(CAItems.spectralVisionAreaEffectRune.get()), new AreaEffectRune(ModMobEffects.SPECTRAL_VISION.get(), 5, true), "area_effect"),
    attractSoul(new ItemStack(CAItems.soulAttractionRune.get()), new EntityAttractRune(ModEntities.LOST_SOUL.get(), 50), "entity_attract"),
    attractSlime(new ItemStack(CAItems.slimeAttractionRune.get()), new EntityAttractRune(EntityType.SLIME, 50), "entity_attract"),
    attractMagmaCube(new ItemStack(CAItems.magmaCubeAttractionRune.get()), new EntityAttractRune(EntityType.MAGMA_CUBE, 50), "entity_attract")
    ;
    
    private final ItemStack runeItem;
    private final RuneEffect runeEffect;
    private final String runeType;
    private RuneTypes(ItemStack runeItem, RuneEffect runeEffect, String runeType) {
        this.runeItem = runeItem;
        this.runeEffect = runeEffect;
        this.runeType = runeType;
    }
    
    public ItemStack getRuneItem() {
        return this.runeItem;
    }
    
    public RuneEffect getRuneEffect() {
        return this.runeEffect;
    }
    
    public String getRuneType() {
        return this.runeType;
    }
    
    
    
    public static RuneTypes getFromRuneItem(ItemStack runeItem) {
        for (RuneTypes type : RuneTypes.values()) {
            if (type.getRuneItem().getItem() == runeItem.getItem()) {
                return type;
            }
        }
        
        return null;
    }
}
