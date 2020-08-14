package com.kneelawk.notsosimplepipes

import alexiil.mc.mod.pipes.blocks.BlockPipe
import alexiil.mc.mod.pipes.blocks.TilePipe
import alexiil.mc.mod.pipes.client.render.PipeFluidTileRenderer
import alexiil.mc.mod.pipes.client.render.PipeItemTileRenderer
import com.kneelawk.notsosimplepipes.client.PipeCoredBaseModelGen
import com.kneelawk.notsosimplepipes.mixinapi.PipeBaseModelGenerator
import com.kneelawk.notsosimplepipes.mixinapi.PipeBaseModels
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier

object NSSPBlocksClient {
    fun register() {
        registerItemCenterGenerated(
            NSSPBlocks.BLOCK_PIPE_LAVA_ITEM,
            NSSPBlocks.TILE_TYPE_PIPE_LAVA_ITEM,
            Identifier("simple_pipes", "pipe_stone_item"),
            PipeCoredBaseModelGen(Identifier("block/lava_still"))
        )
        registerFluidCenterGenerated(
            NSSPBlocks.BLOCK_PIPE_VOID_FLUID,
            NSSPBlocks.TILE_TYPE_PIPE_VOID_FLUID,
            Identifier("simple_pipes", "pipe_stone_fluid"),
            PipeCoredBaseModelGen(NSSPConstants.identifier("block/pseudo_void"))
        )
        registerItemCenter(
            NSSPBlocks.BLOCK_PIPE_ITEM_SOURCE,
            NSSPBlocks.TILE_TYPE_PIPE_ITEM_SOURCE,
            NSSPConstants.identifier("block/pipe_item_source")
        )
        registerFluidCenter(
            NSSPBlocks.BLOCK_PIPE_FLUID_SOURCE,
            NSSPBlocks.TILE_TYPE_PIPE_FLUID_SOURCE,
            NSSPConstants.identifier("block/pipe_fluid_source")
        )
    }

    private fun <T : TilePipe> registerItemCenter(
        block: BlockPipe,
        tileType: BlockEntityType<T>,
        centerSprite: Identifier
    ) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        BlockEntityRendererRegistry.INSTANCE.register(tileType) { PipeItemTileRenderer(it) }
        PipeBaseModels.registerCenterSprite(block, centerSprite)
    }

    private fun <T : TilePipe> registerFluidCenter(
        block: BlockPipe,
        tileType: BlockEntityType<T>,
        centerSprite: Identifier
    ) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        BlockEntityRendererRegistry.INSTANCE.register(tileType) { PipeFluidTileRenderer(it) }
        PipeBaseModels.registerCenterSprite(block, centerSprite)
    }

    private fun <T : TilePipe> registerItemCenterGenerated(
        block: BlockPipe,
        tileType: BlockEntityType<T>,
        centerSprite: Identifier,
        generator: PipeBaseModelGenerator
    ) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        BlockEntityRendererRegistry.INSTANCE.register(tileType) { PipeItemTileRenderer(it) }
        PipeBaseModels.registerCenterSprite(block, centerSprite)
        PipeBaseModels.registerModelGenerator(block, generator)
    }

    private fun <T : TilePipe> registerFluidCenterGenerated(
        block: BlockPipe,
        tileType: BlockEntityType<T>,
        centerSprite: Identifier,
        generator: PipeBaseModelGenerator
    ) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        BlockEntityRendererRegistry.INSTANCE.register(tileType) { PipeFluidTileRenderer(it) }
        PipeBaseModels.registerCenterSprite(block, centerSprite)
        PipeBaseModels.registerModelGenerator(block, generator)
    }
}