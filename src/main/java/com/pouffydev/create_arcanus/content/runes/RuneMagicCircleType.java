package com.pouffydev.create_arcanus.content.runes;

import com.pouffydev.create_arcanus.CAItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public enum RuneMagicCircleType {
    ATTRACT(new ResourceLocation("forbidden_arcanus", "textures/effect/magic_circle/inner/origin.png"), new ResourceLocation("forbidden_arcanus", "textures/effect/magic_circle/outer/attract.png"), new ItemStack(CAItems.soulAttractionRune.get())),
    
    ;
    
    private final ResourceLocation innerTexture;
    private final ResourceLocation outerTexture;
    private final ItemStack runeItem;
    
    private RuneMagicCircleType(ResourceLocation innerTexture, ResourceLocation outerTexture, ItemStack runeItem) {
        this.innerTexture = innerTexture;
        this.outerTexture = outerTexture;
        this.runeItem = runeItem;
    }
    
    public ResourceLocation getInnerTexture() {
        return this.innerTexture;
    }
    
    public ResourceLocation getOuterTexture() {
        return this.outerTexture;
    }
    
    public ItemStack getRuneItem() {
        return this.runeItem;
    }
    
    public static RuneMagicCircleType getFromRuneItem(ItemStack runeItem) {
        for (RuneMagicCircleType type : RuneMagicCircleType.values()) {
            if (type.getRuneItem().getItem() == runeItem.getItem()) {
                return type;
            }
        }
        
        return null;
    }
}
