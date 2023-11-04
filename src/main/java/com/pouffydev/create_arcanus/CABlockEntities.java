package com.pouffydev.create_arcanus;

import com.pouffydev.create_arcanus.content.aureal.battery.BatteryBlockEntity;
import com.pouffydev.create_arcanus.content.aureal.battery.BatteryRenderer;
import com.pouffydev.create_arcanus.content.aureal.battery.CreativeBatteryBlockEntity;
import com.pouffydev.create_arcanus.content.aureal.cable.CableBlockEntity;
import com.pouffydev.create_arcanus.content.runes.activator.ActivatorBlockEntity;
import com.pouffydev.create_arcanus.content.runes.activator.ActivatorRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.pouffydev.create_arcanus.CreateArcanus.registrate;

public class CABlockEntities {
    
    public static final BlockEntityEntry<CableBlockEntity> aurealCable = registrate
            .blockEntity("cable", CableBlockEntity::new)
            .validBlocks(CABlocks.aurealCable)
            .register();
    
    public static final BlockEntityEntry<CableBlockEntity> encasedAurealCable = registrate
            .blockEntity("encased_cable", CableBlockEntity::new)
            .validBlocks(CABlocks.encasedAurealCable)
            .register();
    
    public static final BlockEntityEntry<BatteryBlockEntity> battery = registrate
            .blockEntity("aureal_battery", BatteryBlockEntity::new)
            .validBlocks(CABlocks.aurealBattery)
            .renderer(() -> BatteryRenderer::new)
            .register();
    
    public static final BlockEntityEntry<CreativeBatteryBlockEntity> creativeBattery = registrate
            .blockEntity("creative_aureal_battery", CreativeBatteryBlockEntity::new)
            .validBlocks(CABlocks.creativeAurealBattery)
            .renderer(() -> BatteryRenderer::new)
            .register();
    
    public static final BlockEntityEntry<ActivatorBlockEntity> activator = registrate
            .blockEntity("activator", ActivatorBlockEntity::new)
            .validBlocks(CABlocks.activator)
            .renderer(() -> ActivatorRenderer::new)
            .register();
    
    public static void register() {}
}
