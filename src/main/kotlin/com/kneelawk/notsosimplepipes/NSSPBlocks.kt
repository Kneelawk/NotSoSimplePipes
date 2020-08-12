package com.kneelawk.notsosimplepipes

import com.google.common.collect.Sets
import com.kneelawk.notsosimplepipes.pipes.BlockPipeLavaItem
import com.kneelawk.notsosimplepipes.pipes.BlockPipeVoidFluid
import com.kneelawk.notsosimplepipes.pipes.TilePipeLavaItem
import com.kneelawk.notsosimplepipes.pipes.TilePipeVoidFluid
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object NSSPBlocks {
    val BLOCK_PIPE_LAVA_ITEM: BlockPipeLavaItem
    val BLOCK_PIPE_VOID_FLUID: BlockPipeVoidFluid

    val TILE_TYPE_PIPE_LAVA_ITEM: BlockEntityType<TilePipeLavaItem>
    val TILE_TYPE_PIPE_VOID_FLUID: BlockEntityType<TilePipeVoidFluid>

    init {
        val glowingPipeBlockSettings = FabricBlockSettings.of(Material.SUPPORTED).strength(0.5f, 1.0f).lightLevel(15)
        val pipeBlockSettings = FabricBlockSettings.of(Material.SUPPORTED).strength(0.5f, 1.0f)

        BLOCK_PIPE_LAVA_ITEM = BlockPipeLavaItem(glowingPipeBlockSettings)
        BLOCK_PIPE_VOID_FLUID = BlockPipeVoidFluid(pipeBlockSettings)

        TILE_TYPE_PIPE_LAVA_ITEM = newBlockEntityType(::TilePipeLavaItem, BLOCK_PIPE_LAVA_ITEM)
        TILE_TYPE_PIPE_VOID_FLUID = newBlockEntityType(::TilePipeVoidFluid, BLOCK_PIPE_VOID_FLUID)
    }

    private fun <T : BlockEntity> newBlockEntityType(supplier: () -> T, vararg blocks: Block): BlockEntityType<T> {
        return BlockEntityType(supplier, Sets.newHashSet(*blocks), null)
    }

    fun register() {
        register(BLOCK_PIPE_LAVA_ITEM, "pipe_lava_item")
        register(TILE_TYPE_PIPE_LAVA_ITEM, "pipe_lava_item")
        register(BLOCK_PIPE_VOID_FLUID, "pipe_void_fluid")
        register(TILE_TYPE_PIPE_VOID_FLUID, "pipe_void_fluid")
    }

    private fun register(block: Block, name: String) {
        Registry.register(Registry.BLOCK, NSSPConstants.identifier(name), block)
    }

    private fun register(type: BlockEntityType<*>, name: String) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, NSSPConstants.identifier(name), type)
    }
}