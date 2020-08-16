package com.kneelawk.notsosimplepipes

import com.kneelawk.notsosimplepipes.handler.NSSPHandlers

/**
 * Mod entry point.
 */
@Suppress("unused")
fun init() {
    NSSPBlocks.register()
    NSSPItems.register()
    NSSPNetwork.register()
    NSSPHandlers.register()
}
