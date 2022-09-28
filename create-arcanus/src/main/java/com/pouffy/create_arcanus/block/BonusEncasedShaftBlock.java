package com.pouffy.create_arcanus.block;

import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.content.contraptions.relays.encased.EncasedShaftBlock;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;

public class BonusEncasedShaftBlock extends EncasedShaftBlock {
    private BlockEntry<CasingBlock> casing;
    public static EncasedShaftBlock edelwood(Properties properties) {
        return new BonusEncasedShaftBlock(properties, AllBlocks.EDELWOOD_CASING);
    }

    protected BonusEncasedShaftBlock(Properties properties, BlockEntry<CasingBlock> casing) {
        super(properties, casing);
    }
}
