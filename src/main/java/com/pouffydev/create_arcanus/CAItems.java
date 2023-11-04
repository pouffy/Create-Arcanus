package com.pouffydev.create_arcanus;

import com.pouffydev.create_arcanus.content.runes.RuneItem;
import com.pouffydev.create_arcanus.content.spectral_goggles.SpectralGogglesItem;
import com.pouffydev.create_arcanus.content.spectral_goggles.SpectralGogglesModel;
import com.pouffydev.create_arcanus.foundation.CACreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import static com.pouffydev.create_arcanus.CreateArcanus.registrate;

public class CAItems {
    static {
        registrate.setCreativeTab(CACreativeModeTabs.BASE_CREATIVE_TAB);
    }
    public static final ItemEntry<SpectralGogglesItem> spectralGoggles = registrate.item("spectral_goggles", SpectralGogglesItem::new)
            .properties(p->p.stacksTo(1))
            .onRegister(CreateRegistrate.itemModel(() -> SpectralGogglesModel::new))
            .lang("Spectral Goggles")
            .register();
    
    public static final ItemEntry<RuneItem> soulAttractionRune = registrate.item("rune_entity_attract_lost_soul", RuneItem::new)
            .properties(p->p.stacksTo(1))
            .lang("Rune")
            .register();
    public static final ItemEntry<RuneItem> slimeAttractionRune = registrate.item("rune_entity_attract_slime", RuneItem::new)
            .properties(p->p.stacksTo(1))
            .lang("Rune")
            .register();
    public static final ItemEntry<RuneItem> magmaCubeAttractionRune = registrate.item("rune_entity_attract_magma_cube", RuneItem::new)
            .properties(p->p.stacksTo(1))
            .lang("Rune")
            .register();
    public static final ItemEntry<RuneItem> spectralVisionAreaEffectRune = registrate.item("rune_area_effect_spectral_vision", RuneItem::new)
            .properties(p->p.stacksTo(1))
            .lang("Rune")
            .register();
    
    
    
    // Load this class
    
    public static void register() {}
}
