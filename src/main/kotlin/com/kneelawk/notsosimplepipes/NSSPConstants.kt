package com.kneelawk.notsosimplepipes

import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

object NSSPConstants {
    val MOD_ID = "notsosimplepipes"

    fun identifier(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }

    fun guiLang(name: String): Text {
        return lang("gui", name)
    }

    fun lang(prefix: String, suffix: String): Text {
        return TranslatableText("$prefix.$MOD_ID.$suffix")
    }
}