package com.pouffydev.create_arcanus;

import com.jozufozu.flywheel.core.PartialModel;

public class CAPartialModels {
    public static final PartialModel
            CONVERTER_HEAD = block("converter/head"),
            DARKSTONE_COGWHEEL = block("darkstone_cogwheel_shaftless"),
            SPECTRAL_GOGGLES = block("spectral_goggles"),
    
    ARCANE_COGWHEEL = block("arcane_cogwheel_shaftless");
    
    public CAPartialModels() {
    }
    
    private static PartialModel block(String path) {
        return new PartialModel(CreateArcanus.asResource("block/" + path));
    }
    
    public static void init() {
        // init static fields
    }
}
