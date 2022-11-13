package com.pouffy.create_arcanus.client;

import com.pouffy.create_arcanus.registry.AllBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {

        ItemBlockRenderTypes.setRenderLayer(AllBlocks.CONVERTER.get(), RenderType.cutout());
    }
}
