package com.kneelawk.notsosimplepipes.pipes

import alexiil.mc.mod.pipes.blocks.BlockPipe
import alexiil.mc.mod.pipes.blocks.PipeFlowItem
import alexiil.mc.mod.pipes.blocks.TilePipe
import alexiil.mc.mod.pipes.blocks.TravellingItem
import alexiil.mc.mod.pipes.util.DelayedList
import com.kneelawk.notsosimplepipes.NSSPBlocks
import com.kneelawk.notsosimplepipes.handler.HandlerPipeItemSource
import com.kneelawk.notsosimplepipes.util.ReflectionField
import com.kneelawk.notsosimplepipes.util.ReflectionMethod
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*

/**
 * Block for Item Source Pipe.
 */
class BlockPipeItemSource(settings: Settings) : BlockPipe(settings) {
    override fun createBlockEntity(var1: BlockView?): TilePipe? {
        return TilePipeItemSource()
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            player.openHandledScreen(world.getBlockEntity(pos) as TilePipeItemSource)
        }

        return ActionResult.SUCCESS
    }
}

/**
 * BlockEntity for Item Source Pipe.
 */
class TilePipeItemSource :
    TilePipe(NSSPBlocks.TILE_TYPE_PIPE_ITEM_SOURCE, NSSPBlocks.BLOCK_PIPE_ITEM_SOURCE, ::PipeFlowItemSource),
    ExtendedScreenHandlerFactory {
    companion object {
        const val INVENTORY_SIZE = 3 * 3
        const val MIN_SPEED = 0.001
        const val MAX_SPEED = 10.0
        const val MIN_INTERVAL = 1
    }

    val sourceInv = SimpleInventory(INVENTORY_SIZE)
    var speed = 0.08
    var interval = 20
    var color: DyeColor? = null
    var lastTick = 0L

    override fun toTag(tag: CompoundTag): CompoundTag {
        val newTag = super.toTag(tag)

        for (i in 0 until INVENTORY_SIZE) {
            val stack = sourceInv.getStack(i)
            if (!stack.isEmpty) {
                newTag.put("sourceStack_$i", stack.toTag(CompoundTag()))
            }
        }

        newTag.putDouble("speed", speed)
        newTag.putInt("interval", interval)
        newTag.putLong("lastTick", lastTick)

        val curColor = color
        if (curColor != null) {
            newTag.putByte("color", curColor.id.toByte())
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

        speed = tag.getDouble("speed").coerceIn(MIN_SPEED, MAX_SPEED)
        interval = tag.getInt("interval").coerceAtLeast(MIN_INTERVAL)
        lastTick = tag.getLong("lastTick")

        if (tag.contains("color")) {
            color = DyeColor.byId(tag.getByte("color").toInt())
        }
    }

    override fun toClientTag(tag: CompoundTag): CompoundTag {
        val newTag = super.toClientTag(tag)

        newTag.putDouble("speed", speed)
        newTag.putInt("interval", interval)
        newTag.putLong("lastTick", lastTick)

        val curColor = color
        if (curColor != null) {
            newTag.putByte("color", curColor.id.toByte())
        }

        return newTag
    }

    override fun fromClientTag(tag: CompoundTag) {
        super.fromClientTag(tag)

        // ignore flow update packets
        if (!tag.getBoolean("f")) {
            speed = tag.getDouble("speed").coerceIn(MIN_SPEED, MAX_SPEED)
            interval = tag.getInt("interval").coerceAtLeast(MIN_INTERVAL)
            lastTick = tag.getLong("lastTick")

            if (tag.contains("color")) {
                color = DyeColor.byId(tag.getByte("color").toInt())
            }
        }
    }

    override fun removeItemsForDrop(): DefaultedList<ItemStack> {
        val drops = super.removeItemsForDrop()
        drops.addAll(sourceInv.clearToList())
        return drops
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return HandlerPipeItemSource(syncId, inv, pos)
    }

    override fun getDisplayName(): Text {
        return TranslatableText(pipeBlock.translationKey)
    }

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }
}

/**
 * PipeFlow for Item Source Pipe.
 */
class PipeFlowItemSource(pipe: TilePipe) : PipeFlowItem(pipe) {
    companion object {
        private val itemsField = ReflectionField.new<PipeFlowItem, DelayedList<TravellingItem>>("items")
        private val toCenterField = ReflectionField.primitive<TravellingItem, Boolean>("toCenter")
        private val speedField = ReflectionField.primitive<TravellingItem, Double>("speed")
        private val sideField = ReflectionField.new<TravellingItem, Direction>("side")
        private val addItemTryMerge =
            ReflectionMethod.primitive<PipeFlowItem, Void>("addItemTryMerge", TravellingItem::class)
    }

    private val items by itemsField
    private val rand by lazy { Random() }

    private fun newItem(tile: TilePipeItemSource): TravellingItem? {
        val items = mutableListOf<ItemStack>()
        for (i in 0 until TilePipeItemSource.INVENTORY_SIZE) {
            val item = tile.sourceInv.getStack(i)
            if (!item.isEmpty) {
                items += item
            }
        }

        if (items.isEmpty()) {
            return null
        }

        val item = TravellingItem(items[rand.nextInt(items.size)].copy())
        speedField[item] = tile.speed
        item.colour = tile.color

        return item
    }

    override fun tick() {
        super.tick()

        if (world().isClient) {
            return
        }

        val now = pipe.worldTime
        val tile = pipe as TilePipeItemSource

        if (tile.lastTick + tile.interval > now) {
            return
        }
        tile.lastTick = now

        val item = newItem(tile)
        if (item == null || item.stack.isEmpty) {
            return
        }

        toCenterField[item] = false

        val dirs = EnumSet.noneOf(Direction::class.java)
        for (dir in Direction.values()) {
            if (pipe.isConnected(dir) && pipe.getItemInsertable(dir) != null) {
                dirs += dir
            }
        }

        val order = getOrderForItem(item, dirs)
        if (order.isEmpty()) {
            return
        }

        val destinations = mutableListOf<Direction>()
        for (set in order) {
            val shuffled = mutableListOf<Direction>()
            shuffled.addAll(set)
            shuffled.shuffle()
            destinations.addAll(shuffled)
        }

        if (destinations.isEmpty()) {
            return
        }

        val side = destinations[0]
        sideField[item] = side
        item.genTimings(now, pipe.getPipeLength(side))

        addItemTryMerge(this, item)
    }

    override fun removeItemsForDrop(all: DefaultedList<ItemStack>?) {
        // Don't bother dropping the spawned items.
        items.clear()
    }
}
