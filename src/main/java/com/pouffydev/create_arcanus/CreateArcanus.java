package com.pouffydev.create_arcanus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.pouffydev.create_arcanus.foundation.CACreativeModeTabs;
import com.pouffydev.create_arcanus.foundation.CADatagen;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.simibubi.create.foundation.item.TooltipHelper.styleFromColor;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateArcanus.ID)
public class CreateArcanus
{
    public static final String ID = "create_arcanus";
    public static final String NAME = "Create: Arcanus";
    public static final String VERSION = "0.0.1";
    
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    
    public static final CreateRegistrate registrate = CreateRegistrate.create(ID);
    
    static {
        registrate.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, new TooltipHelper.Palette(styleFromColor(0x666dd3), styleFromColor(0x9fe2fd)))
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public CreateArcanus()
    {
        onCtor();
    }
    public static void onCtor() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        
        registrate.registerEventListeners(modEventBus);
        CACreativeModeTabs.register(modEventBus);
        CAItems.register();
        
        modEventBus.addListener(EventPriority.LOWEST, CADatagen::gatherData);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateArcanusClient.onCtorClient(modEventBus, forgeEventBus));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
}
