package com.pouffydev.create_arcanus.foundation;

import com.pouffydev.create_arcanus.CreateArcanus;
import com.pouffydev.create_arcanus.content.aureal.battery.BatteryBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CABlockModelGenerator extends BlockModelProvider {
    public CABlockModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), CreateArcanus.ID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        
        for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
            for (int i = 0; i < 9; i++) {
                getBuilder("block/battery/" + shape.getSerializedName().toLowerCase() + "_" + i)
                        .parent(getExistingFile(CreateArcanus.asResource("block/battery/" + shape.getSerializedName().toLowerCase())))
                        .texture("0", CreateArcanus.asResource("block/battery/" + i));
            }
            getBuilder("block/battery/" + shape.getSerializedName().toLowerCase() + "_creative")
                    .parent(getExistingFile(CreateArcanus.asResource("block/battery/" + shape.getSerializedName().toLowerCase())))
                    .texture("0", CreateArcanus.asResource("block/battery/creative"));
        }
        
        for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
            getBuilder("block/battery/end/bottom_" + shape.getSerializedName().toLowerCase() + "_creative")
                    .parent(getExistingFile(CreateArcanus.asResource("block/battery/end/bottom_" + shape.getSerializedName().toLowerCase())))
                    .texture("1", CreateArcanus.asResource("block/battery/end_creative"));
            getBuilder("block/battery/end/top_" + shape.getSerializedName().toLowerCase() + "_creative")
                    .parent(getExistingFile(CreateArcanus.asResource("block/battery/end/top_" + shape.getSerializedName().toLowerCase())))
                    .texture("1", CreateArcanus.asResource("block/battery/end_creative"));
        }
    }
}
