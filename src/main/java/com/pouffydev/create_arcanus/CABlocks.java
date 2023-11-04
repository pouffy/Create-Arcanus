package com.pouffydev.create_arcanus;

import com.pouffydev.create_arcanus.content.aureal.battery.BatteryBlock;
import com.pouffydev.create_arcanus.content.aureal.battery.BatteryItem;
import com.pouffydev.create_arcanus.content.aureal.cable.CableAttachmentModel;
import com.pouffydev.create_arcanus.content.aureal.cable.CableBlock;
import com.pouffydev.create_arcanus.content.aureal.cable.EncasedCableBlock;
import com.pouffydev.create_arcanus.content.runes.activator.ActivatorBlock;
import com.pouffydev.create_arcanus.content.runes.activator.ActivatorBlockItem;
import com.pouffydev.create_arcanus.foundation.CABlockStateGen;
import com.pouffydev.create_arcanus.foundation.CACreativeModeTabs;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import com.stal111.forbidden_arcanus.core.init.ModItems;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import static com.pouffydev.create_arcanus.CreateArcanus.registrate;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CABlocks {
    static {
        registrate.setCreativeTab(CACreativeModeTabs.BASE_CREATIVE_TAB);
    }
    
    public static final BlockEntry<BatteryBlock> aurealBattery = registrate
            .block("battery", BatteryBlock::regular)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.getVariantBuilder(c.get()).partialState().setModels(ConfiguredModel.builder().modelFile(
                    p.models().getExistingFile(CreateArcanus.asResource("block/battery/empty"))
            ).build()))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(BatteryItem::new)
            .model(AssetLookup.customBlockItemModel("battery/item"))
            .build()
            .register();
    
    public static final BlockEntry<BatteryBlock> creativeAurealBattery = registrate
            .block("creative_battery", BatteryBlock::creative)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.getVariantBuilder(c.get()).partialState().setModels(ConfiguredModel.builder().modelFile(
                    p.models().getExistingFile(CreateArcanus.asResource("block/battery/empty"))
            ).build()))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(BatteryItem::new)
            .properties(p -> p.rarity(Rarity.EPIC))
            .model(AssetLookup.customBlockItemModel("battery/item_creative"))
            .build()
            .register();
    public static final BlockEntry<CasingBlock> darkstoneCasing = registrate.block("darkstone_casing", CasingBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_BLUE))
            .transform(BuilderTransformers.casing(() -> CASpriteShifts.darkstoneCasing))
            .register();
    public static final BlockEntry<CableBlock> aurealCable = registrate
            .block("cable", CableBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .blockstate(CABlockStateGen.cable())
            .onRegister(CreateRegistrate.blockModel(() -> CableAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();
    
    public static final BlockEntry<EncasedCableBlock> encasedAurealCable = registrate
            .block("encased_cable", EncasedCableBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(CABlockStateGen.encasedCable())
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(CASpriteShifts.darkstoneCasing)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, CASpriteShifts.darkstoneCasing)))
            .onRegister(CreateRegistrate.blockModel(() -> CableAttachmentModel::new))
            .loot((p, b) -> p.dropOther(b, aurealCable.get()))
            .register();
    
    public static final BlockEntry<ActivatorBlock> activator = registrate
            .block("activator", ActivatorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .loot((lt, block) -> {
                LootTable.Builder builder = LootTable.lootTable();
                LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
                lt.add(block, builder.withPool(LootPool.lootPool()
                        .when(survivesExplosion)
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(CABlocks.activator.get()
                                        .asItem())
                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("Options", "BlockEntityTag.Options")))));
            })
            .item(ActivatorBlockItem::new)
            .transform(customItemModel())
            .register();
    
    public static void register() {}
}
