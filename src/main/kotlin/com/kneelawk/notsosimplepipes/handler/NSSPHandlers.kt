package com.kneelawk.notsosimplepipes.handler

import com.kneelawk.notsosimplepipes.NSSPConstants
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

object NSSPHandlers {
    val IDENTIFIER_PIPE_ITEM_SOURCE: Identifier =
        NSSPConstants.identifier("pipe_item_source")
    val HANDLER_PIPE_ITEM_SOURCE: ScreenHandlerType<HandlerPipeItemSource> =
        ScreenHandlerRegistry.registerExtended(IDENTIFIER_PIPE_ITEM_SOURCE) { syncId, inventory, buf ->
            HandlerPipeItemSource(
                syncId,
                inventory,
                buf.readBlockPos()
            )
        }

    fun register() {
        // make sure this class gets loaded
    }
}