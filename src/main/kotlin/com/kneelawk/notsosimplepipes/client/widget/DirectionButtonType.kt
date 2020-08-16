package com.kneelawk.notsosimplepipes.client.widget

import com.kneelawk.notsosimplepipes.NSSPConstants
import net.minecraft.util.math.Direction
import spinnery.client.utility.SpriteSheet

enum class DirectionButtonType(name: String, spriteIndex: Int, val direction: Direction) : IconButtonType {
    DOWN("down", 18, Direction.DOWN),
    UP("up", 19, Direction.UP),
    NORTH("north", 20, Direction.NORTH),
    SOUTH("south", 21, Direction.NORTH),
    WEST("west", 22, Direction.WEST),
    EAST("east", 23, Direction.EAST);

    override val colorName = NSSPConstants.guiLang("direction.$name")
    override val sprite: SpriteSheet.Sprite by lazy { spriteSheet.getSprite(spriteIndex) }

    companion object {
        private val spriteSheet = SpriteSheet(
            NSSPConstants.identifier(
                "textures/gui/colors.png"
            ), 80, 80
        ).setDimensions(10, 10)
    }
}