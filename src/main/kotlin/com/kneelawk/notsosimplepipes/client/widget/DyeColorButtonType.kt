package com.kneelawk.notsosimplepipes.client.widget

import com.kneelawk.notsosimplepipes.NSSPConstants
import net.minecraft.util.DyeColor
import spinnery.client.utility.SpriteSheet

enum class DyeColorButtonType(name: String, spriteIndex: Int, val dyeColor: DyeColor?) : IconButtonType {
    WHITE("white", 0, DyeColor.WHITE),
    ORANGE("orange", 1, DyeColor.ORANGE),
    MAGENTA("magenta", 2, DyeColor.MAGENTA),
    LIGHT_BLUE("light_blue", 3, DyeColor.LIGHT_BLUE),
    YELLOW("yellow", 4, DyeColor.YELLOW),
    LIME("lime", 5, DyeColor.LIME),
    PINK("pink", 6, DyeColor.PINK),
    GRAY("gray", 7, DyeColor.GRAY),
    LIGHT_GRAY("light_gray", 8, DyeColor.LIGHT_GRAY),
    CYAN("cyan", 9, DyeColor.CYAN),
    PURPLE("purple", 10, DyeColor.PURPLE),
    BLUE("blue", 11, DyeColor.BLUE),
    BROWN("brown", 12, DyeColor.BROWN),
    GREEN("green", 13, DyeColor.GREEN),
    RED("red", 14, DyeColor.RED),
    BLACK("black", 15, DyeColor.BLACK),
    NONE("none", 16, null);

    override val colorName = NSSPConstants.guiLang("color.$name")
    override val sprite: SpriteSheet.Sprite by lazy { spriteSheet.getSprite(spriteIndex) }

    companion object {
        private val spriteSheet = SpriteSheet(
            NSSPConstants.identifier(
                "textures/gui/colors.png"
            ), 80, 80
        ).setDimensions(10, 10)
    }
}