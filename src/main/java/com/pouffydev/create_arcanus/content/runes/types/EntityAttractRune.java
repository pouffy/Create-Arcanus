package com.pouffydev.create_arcanus.content.runes.types;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityAttractRune extends RuneEffect {
    private final double radius;
    private final EntityType<?> entity;
    public EntityAttractRune(EntityType<?> entity, double radius) {
        this.entity = entity;
        this.radius = radius;
    }
    
    public EntityType<?> getEntity() {
        return entity;
    }
    
    public double getRadius() {
        return radius;
    }
    public void applyEffect(Level pLevel, BlockPos pPos) {
        AABB aabb = (new AABB(pPos)).inflate(radius).expandTowards(radius, radius + 10, radius);
        List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, aabb);
        for (LivingEntity entity : list) {
            if (entity.getType() == this.entity) {
                entity.moveRelative(-1, new Vec3(pPos.getX(), pPos.getY(), pPos.getZ()));
            }
        }
    }
}
