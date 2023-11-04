package com.pouffydev.create_arcanus.content.runes.activator;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ActivatorBlockItem extends BlockItem {
    public ActivatorBlockItem(ActivatorBlock block, Properties properties) {
        super(block, properties);
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new ActivatorBlockItemRenderer()));
    }
}
