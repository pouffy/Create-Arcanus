package com.pouffy.create_arcanus.foundation;

import com.simibubi.create.foundation.utility.Lang;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ArcanusLangBuilder {
    String namespace;
    MutableComponent component;

    public ArcanusLangBuilder(String namespace) {
        this.namespace = namespace;
    }

    public ArcanusLangBuilder space() {
        return this.text(" ");
    }

    public ArcanusLangBuilder newLine() {
        return this.text("\n");
    }

    public ArcanusLangBuilder translate(String langKey, Object... args) {
        return this.add((MutableComponent)(new TranslatableComponent(this.namespace + "." + langKey, ArcanusLang.resolveBuilders(args))));
    }

    public ArcanusLangBuilder text(String literalText) {
        return this.add((MutableComponent)(new TextComponent(literalText)));
    }

    public ArcanusLangBuilder text(ChatFormatting format, String literalText) {
        return this.add((new TextComponent(literalText)).withStyle(format));
    }

    public ArcanusLangBuilder text(int color, String literalText) {
        return this.add((new TextComponent(literalText)).withStyle((s) -> {
            return s.withColor(color);
        }));
    }

    public ArcanusLangBuilder add(ArcanusLangBuilder otherBuilder) {
        return this.add(otherBuilder.component());
    }

    public ArcanusLangBuilder add(MutableComponent customComponent) {
        this.component = this.component == null ? customComponent : this.component.append(customComponent);
        return this;
    }

    public ArcanusLangBuilder style(ChatFormatting format) {
        this.assertComponent();
        this.component = this.component.withStyle(format);
        return this;
    }

    public ArcanusLangBuilder color(int color) {
        this.assertComponent();
        this.component = this.component.withStyle((s) -> {
            return s.withColor(color);
        });
        return this;
    }

    public MutableComponent component() {
        this.assertComponent();
        return this.component;
    }

    public String string() {
        return this.component().getString();
    }

    public String json() {
        return Component.Serializer.toJson(this.component());
    }

    public void sendStatus(Player player) {
        player.displayClientMessage(this.component(), true);
    }

    public void sendChat(Player player) {
        player.displayClientMessage(this.component(), false);
    }

    public void addTo(List<? super MutableComponent> tooltip) {
        tooltip.add(this.component());
    }

    public void forGoggles(List<? super MutableComponent> tooltip) {
        this.forGoggles(tooltip, 0);
    }

    public void forGoggles(List<? super MutableComponent> tooltip, int indents) {
        tooltip.add(ArcanusLang.builder().text(Strings.repeat(' ', 4 + indents)).add(this).component());
    }

    private void assertComponent() {
        if (this.component == null) {
            throw new IllegalStateException("No components were added to builder");
        }
    }
}
