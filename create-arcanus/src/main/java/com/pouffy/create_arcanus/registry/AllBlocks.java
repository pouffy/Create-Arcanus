package com.pouffy.create_arcanus.registry;

import com.pouffy.create_arcanus.CreateArcanus;
import com.pouffy.create_arcanus.MoreSpriteShifts;
import com.pouffy.create_arcanus.block.ArcanusEncasedShaftBlock;
import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.content.contraptions.relays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.AllTags.axeOrPickaxe;
import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = CreateArcanus.registrate()
            .creativeModeTab(() -> CreateArcanus.BASE_CREATIVE_TAB);

    public static final BlockEntry<Block> DARK_RUNIC_TILES = REGISTRATE.block("dark_runic_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_DARK_RUNIC_TILES =
            REGISTRATE.block("small_dark_runic_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_BLACK))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();
    public static final BlockEntry<Block> RUNIC_TILES = REGISTRATE.block("runic_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_PURPLE))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_RUNIC_TILES =
            REGISTRATE.block("small_runic_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_PURPLE))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();
    public static final BlockEntry<Block> PROCESSED_OBSIDIAN_TILES = REGISTRATE.block("processed_obsidian_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_PROCESSED_OBSIDIAN_TILES =
            REGISTRATE.block("small_processed_obsidian_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_BLACK))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();
    public static final BlockEntry<Block> ARCANE_CRYSTAL_TILES = REGISTRATE.block("arcane_crystal_tiles", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.color(MaterialColor.COLOR_PINK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMALL_ARCANE_CRYSTAL_TILES =
            REGISTRATE.block("small_arcane_crystal_tiles", Block::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.color(MaterialColor.COLOR_PINK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();

    public static final BlockEntry<CasingBlock> ARCANE_GOLD_CASING = REGISTRATE.block("arcane_gold_casing", CasingBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .transform(BuilderTransformers.casing(() -> MoreSpriteShifts.ARCANE_GOLD_CASING))
            .simpleItem()
            .lang("Arcane Gold Casing")
            .register();

    public static final BlockEntry<CasingBlock> EDELWOOD_CASING = REGISTRATE.block("edelwood_casing", CasingBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .transform(BuilderTransformers.casing(() -> MoreSpriteShifts.EDELWOOD_CASING))
            .simpleItem()
            .lang("Edelwood Casing")
            .register();
    public static final BlockEntry<EncasedShaftBlock> EDELWOOD_ENCASED_SHAFT =
            REGISTRATE.block("edelwood_encased_shaft", ArcanusEncasedShaftBlock::edelwood)
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(BuilderTransformers.encasedShaft("edelwood", () -> MoreSpriteShifts.EDELWOOD_CASING))
                    .transform(axeOrPickaxe())
                    .register();


    public static void register() {}
}
