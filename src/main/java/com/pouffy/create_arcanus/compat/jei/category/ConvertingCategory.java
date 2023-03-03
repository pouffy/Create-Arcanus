package com.pouffy.create_arcanus.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffy.create_arcanus.compat.jei.category.animations.AnimatedConverter;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.world.level.ItemLike;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ConvertingCategory extends BasinCategory {
    private final AnimatedConverter converter = new AnimatedConverter();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
    ConvertingCategory.ConversionType type;
    

    public static ConvertingCategory standard() {
        return new ConvertingCategory(ConvertingCategory.ConversionType.CONVERSION, (ItemLike)AllBlocks.BASIN.get(), 103);
    }
    

    public ConvertingCategory(ConvertingCategory.ConversionType type, ItemLike secondaryItem, int height) {
        super(type != ConvertingCategory.ConversionType.DARK_CONVERSION, doubleItemIcon((ItemLike) com.pouffy.create_arcanus.registry.AllBlocks.CONVERTER.get(), secondaryItem), emptyBackground(177, height));
        this.type = type;
    }

    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, matrixStack, mouseX, mouseY);
        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) {
            this.heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(matrixStack, this.getBackground().getWidth() / 2 + 3, 55);
        }

        this.converter.draw(matrixStack, this.getBackground().getWidth() / 2 + 3, 34);
    }

    static enum ConversionType {
        DARK_CONVERSION,
        CONVERSION;

        private ConversionType() {
        }
    }
}
