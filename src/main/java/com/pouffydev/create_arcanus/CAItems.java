package com.pouffydev.create_arcanus;

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
    
    
    
    // Load this class
    
    public static void register() {}
}
