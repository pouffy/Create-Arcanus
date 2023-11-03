package com.pouffydev.create_arcanus;

import com.jozufozu.flywheel.core.PartialModel;
import com.pouffydev.create_arcanus.content.aureal.battery.BatteryBlock;
import com.pouffydev.create_arcanus.content.aureal.util.AurealTransportBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;

import java.util.*;

public class CAPartialModels {
    public static final PartialModel
            CABLE_CASING = block("cable/casing"),
            CONVERTER_HEAD = block("converter/head"),
            DARKSTONE_COGWHEEL = block("darkstone_cogwheel_shaftless"),
            SPECTRAL_GOGGLES = block("spectral_goggles"),
    
    ARCANE_COGWHEEL = block("arcane_cogwheel_shaftless");
    
    public static Map<BatteryBlock.Shape, PartialModel> BATTERY_TOP = new HashMap<>();
    public static Map<BatteryBlock.Shape, PartialModel> BATTERY_BOTTOM = new HashMap<>();
    
    public static Map<BatteryBlock.Shape, PartialModel> CREATIVE_BATTERY_TOP = new HashMap<>();
    public static Map<BatteryBlock.Shape, PartialModel> CREATIVE_BATTERY_BOTTOM = new HashMap<>();
    
    public static List<Map<BatteryBlock.Shape, PartialModel>> BATTERY_BLOCK_CHARGE_SHAPES = new ArrayList<>();
    public static Map<BatteryBlock.Shape, PartialModel> CREATIVE_BATTERY_BLOCK_SHAPES = new HashMap<>();
    
    static {
        for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
            BATTERY_TOP.put(shape, block("battery/end/top_" +
                    shape.getSerializedName().toLowerCase()));
            BATTERY_BOTTOM.put(shape, block("battery/end/bottom_" +
                    shape.getSerializedName().toLowerCase()));
            
            CREATIVE_BATTERY_TOP.put(shape, block("battery/end/top_" +
                    shape.getSerializedName().toLowerCase() + "_creative"));
            CREATIVE_BATTERY_BOTTOM.put(shape, block("battery/end/bottom_" +
                    shape.getSerializedName().toLowerCase() + "_creative"));
            
            CREATIVE_BATTERY_BLOCK_SHAPES.put(shape, block("battery/" +
                    shape.getSerializedName().toLowerCase() + "_creative"));
        }
        
        for (int i = 0; i < 9; i++) {
            Map<BatteryBlock.Shape, PartialModel> entry = new HashMap<>();
            
            for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
                entry.put(shape, block("battery/" +
                        shape.getSerializedName().toLowerCase() + "_" + i));
            }
            
            BATTERY_BLOCK_CHARGE_SHAPES.add(entry);
        }
    }
    
    public static final Map<AurealTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> CABLE_ATTACHMENTS =
            new EnumMap<>(AurealTransportBehaviour.AttachmentTypes.ComponentPartials.class);
    
    static {
        for (AurealTransportBehaviour.AttachmentTypes.ComponentPartials type : AurealTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
//            if (!type.hasModel())
//                continue;
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("cable/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            CABLE_ATTACHMENTS.put(type, map);
        }
    }
    
    public CAPartialModels() {
    }
    
    private static PartialModel block(String path) {
        return new PartialModel(CreateArcanus.asResource("block/" + path));
    }
    
    public static void init() {
        // init static fields
    }
}
