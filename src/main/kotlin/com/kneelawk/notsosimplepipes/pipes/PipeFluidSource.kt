package com.kneelawk.notsosimplepipes.pipes

import alexiil.mc.mod.pipes.blocks.BlockPipe
import alexiil.mc.mod.pipes.blocks.PipeFlowFluid
import alexiil.mc.mod.pipes.blocks.TilePipe
import com.kneelawk.notsosimplepipes.NSSPBlocks
import net.minecraft.world.BlockView

/**
 * Block for Fluid Source Pipe.
 */
class BlockPipeFluidSource(settings: Settings) : BlockPipe(settings) {
    override fun createBlockEntity(var1: BlockView?): TilePipe? {
        return TilePipeFluidSource()
    }
}

/**
 * BlockEntity for Fluid Source Pipe.
 */
class TilePipeFluidSource :
    TilePipe(NSSPBlocks.TILE_TYPE_PIPE_FLUID_SOURCE, NSSPBlocks.BLOCK_PIPE_FLUID_SOURCE, ::PipeFlowFluidSource)

/**
 * PipeFlow for Fluid Source Pipe.
 */
class PipeFlowFluidSource(pipe: TilePipe) : PipeFlowFluid(pipe) {

}
