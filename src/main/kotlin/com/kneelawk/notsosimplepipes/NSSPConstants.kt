package com.kneelawk.notsosimplepipes

import net.minecraft.util.Identifier

object NSSPConstants {
    val MOD_ID = "notsosimplepipes"

    fun identifier(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }
}