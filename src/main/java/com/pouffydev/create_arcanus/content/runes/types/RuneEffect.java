package com.pouffydev.create_arcanus.content.runes.types;

import com.pouffydev.create_arcanus.foundation.CALang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class RuneEffect {
    public static final ChatFormatting DESCRIPTION_FORMAT;
    static {
        DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    }
    public RuneEffect() {}
    
    public LangBuilder getEffectDescription() {
        return CALang.builder().add(CALang.translate("tooltip.create_arcanus.rune_effect")).style(ChatFormatting.AQUA);
    }
    
    public void applyEffect(Level pLevel, BlockPos pPos) {
    
    }
}
