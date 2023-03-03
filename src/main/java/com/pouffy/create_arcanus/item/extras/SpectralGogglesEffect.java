package com.pouffy.create_arcanus.item.extras;

import com.stal111.forbidden_arcanus.core.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SpectralGogglesEffect {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof LivingEntity _entity)
            _entity.addEffect(new MobEffectInstance(ModEffects.SPECTRAL_VISION.get(), 40, 0, false, false, true));
    }

}
