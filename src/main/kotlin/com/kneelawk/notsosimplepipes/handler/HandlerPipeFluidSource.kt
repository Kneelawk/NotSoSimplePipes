package com.kneelawk.notsosimplepipes.handler

import com.kneelawk.notsosimplepipes.pipes.TilePipeFluidSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.InventoryChangedListener
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.math.BlockPos
import spinnery.common.handler.BaseScreenHandler
import spinnery.widget.WSlot

class HandlerPipeFluidSource(syncId: Int, playerInv: PlayerInventory, pos: BlockPos) :
    BaseScreenHandler(syncId, playerInv) {
    companion object {
        const val PIPE_INVENTORY = 1
    }

    val tile = world.getBlockEntity(pos) as TilePipeFluidSource

    private val listener = InventoryChangedListener { sendContentUpdates() }

    init {
        inventories[PIPE_INVENTORY] = tile.sourceInv

        WSlot.addHeadlessArray(`interface`, 0, PIPE_INVENTORY, 1, 1)
        WSlot.addHeadlessPlayerInventory(`interface`)

        tile.sourceInv.addListener(listener)

        if (!world.isClient) {
            tile.sync()
        }
    }

    override fun getType(): ScreenHandlerType<*> {
        return NSSPHandlers.HANDLER_PIPE_FLUID_SOURCE
    }

    override fun close(player: PlayerEntity?) {
        tile.sourceInv.removeListener(listener)
        super.close(player)
    }
}