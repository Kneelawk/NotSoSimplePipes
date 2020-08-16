package com.kneelawk.notsosimplepipes

import com.kneelawk.notsosimplepipes.client.screen.NSSPScreens
import com.kneelawk.notsosimplepipes.client.widget.NSSPWidgets

/**
 * Client entry point.
 */
@Suppress("unused")
fun initClient() {
    NSSPBlocksClient.register()
    NSSPNetworkClient.register()
    NSSPWidgets.register()
    NSSPScreens.register()
}
