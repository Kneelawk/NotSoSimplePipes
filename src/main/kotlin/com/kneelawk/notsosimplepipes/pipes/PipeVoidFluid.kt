package com.kneelawk.notsosimplepipes.pipes

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import alexiil.mc.mod.pipes.blocks.BlockPipe
import alexiil.mc.mod.pipes.blocks.PipeFlowFluid
import alexiil.mc.mod.pipes.blocks.TilePipe
import com.kneelawk.notsosimplepipes.NSSPBlocks
import com.kneelawk.notsosimplepipes.util.ReflectionField
import net.minecraft.world.BlockView

/**
 * Block for Void Fluid Pipe.
 */
class BlockPipeVoidFluid(settings: Settings) : BlockPipe(settings) {
    override fun createBlockEntity(var1: BlockView?): TilePipe? {
        return TilePipeVoidFluid()
    }
}

/**
 * BlockEntity for Void Fluid Pipe.
 */
class TilePipeVoidFluid :
    TilePipe(NSSPBlocks.TILE_TYPE_PIPE_VOID_FLUID, NSSPBlocks.BLOCK_PIPE_VOID_FLUID, ::PipeFlowVoidFluid)

/**
 * PipeFlow for Void Fluid Pipe.
 */
class PipeFlowVoidFluid(pipe: TilePipe) : PipeFlowFluid(pipe) {
    companion object {
        private val centerSectionField = ReflectionField(
            PipeFlowFluid::class.java,
            Class.forName("alexiil.mc.mod.pipes.blocks.PipeFlowFluid\$CenterSection"),
            "centerSection"
        )
        private val fluidField = ReflectionField(
            Class.forName("alexiil.mc.mod.pipes.blocks.PipeFlowFluid\$Section"),
            FluidVolume::class.java,
            "fluid"
        )
    }

    private val centerSection by centerSectionField
    private val fluid: FluidVolume
        get() = fluidField[centerSection]

    override fun tick() {
        if (!world().isClient) {
            // emulate fluid being dumped into a bottomless void
            fluid.split(fluid.amount())

            super.tick()
        }
    }
}
