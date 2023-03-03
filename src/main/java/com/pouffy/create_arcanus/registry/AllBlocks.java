package com.pouffy.create_arcanus.registry;

import com.pouffy.create_arcanus.CreateArcanus;
import com.pouffy.create_arcanus.MoreSpriteShifts;
import com.pouffy.create_arcanus.block.ArcanusCasingBlock;
import com.pouffy.create_arcanus.block.ArcanusEncasedCogwheelBlock;
import com.pouffy.create_arcanus.block.ArcanusEncasedShaftBlock;
import com.pouffy.create_arcanus.content.contraptions.components.converter.ConverterBlock;
import com.pouffy.create_arcanus.foundation.data.BuilderTransformers;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.curiosities.deco.MetalLadderBlock;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.DataIngredient;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.stal111.forbidden_arcanus.core.init.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.AllTags.axeOrPickaxe;
import static com.simibubi.create.AllTags.pickaxeOnly;

public class AllBlocks {

    private static final CreateRegistrate REGISTRATE = CreateArcanus.registrate()
            .creativeModeTab(() -> CreateArcanus.BASE_CREATIVE_TAB);


    public static final BlockEntry<Block> DARK_RUNIC_TILES = REGISTRATE.block("dark_runic_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.DARK_RUNE.get()), c::get, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_DARK_RUNIC_TILES =
            REGISTRATE.block("small_dark_runic_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_BLACK))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.DARK_RUNE.get()), c::get, 2))
                    .simpleItem()
                    .register();
    public static final BlockEntry<Block> RUNIC_TILES = REGISTRATE.block("runic_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_PURPLE))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.RUNE.get()), c::get, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_RUNIC_TILES =
            REGISTRATE.block("small_runic_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_PURPLE))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.RUNE.get()), c::get, 2))
                    .simpleItem()
                    .register();
    public static final BlockEntry<Block> PROCESSED_OBSIDIAN_TILES = REGISTRATE.block("processed_obsidian_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.OBSIDIAN_INGOT.get()), c::get, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_PROCESSED_OBSIDIAN_TILES =
            REGISTRATE.block("small_processed_obsidian_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_BLACK))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.OBSIDIAN_INGOT.get()), c::get, 2))
                    .simpleItem()
                    .register();
    public static final BlockEntry<Block> ARCANE_CRYSTAL_TILES = REGISTRATE.block("arcane_crystal_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_PINK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.ARCANE_CRYSTAL.get()), c::get, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_ARCANE_CRYSTAL_TILES =
            REGISTRATE.block("small_arcane_crystal_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_PINK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(ModItems.ARCANE_CRYSTAL.get()), c::get, 2))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ArcanusCasingBlock> ARCANE_GOLD_CASING = REGISTRATE.block("arcane_gold_casing", ArcanusCasingBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .transform(BuilderTransformers.casing(() -> MoreSpriteShifts.ARCANE_GOLD_CASING))
            .simpleItem()
            .lang("Arcane Gold Casing")
            .register();

    public static final BlockEntry<ArcanusCasingBlock> EDELWOOD_CASING = REGISTRATE.block("edelwood_casing", ArcanusCasingBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .transform(BuilderTransformers.casing(() -> MoreSpriteShifts.EDELWOOD_CASING))
            .simpleItem()
            .lang("Edelwood Casing")
            .register();
    public static final BlockEntry<ArcanusEncasedShaftBlock> EDELWOOD_ENCASED_SHAFT =
            REGISTRATE.block("edelwood_encased_shaft", ArcanusEncasedShaftBlock::edelwood)
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(BuilderTransformers.encasedShaft("edelwood", () -> MoreSpriteShifts.EDELWOOD_CASING))
                    .transform(axeOrPickaxe())
                    .register();
    public static final BlockEntry<ConverterBlock> CONVERTER = REGISTRATE.block("converter", ConverterBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .item()
            .build()
            .transform(BlockStressDefaults.setImpact(8))
            .register();

    public static final BlockEntry<MetalLadderBlock> ARCANE_GOLD_LADDER =
            REGISTRATE.block("arcane_gold_ladder", MetalLadderBlock::new)
                    .transform(
                            BuilderTransformers.ladder("arcane_gold", () -> DataIngredient.tag(AllTags.forgeItemTag("plates/arcane_gold"))))
                    .register();

    public static void register() {}
}
