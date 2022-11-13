package com.pouffy.create_arcanus.registry;

import com.pouffy.create_arcanus.CreateArcanus;
import com.pouffy.create_arcanus.item.DarkSoulQuartzItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.pouffy.create_arcanus.CreateArcanus.REGISTRATE;

public class AllTablets {
    private static final CreateRegistrate REGISTRATE = CreateArcanus.registrate()
            .creativeModeTab(() -> CreateArcanus.BASE_CREATIVE_TAB);

    public static final ItemEntry<Item> BLANK_TABLET = REGISTRATE.item("blank_tablet", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<Item> ALCHEMIC_TABLET = REGISTRATE.item("alchemic_tablet", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static final ItemEntry<Item> ARCANE_TABLET = REGISTRATE.item("arcane_tablet", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .register();
    public static void register() {}
}
