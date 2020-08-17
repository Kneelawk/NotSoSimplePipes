package com.kneelawk.notsosimplepipes.handler

import com.kneelawk.notsosimplepipes.NSSPConstants
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandlerType

object NSSPHandlers {
    val IDENTIFIER_PIPE_ITEM_SOURCE = NSSPConstants.identifier("pipe_item_source")
    val IDENTIFIER_PIPE_FLUID_SOURCE = NSSPConstants.identifier("pipe_fluid_source")

    val HANDLER_PIPE_ITEM_SOURCE: ScreenHandlerType<HandlerPipeItemSource> =
        ScreenHandlerRegistry.registerExtended(IDENTIFIER_PIPE_ITEM_SOURCE) { syncId, inventory, buf ->
            HandlerPipeItemSource(syncId, inventory, buf.readBlockPos())
        }
    val HANDLER_PIPE_FLUID_SOURCE: ScreenHandlerType<HandlerPipeFluidSource> =
        ScreenHandlerRegistry.registerExtended(IDENTIFIER_PIPE_FLUID_SOURCE) { syncId, inventory, buf ->
            HandlerPipeFluidSource(syncId, inventory, buf.readBlockPos())
        }

    fun register() {
        // make sure this class gets loaded
    }
}