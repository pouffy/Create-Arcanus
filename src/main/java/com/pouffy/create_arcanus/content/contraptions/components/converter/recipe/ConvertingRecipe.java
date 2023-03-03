package com.pouffy.create_arcanus.content.contraptions.components.converter.recipe;

import com.pouffy.create_arcanus.registry.RecipeTypes;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import net.minecraft.world.item.crafting.RecipeType;

public class ConvertingRecipe extends BasinRecipe {
    public ConvertingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(RecipeTypes.CONVERSION, params);
    }
}
