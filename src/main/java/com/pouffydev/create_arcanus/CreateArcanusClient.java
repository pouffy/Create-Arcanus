package com.pouffydev.create_arcanus;

import com.simibubi.create.CreateClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateArcanusClient {
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CreateArcanusClient::clientInit);
    }
    
    public static void clientInit(final FMLClientSetupEvent event) {
        CAPartialModels.init();
    }
}
