package com.kneelawk.notsosimplepipes

import alexiil.mc.mod.pipes.client.render.PipeItemTileRenderer
import com.kneelawk.notsosimplepipes.client.PipeItemLavaBaseModelGen
import com.kneelawk.notsosimplepipes.mixinapi.PipeBaseModels
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier

object NSSPBlocksClient {
    fun register() {
        BlockRenderLayerMap.INSTANCE.putBlock(NSSPBlocks.BLOCK_PIPE_LAVA_ITEM, RenderLayer.getCutout())
        BlockEntityRendererRegistry.INSTANCE.register(NSSPBlocks.TILE_TYPE_PIPE_LAVA_ITEM) { PipeItemTileRenderer(it) }

        PipeBaseModels.registerCenterSprite(
            NSSPBlocks.BLOCK_PIPE_LAVA_ITEM,
            Identifier("simple_pipes", "pipe_stone_item")
        )
        PipeBaseModels.registerModelGenerator(NSSPBlocks.BLOCK_PIPE_LAVA_ITEM, PipeItemLavaBaseModelGen)
    }
}