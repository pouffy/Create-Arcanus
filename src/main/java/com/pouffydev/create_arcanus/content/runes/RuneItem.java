package com.pouffydev.create_arcanus.content.runes;

import com.pouffydev.create_arcanus.content.runes.types.AreaEffectRune;
import com.pouffydev.create_arcanus.content.runes.types.EntityAttractRune;
import com.pouffydev.create_arcanus.content.runes.types.RuneTypes;
import com.pouffydev.create_arcanus.foundation.CALang;
import com.simibubi.create.foundation.utility.LangBuilder;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RuneItem extends Item {
    private static final ChatFormatting DESCRIPTION_FORMAT;
    private static final Component RUNE_COMPONENT;
    static {
        DESCRIPTION_FORMAT = ChatFormatting.BLUE;
        RUNE_COMPONENT = Component.translatable("item.create_arcanus.rune").withStyle(ChatFormatting.GOLD);
    }
    public RuneItem(Properties pProperties) {
        super(pProperties);
    }
    private void expandTooltip(boolean advanced, List<Component> tooltip, Component addition) {
        if (advanced) {
            tooltip.add(tooltip.size() - 1, addition);
        } else {
            tooltip.add(addition);
        }
        
    }
    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        RuneTypes type = RuneTypes.getFromRuneItem(stack.getItem().getDefaultInstance());
        if (type == null) {
            return;
        }
        tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).add(CALang.translateRune("item.create_arcanus.rune").withStyle(ChatFormatting.GOLD)).component());
        tooltip.add(CALang.builder().text(Strings.repeat(' ', 2)).add(CALang.translateRune("tooltip.create_arcanus.rune_type." + type.getRuneType())).style(ChatFormatting.UNDERLINE).style(ChatFormatting.AQUA).component());
        
        if (type.getRuneEffect() instanceof AreaEffectRune) {
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).component());
            String mobEffect = ((AreaEffectRune) type.getRuneEffect()).getEffect().getDescriptionId();
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).add(CALang.translateRune("tooltip.create_arcanus.rune_radius", ((AreaEffectRune) type.getRuneEffect()).getRadius())).style(DESCRIPTION_FORMAT).component());
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).add(CALang.translateRune("tooltip.create_arcanus.rune_mob_effect")).component());
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 2)).add(CALang.translateDescriptionID(mobEffect).style(ChatFormatting.GOLD)).component());
            if (((AreaEffectRune) type.getRuneEffect()).isOnlyPlayers()) {
                tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).component());
                tooltip.add(CALang.builder().add(CALang.translateRune("tooltip.create_arcanus.rune_only_players")).style(DESCRIPTION_FORMAT).component());
            }
        }
        if (type.getRuneEffect() instanceof EntityAttractRune) {
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).component());
            String entity = ((EntityAttractRune) type.getRuneEffect()).getEntity().getDescriptionId();
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).add(CALang.translateRune("tooltip.create_arcanus.rune_radius", ((EntityAttractRune) type.getRuneEffect()).getRadius())).style(DESCRIPTION_FORMAT).component());
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 1)).add(CALang.translateRune("tooltip.create_arcanus.rune_affects_entity")).component());
            tooltip.add(CALang.builder().text(Strings.repeat(' ', 2)).add(CALang.translateDescriptionID(entity).style(ChatFormatting.GOLD)).component());
        }
    }
}
