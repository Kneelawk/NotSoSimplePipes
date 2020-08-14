package com.kneelawk.notsosimplepipes

import com.kneelawk.notsosimplepipes.client.screen.ScreenPipeItemSource
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry

object NSSPScreens {
    fun register() {
        ScreenRegistry.register(NSSPHandlers.HANDLER_PIPE_ITEM_SOURCE, ::ScreenPipeItemSource)
    }
}