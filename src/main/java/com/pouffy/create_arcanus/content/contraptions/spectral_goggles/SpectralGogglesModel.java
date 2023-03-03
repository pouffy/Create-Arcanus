package com.pouffy.create_arcanus.content.contraptions.spectral_goggles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffy.create_arcanus.registry.BlockPartials;
import com.simibubi.create.AllBlockPartials;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

public class SpectralGogglesModel extends BakedModelWrapper<BakedModel> {

    public SpectralGogglesModel(BakedModel template) {
        super(template);
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat) {
        if (cameraTransformType == ItemTransforms.TransformType.HEAD)
            return BlockPartials.SPECTRAL_GOGGLES.get()
                    .handlePerspective(cameraTransformType, mat);
        return super.handlePerspective(cameraTransformType, mat);
    }

}
