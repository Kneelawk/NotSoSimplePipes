package com.kneelawk.notsosimplepipes.handler

import com.kneelawk.notsosimplepipes.NSSPHandlers
import com.kneelawk.notsosimplepipes.NSSPNetworkClient
import com.kneelawk.notsosimplepipes.client.screen.ScreenPipeItemSource
import com.kneelawk.notsosimplepipes.pipes.TilePipeItemSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.InventoryChangedListener
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import spinnery.common.handler.BaseScreenHandler
import spinnery.widget.WSlot

class HandlerPipeItemSource(
    synchronizationId: Int,
    playerInventory: PlayerInventory,
    pos: BlockPos
) : BaseScreenHandler(synchronizationId, playerInventory) {
    companion object {
        const val PIPE_INVENTORY = 1
    }

    val tile = world.getBlockEntity(pos) as TilePipeItemSource

    private val listener = InventoryChangedListener { sendContentUpdates() }

    var screen: ScreenPipeItemSource? = null

    init {
        inventories[PIPE_INVENTORY] = tile.sourceInv

        WSlot.addHeadlessArray(`interface`, 0, PIPE_INVENTORY, 3, 3)
        WSlot.addHeadlessPlayerInventory(`interface`)

        tile.sourceInv.addListener(listener)

        if (!world.isClient) {
            tile.sync()
        }
    }

    override fun getType(): ScreenHandlerType<*> {
        return NSSPHandlers.HANDLER_PIPE_ITEM_SOURCE
    }

    fun s2cUpdateSpeed(speed: Double) {
        screen?.setSpeed(speed)
    }

    fun s2cUpdateInterval(interval: Int) {
        screen?.setInterval(interval)
    }

    fun s2cUpdateColor(color: DyeColor?) {
        screen?.setColor(color)
    }

    fun c2sUpdateSpeed(speed: Double) {
        val newSpeed = speed.coerceIn(TilePipeItemSource.MIN_SPEED, TilePipeItemSource.MAX_SPEED)
        // the handler is responsible for updating the tile of the client who requested the change
        tile.speed = newSpeed
        NSSPNetworkClient.sendUpdateItemSourceSpeed(tile.pos, newSpeed)
    }

    fun c2sUpdateInterval(interval: Int) {
        val newInterval = interval.coerceAtLeast(TilePipeItemSource.MIN_INTERVAL)
        tile.interval = newInterval
        NSSPNetworkClient.sendUpdateItemSourceInterval(tile.pos, newInterval)
    }

    fun c2sUpdateColor(color: DyeColor?) {
        tile.color = color
        NSSPNetworkClient.sendUpdateItemSourceColor(tile.pos, color)
    }

    override fun close(player: PlayerEntity?) {
        screen = null
        tile.sourceInv.removeListener(listener)
        super.close(player)
    }
}