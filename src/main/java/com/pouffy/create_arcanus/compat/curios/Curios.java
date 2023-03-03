package com.pouffy.create_arcanus.compat.curios;

import com.pouffy.create_arcanus.registry.AllItems;
import com.simibubi.create.compat.curios.CuriosRenderers;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class Curios {
    public static void init(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(Curios::onInterModEnqueue);
        modEventBus.addListener(Curios::onClientSetup);

        GogglesItem.addIsWearingPredicate(player -> player.getCapability(CuriosCapability.INVENTORY).map(handler -> {
            ICurioStacksHandler stacksHandler = handler.getCurios().get("head");
            if (stacksHandler != null) {
                return AllItems.SPECTRAL_GOGGLES.isIn(stacksHandler.getStacks().getStackInSlot(0));
            }
            return false;
        }).orElse(false));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(CuriosRenderers::onLayerRegister));
    }

    private static void onInterModEnqueue(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
    }

    private static void onClientSetup(final FMLClientSetupEvent event) {
        CuriosRenderers.register();
    }
}
