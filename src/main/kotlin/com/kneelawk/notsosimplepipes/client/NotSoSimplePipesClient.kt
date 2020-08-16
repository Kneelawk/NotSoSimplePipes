package com.kneelawk.notsosimplepipes.client

import com.kneelawk.notsosimplepipes.client.screen.NSSPScreens
import com.kneelawk.notsosimplepipes.client.widget.NSSPWidgets

/**
 * Client entry point.
 */
@Suppress("unused")
fun initClient() {
    NSSPSprites.register()
    NSSPBlocksClient.register()
    NSSPNetworkClient.register()
    NSSPWidgets.register()
    NSSPScreens.register()
}
