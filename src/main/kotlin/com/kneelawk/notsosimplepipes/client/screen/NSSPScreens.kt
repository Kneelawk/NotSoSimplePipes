package com.kneelawk.notsosimplepipes.client.screen

import com.kneelawk.notsosimplepipes.handler.NSSPHandlers
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry

object NSSPScreens {
    fun register() {
        ScreenRegistry.register(NSSPHandlers.HANDLER_PIPE_ITEM_SOURCE, ::ScreenPipeItemSource)
    }
}