package com.pouffy.create_arcanus.foundation;

import com.pouffy.create_arcanus.CreateArcanus;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArcanusLang {
    public ArcanusLang() {
    }

    public static TranslatableComponent translateDirect(String key, Object... args) {
        return new TranslatableComponent("create_arcanus." + key, resolveBuilders(args));
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static String nonPluralId(String name) {
        String asId = asId(name);
        return asId.endsWith("s") ? asId.substring(0, asId.length() - 1) : asId;
    }

    public static List<Component> translatedOptions(String prefix, String... keys) {
        List<Component> result = new ArrayList(keys.length);
        String[] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String key = var3[var5];
            result.add(translate((prefix != null ? prefix + "." : "") + key).component());
        }

        return result;
    }

    public static Component empty() {
        return TextComponent.EMPTY;
    }

    public static ArcanusLangBuilder builder() {
        return new ArcanusLangBuilder("create_arcanus");
    }

    public static ArcanusLangBuilder builder(String namespace) {
        return new ArcanusLangBuilder(namespace);
    }

    public static ArcanusLangBuilder blockName(BlockState state) {
        return builder().add(state.getBlock().getName());
    }

    public static ArcanusLangBuilder itemName(ItemStack stack) {
        return builder().add(stack.getHoverName().copy());
    }

    public static ArcanusLangBuilder fluidName(FluidStack stack) {
        return builder().add(stack.getDisplayName().copy());
    }

    public static ArcanusLangBuilder number(double d) {
        return builder().text(LangNumberFormat.format(d));
    }

    public static ArcanusLangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }

    public static ArcanusLangBuilder text(String text) {
        return builder().text(text);
    }

    public static Object[] resolveBuilders(Object[] args) {
        for(int i = 0; i < args.length; ++i) {
            Object var3 = args[i];
            if (var3 instanceof LangBuilder cb) {
                args[i] = cb.component();
            }
        }

        return args;
    }
}
