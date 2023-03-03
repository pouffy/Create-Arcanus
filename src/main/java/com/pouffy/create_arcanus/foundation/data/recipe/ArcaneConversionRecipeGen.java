package com.pouffy.create_arcanus.foundation.data.recipe;

import com.pouffy.create_arcanus.registry.RecipeTypes;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;


public class ArcaneConversionRecipeGen extends ArcanusRecipeGen {

    CreateRecipeProvider.GeneratedRecipe

    CHOCOLATE = create("chocolate", b -> b.require(Tags.Fluids.MILK, 250)
            .require(Items.SUGAR)
            .require(Items.COCOA_BEANS)
            .output(AllFluids.CHOCOLATE.get(), 250)
            .requiresHeat(HeatCondition.HEATED))
            ;

    public ArcaneConversionRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected RecipeTypes getRecipeType() {
        return RecipeTypes.CONVERSION;
    }

}
