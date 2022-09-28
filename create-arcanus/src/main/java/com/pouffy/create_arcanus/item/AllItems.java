package com.pouffy.create_arcanus.item;

import com.pouffy.create_arcanus.CreateArcanus;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AllItems {
    private static final CreateRegistrate REGISTRATE = CreateArcanus.registrate()
            .creativeModeTab(() -> CreateArcanus.BASE_CREATIVE_TAB);

    public static final ItemEntry<Item> DARKSTONE_ALLOY = REGISTRATE.item("darkstone_alloy", Item::new)
            .properties(p->p.tab(CreateArcanus.BASE_CREATIVE_TAB))
            .lang("Darkstone Alloy")
            .register();
    public static void register() {}
}
