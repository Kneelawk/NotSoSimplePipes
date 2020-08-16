package com.kneelawk.notsosimplepipes.client.widget

import com.kneelawk.notsosimplepipes.NSSPConstants
import spinnery.common.registry.WidgetRegistry

object NSSPWidgets {
    fun register() {
        WidgetRegistry.register(NSSPConstants.identifier("icon_button"), KIconButton::class.java)
    }
}
