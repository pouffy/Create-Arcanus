package com.pouffy.create_arcanus.block;

import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ArcanusCasingBlock  extends Block implements IWrenchable {
    private boolean visible;

    public static CasingBlock deprecated(BlockBehaviour.Properties p_i48440_1_) {
        return new CasingBlock(p_i48440_1_, false);
    }

    public ArcanusCasingBlock(BlockBehaviour.Properties p_i48440_1_) {
        this(p_i48440_1_, true);
    }

    public ArcanusCasingBlock(BlockBehaviour.Properties p_i48440_1_, boolean visible) {
        super(p_i48440_1_);
        this.visible = visible;
    }

    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (this.visible) {
            super.fillItemCategory(pCategory, pItems);
        }

    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.FAIL;
    }
}
