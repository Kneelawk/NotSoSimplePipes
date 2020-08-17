package com.kneelawk.notsosimplepipes.pipes

import alexiil.mc.lib.attributes.fluid.FluidAttributes
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import alexiil.mc.mod.pipes.blocks.BlockPipe
import alexiil.mc.mod.pipes.blocks.PipeFlowFluid
import alexiil.mc.mod.pipes.blocks.TilePipe
import com.kneelawk.notsosimplepipes.NSSPBlocks
import com.kneelawk.notsosimplepipes.handler.HandlerPipeFluidSource
import com.kneelawk.notsosimplepipes.util.ReflectionField
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

/**
 * Block for Fluid Source Pipe.
 */
class BlockPipeFluidSource(settings: Settings) : BlockPipe(settings) {
    override fun createBlockEntity(var1: BlockView?): TilePipe {
        return TilePipeFluidSource()
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val itemStack = player.getStackInHand(hand)
        println("USE: $itemStack")
        if (itemStack != null) {
            val item = itemStack.item
            if (item is BlockItem && item.block is BlockPipe) {
                return ActionResult.PASS
            }
        }

        if (!player.isCreative) {
            return ActionResult.PASS
        }

        if (!world.isClient) {
            player.openHandledScreen(world.getBlockEntity(pos) as TilePipeFluidSource)
        }

        return ActionResult.SUCCESS
    }
}

/**
 * BlockEntity for Fluid Source Pipe.
 */
class TilePipeFluidSource :
    TilePipe(NSSPBlocks.TILE_TYPE_PIPE_FLUID_SOURCE, NSSPBlocks.BLOCK_PIPE_FLUID_SOURCE, ::PipeFlowFluidSource),
    ExtendedScreenHandlerFactory {

    companion object {
        const val INVENTORY_SIZE = 1
    }

    val sourceInv = object : SimpleInventory(INVENTORY_SIZE) {
        override fun isValid(slot: Int, stack: ItemStack): Boolean {
            return FluidAttributes.GROUPED_INV_VIEW[stack].storedFluids.size == 1
        }
    }

    override fun toTag(tag: CompoundTag?): CompoundTag {
        val newTag = super.toTag(tag)

        for (i in 0 until INVENTORY_SIZE) {
            val stack = sourceInv.getStack(i)
            if (!stack.isEmpty) {
                newTag.put("sourceStack_$i", stack.toTag(CompoundTag()))
            }
        }

        return newTag
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)

        for (i in 0 until INVENTORY_SIZE) {
            if (tag.contains("sourceStack_$i")) {
                sourceInv.setStack(i, ItemStack.fromTag(tag.getCompound("sourceStack_$i")))
            } else {
                sourceInv.setStack(i, ItemStack.EMPTY)
            }
        }
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return HandlerPipeFluidSource(syncId, inv, pos)
    }

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }

    override fun getDisplayName(): Text {
        return TranslatableText(pipeBlock.translationKey)
    }
}

/**
 * PipeFlow for Fluid Source Pipe.
 */
class PipeFlowFluidSource(pipe: TilePipe) : PipeFlowFluid(pipe) {
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
    private var fluid: FluidVolume
        get() = fluidField[centerSection]
        set(value) {
            fluidField[centerSection] = value
        }

    override fun tick() {
        if (!world().isClient) {
            val tile = pipe as TilePipeFluidSource

            val view = FluidAttributes.GROUPED_INV_VIEW[tile.sourceInv.getStack(0)]
            view.storedFluids.firstOrNull()?.let { fluidKey ->
                val amount = SECTION_CAPACITY - fluid.amount
                if (amount > 0) {
                    val merged = FluidVolume.merge(fluid, fluidKey.withAmount(amount))
                    merged?.let { fluid = it }
                }
            }
        }

        super.tick()
    }
}
