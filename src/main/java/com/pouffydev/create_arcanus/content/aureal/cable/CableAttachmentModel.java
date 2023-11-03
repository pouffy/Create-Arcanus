package com.pouffydev.create_arcanus.content.aureal.cable;

import com.pouffydev.create_arcanus.CAPartialModels;
import com.pouffydev.create_arcanus.content.aureal.util.AurealNetworkBlock;
import com.pouffydev.create_arcanus.content.aureal.util.AurealTransportBehaviour;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelData.Builder;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CableAttachmentModel extends BakedModelWrapperWithData {
    
    private static final ModelProperty<CableModelData> CABLE_PROPERTY = new ModelProperty<>();
    
    public CableAttachmentModel(BakedModel template) {
        super(template);
    }
    
    @Override
    protected ModelData.Builder gatherModelData(Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                ModelData blockEntityData) {
        CableModelData data = new CableModelData();
        AurealTransportBehaviour transport = BlockEntityBehaviour.get(world, pos, AurealTransportBehaviour.TYPE);
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        
        if (transport != null)
            for (Direction d : Iterate.directions)
                data.putAttachment(d, transport.getRenderedRimAttachment(world, pos, state, d));
        if (bracket != null)
            data.putBracket(bracket.getBracket());
        
        data.setEncased(CableBlock.shouldDrawCasing(world, pos, state));
        return builder.with(CABLE_PROPERTY, data);
    }
    
    private AurealTransportBehaviour.AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction d) {
        Block otherBlock = world.getBlockState(pos.relative(d)).getBlock();
        if (otherBlock instanceof CableBlock)
            return AurealTransportBehaviour.AttachmentTypes.NORMAL;
        else if (otherBlock instanceof AurealNetworkBlock)
            return AurealTransportBehaviour.AttachmentTypes.RIM;
        else
            return AurealTransportBehaviour.AttachmentTypes.NONE;
    }
    
    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);
        if (data.has(CABLE_PROPERTY)) {
            CableModelData pipeData = data.get(CABLE_PROPERTY);
            quads = new ArrayList<>(quads);
            addQuads(quads, state, side, rand, data, pipeData, renderType);
        }
        return quads;
    }
    
    private void addQuads(List<BakedQuad> quads, BlockState state, Direction side, RandomSource rand, ModelData data,
                          CableModelData pipeData, RenderType renderType) {
        BakedModel bracket = pipeData.getBracket();
        if (bracket != null)
            quads.addAll(bracket.getQuads(state, side, rand, data, renderType));
        for (Direction d : Iterate.directions) {
            AurealTransportBehaviour.AttachmentTypes type = pipeData.getAttachment(d);
            for (AurealTransportBehaviour.AttachmentTypes.ComponentPartials partial : type.partials) {
                quads.addAll(CAPartialModels.CABLE_ATTACHMENTS.get(partial)
                        .get(d)
                        .get()
                        .getQuads(state, side, rand, data, renderType));
            }
        }
        if (pipeData.isEncased())
            quads.addAll(CAPartialModels.CABLE_CASING.get()
                    .getQuads(state, side, rand, data, renderType));
    }
    
    private static class CableModelData {
        private AurealTransportBehaviour.AttachmentTypes[] attachments;
        private boolean encased;
        private BakedModel bracket;
        
        public CableModelData() {
            attachments = new AurealTransportBehaviour.AttachmentTypes[6];
            Arrays.fill(attachments, AurealTransportBehaviour.AttachmentTypes.NONE);
        }
        
        public void putBracket(BlockState state) {
            if (state != null) {
                this.bracket = Minecraft.getInstance()
                        .getBlockRenderer()
                        .getBlockModel(state);
            }
        }
        
        public BakedModel getBracket() {
            return bracket;
        }
        
        public void putAttachment(Direction face, AurealTransportBehaviour.AttachmentTypes rim) {
            attachments[face.get3DDataValue()] = rim;
        }
        
        public AurealTransportBehaviour.AttachmentTypes getAttachment(Direction face) {
            return attachments[face.get3DDataValue()];
        }
        
        public void setEncased(boolean encased) {
            this.encased = encased;
        }
        
        public boolean isEncased() {
            return encased;
        }
    }
}
