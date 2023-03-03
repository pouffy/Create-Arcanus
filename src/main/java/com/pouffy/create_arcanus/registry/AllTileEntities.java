package com.pouffy.create_arcanus.registry;

import com.pouffy.create_arcanus.CreateArcanus;
import com.pouffy.create_arcanus.content.contraptions.components.converter.ConverterInstance;
import com.pouffy.create_arcanus.content.contraptions.components.converter.ConverterRenderer;
import com.pouffy.create_arcanus.content.contraptions.components.converter.ConverterTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.content.contraptions.relays.encased.ShaftRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import static com.pouffy.create_arcanus.CreateArcanus.BASE_CREATIVE_TAB;

public class AllTileEntities {
    public static void register(){}

    private static final CreateRegistrate REGISTRATE = CreateArcanus
            .registrate()
            .creativeModeTab(
                    () -> BASE_CREATIVE_TAB
            );
    /////////

    //Glass Encased Shafts
    public static final BlockEntityEntry<KineticTileEntity> ARCANUS_ENCASED_SHAFT = REGISTRATE
            .tileEntity("arcanus_encased_shaft", KineticTileEntity::new)
            .instance(()-> ShaftInstance::new, false)
            .validBlocks(AllBlocks.EDELWOOD_ENCASED_SHAFT)
            .renderer(()-> ShaftRenderer::new)
            .register();
    public static final BlockEntityEntry<ConverterTileEntity> CONVERTER_TILE_ENTITY = REGISTRATE
            .tileEntity("kinetic_electrolyzer", ConverterTileEntity::new)
            .instance(() -> ConverterInstance::new, false)
            .validBlocks(AllBlocks.CONVERTER)
            .renderer(() -> ConverterRenderer::new)
            .register();
}
