package com.pouffy.create_arcanus.content.contraptions.components.converter.recipe;

import com.pouffy.create_arcanus.CreateArcanus;
import net.minecraft.world.item.crafting.RecipeType;

public class ConvertingRecipeType implements RecipeType<ConvertingRecipe> {
    @Override
    public String toString() {
        return CreateArcanus.MODID+":conversion";
    }
}
