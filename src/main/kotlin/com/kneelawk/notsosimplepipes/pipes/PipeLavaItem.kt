package com.kneelawk.notsosimplepipes.pipes

import alexiil.mc.mod.pipes.blocks.*
import com.kneelawk.notsosimplepipes.NSSPBlocks
import com.kneelawk.notsosimplepipes.mixinapi.PipeFlowItemCallbacks
import net.minecraft.world.BlockView

/**
 * Block for Lava Item Pipe.
 */
class BlockPipeLavaItem(settings: Settings) : BlockPipe(settings), BlockPipeItem {
    override fun createBlockEntity(var1: BlockView?): TilePipe {
        return TilePipeLavaItem()
    }
}

/**
 * BlockEntity for Lava Item Pipe.
 */
class TilePipeLavaItem : TilePipe(
    NSSPBlocks.TILE_TYPE_PIPE_LAVA_ITEM,
    NSSPBlocks.BLOCK_PIPE_LAVA_ITEM,
    ::PipeFlowLavaItem
)

/**
 * PipeFlow for Lava Item Pipe.
 */
class PipeFlowLavaItem(pipe: TilePipe) : PipeFlowItem(pipe), PipeFlowItemCallbacks {
    override fun onItemEnterCenter(item: TravellingItem): Boolean {
        return false
    }
}
