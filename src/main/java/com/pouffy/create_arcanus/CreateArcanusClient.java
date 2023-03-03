package com.pouffy.create_arcanus;

import com.pouffy.create_arcanus.content.contraptions.spectral_goggles.SpectralGoggleOverlayRenderer;
import com.pouffy.create_arcanus.registry.AllBlocks;
import com.pouffy.create_arcanus.registry.BlockPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllStitchedTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import com.simibubi.create.content.contraptions.components.structureMovement.render.SBBContraptionManager;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.curiosities.armor.CopperBacktankArmorLayer;
import com.simibubi.create.content.curiosities.toolbox.ToolboxHandlerClient;
import com.simibubi.create.content.curiosities.tools.BlueprintOverlayRenderer;
import com.simibubi.create.content.logistics.item.LinkedControllerClientHandler;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.ponder.content.PonderIndex;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.ShippedResourcePacks;
import com.stal111.forbidden_arcanus.client.gui.overlay.FlightTimerOverlay;
import com.stal111.forbidden_arcanus.client.gui.overlay.ObsidianSkullOverlay;
import com.stal111.forbidden_arcanus.client.gui.overlay.SanityMeterOverlay;
import com.stal111.forbidden_arcanus.client.gui.screen.HephaestusForgeScreen;
import com.stal111.forbidden_arcanus.client.tooltip.ClientEdelwoodBucketTooltip;
import com.stal111.forbidden_arcanus.client.tooltip.EdelwoodBucketTooltip;
import com.stal111.forbidden_arcanus.common.block.properties.ModBlockStateProperties;
import com.stal111.forbidden_arcanus.common.item.BloodTestTubeItem;
import com.stal111.forbidden_arcanus.common.item.SpectralEyeAmuletItem;
import com.stal111.forbidden_arcanus.common.item.UtremJarItem;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import com.stal111.forbidden_arcanus.core.init.ModItems;
import com.stal111.forbidden_arcanus.core.init.other.ModContainers;
import com.stal111.forbidden_arcanus.core.init.other.ModWoodTypes;
import com.stal111.forbidden_arcanus.util.FullbrightBakedModel;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CreateArcanusClient {
    private final List<Triple<Block, StatePropertiesPredicate, Function<BakedModel, BakedModel>>> bakedModelOverrideRegistry = new ArrayList();

    public CreateArcanusClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        //Have to do this here because flywheel lied about the init timing ;(
        //Things won't work if you try init PartialModels in FMLClientSetupEvent
        BlockPartials.register();
        modEventBus.register(this);
        modEventBus.addListener(CreateArcanusClient::clientInit);

    }
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {

    }

        public static void clientInit(FMLClientSetupEvent event) {
        registerOverlays();
    }

    private static void registerOverlays() {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create: Arcanus Spectral Goggle Information", SpectralGoggleOverlayRenderer.OVERLAY);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModelRegistry();
        FullbrightBakedModel.invalidateCache();
        Iterator var3 = this.bakedModelOverrideRegistry.iterator();

        while(var3.hasNext()) {
            Triple<Block, StatePropertiesPredicate, Function<BakedModel, BakedModel>> triple = (Triple)var3.next();
            ((Block)triple.getLeft()).getStateDefinition().getPossibleStates().stream().filter((state) -> {
                return ((StatePropertiesPredicate)triple.getMiddle()).matches(state);
            }).map(BlockModelShaper::stateToModelLocation).forEach((modelResourceLocation) -> {
                modelRegistry.put(modelResourceLocation, (BakedModel)((Function)triple.getRight()).apply((BakedModel)modelRegistry.get(modelResourceLocation)));
            });
        }

    }


    private <T extends Block> void registerModelOverride(RegistryObject<T> block, Function<BakedModel, BakedModel> function) {
        this.registerModelOverride(block, StatePropertiesPredicate.ANY, function);
    }

    private <T extends Block> void registerModelOverride(RegistryObject<T> block, StatePropertiesPredicate predicate, Function<BakedModel, BakedModel> function) {
        this.bakedModelOverrideRegistry.add(Triple.of((Block)block.get(), predicate, function));
    }
}
