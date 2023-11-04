package com.pouffydev.create_arcanus.content.runes.types;

import com.pouffydev.create_arcanus.foundation.CALang;
import com.simibubi.create.foundation.utility.LangBuilder;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AreaEffectRune extends RuneEffect {
    private final MobEffect effect;
    private final double radius;
    private final boolean onlyPlayers;
    public AreaEffectRune(MobEffect effect, double radius, boolean onlyPlayers) {
        this.effect = effect;
        this.radius = radius;
        this.onlyPlayers = onlyPlayers;
    }
    
    public MobEffect getEffect() {
        return effect;
    }
    
    public double getRadius() {
        return radius;
    }
    
    public boolean isOnlyPlayers() {
        return onlyPlayers;
    }
    
    @Override
    public LangBuilder getEffectDescription() {
        Component mobEffect = effect.getDisplayName();
        CALang.builder()
                .text(Strings.repeat(' ', 1))
                .add(CALang.translate("tooltip.create_arcanus.rune_radius", getRadius()))
                .style(DESCRIPTION_FORMAT);
        CALang.builder()
                .text(Strings.repeat(' ', 1))
                .add(CALang.translate("tooltip.create_arcanus.rune_mob_effect"));
        CALang.builder()
                .text(Strings.repeat(' ', 2))
                .add((LangBuilder) mobEffect).style(ChatFormatting.GOLD);
        if (isOnlyPlayers()) {
            CALang.builder()
                    .add(CALang.translate("tooltip.create_arcanus.rune_only_players"))
                    .style(DESCRIPTION_FORMAT);
        }
        return CALang.builder();
    }
    
    @Override
    public void applyEffect(Level pLevel, BlockPos pPos) {
        //give effect to all entities in radius
        AABB aabb = (new AABB(pPos)).inflate(radius).expandTowards(radius, radius + 5, radius);
        
        if (onlyPlayers) {
            List<Player> list = pLevel.getEntitiesOfClass(Player.class, aabb);
            for(Player entity : list) {
                entity.addEffect(new MobEffectInstance(effect, 40, 1, true, true));
            }
        } else {
            List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity entity : list) {
                entity.addEffect(new MobEffectInstance(effect, 40, 1, true, true));
            }
        }
    }
}
