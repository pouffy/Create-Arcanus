package com.pouffy.create_arcanus.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.pouffy.create_arcanus.CreateArcanus;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Map;

public class BlockPartials {
    public static final PartialModel
            CONVERTER_HEAD = block("converter/head"),
            DARKSTONE_COGWHEEL = block("darkstone_cogwheel_shaftless");



    public BlockPartials() {
    }

    private static PartialModel block(String path) {
        return new PartialModel(CreateArcanus.asResource("block/" + path));
    }

    public static void register() {
        // init static fields
    }

}
