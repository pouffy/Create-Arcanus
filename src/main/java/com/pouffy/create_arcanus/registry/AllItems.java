package com.pouffy.create_arcanus.registry;

import com.pouffy.create_arcanus.CreateArcanus;
import com.pouffy.create_arcanus.content.contraptions.spectral_goggles.SpectralGogglesItem;
import com.pouffy.create_arcanus.content.contraptions.spectral_goggles.SpectralGogglesModel;
import com.pouffy.create_arcanus.item.DarkSoulQuartzItem;
import com.pouffy.create_arcanus.item.SoulQuartzItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.simibubi.create.AllTags.AllItemTags.PLATES;
import static com.simibubi.create.AllTags.forgeItemTag;

@SuppressWarnings("unused")
public class AllItems {
    private static final CreateRegistrate REGISTRATE = CreateArcanus.registrate()
            .creativeModeTab(() -> CreateArcanus.BASE_CREATIVE_TAB);

    public static final ItemEntry<Item> DARKSTONE_ALLOY = REGISTRATE.item("darkstone_alloy", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<Item> POLISHED_DARK_RUNE = REGISTRATE.item("polished_dark_rune", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<Item> POLISHED_RUNE = REGISTRATE.item("polished_rune", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<SoulQuartzItem> SOUL_QUARTZ = REGISTRATE.item("soul_quartz", SoulQuartzItem::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<DarkSoulQuartzItem> DARK_SOUL_QUARTZ = REGISTRATE.item("dark_soul_quartz", DarkSoulQuartzItem::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<SoulQuartzItem> POLISHED_SOUL_QUARTZ = REGISTRATE.item("polished_soul_quartz", SoulQuartzItem::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<DarkSoulQuartzItem> POLISHED_DARK_SOUL_QUARTZ = REGISTRATE.item("polished_dark_soul_quartz", DarkSoulQuartzItem::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<SpectralGogglesItem> SPECTRAL_GOGGLES = REGISTRATE.item("spectral_goggles", SpectralGogglesItem::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB).stacksTo(1))
            .onRegister(CreateRegistrate.itemModel(() -> SpectralGogglesModel::new))
            .lang("Spectral Goggles")
            .register();

    public static final ItemEntry<Item> ARCANE_GOLD_SHEET =
            taggedIngredient("arcane_gold_sheet", forgeItemTag("plates/arcane_gold"), PLATES.tag);
    public static void register() {}

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new).tag(tags).register();
    }
}
