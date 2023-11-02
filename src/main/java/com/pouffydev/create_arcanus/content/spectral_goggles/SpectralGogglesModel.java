package com.pouffydev.create_arcanus.content.spectral_goggles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffydev.create_arcanus.CAPartialModels;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public class SpectralGogglesModel extends BakedModelWrapper<BakedModel> {
    
    public SpectralGogglesModel(BakedModel template) {
        super(template);
    }
    
    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraItemDisplayContext, PoseStack mat, boolean leftHanded) {
        if (cameraItemDisplayContext == ItemDisplayContext.HEAD)
            return CAPartialModels.SPECTRAL_GOGGLES.get()
                    .applyTransform(cameraItemDisplayContext, mat, leftHanded);
        return super.applyTransform(cameraItemDisplayContext, mat, leftHanded);
    }
    
}
