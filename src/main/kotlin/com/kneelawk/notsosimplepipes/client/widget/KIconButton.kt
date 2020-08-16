package com.kneelawk.notsosimplepipes.client.widget

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import spinnery.client.render.BaseRenderer
import spinnery.client.utility.SpriteSheet
import spinnery.widget.WAbstractButton

/**
 * UI element representing a color.
 */
@Environment(EnvType.CLIENT)
class KIconButton<T : IconButtonType>(private val colors: List<T>, private val inset: Boolean) : WAbstractButton() {
    companion object {
        private const val LEFT_BUTTON = 0
        private const val RIGHT_BUTTON = 1
        private const val MIDDLE_BUTTON = 2
    }

    init {
        if (colors.isEmpty()) {
            throw IllegalArgumentException("Color button cannot operate on an empty list of colors")
        }
    }

    var colorIndex = 0
    val color: T
        get() = colors[colorIndex]

    override fun getTooltip(): MutableList<Text> {
        return mutableListOf(color.colorName)
    }

    override fun isFocusedMouseListener(): Boolean {
        return true
    }

    override fun onMouseClicked(mouseX: Float, mouseY: Float, mouseButton: Int) {
        when (mouseButton) {
            LEFT_BUTTON -> colorIndex++
            RIGHT_BUTTON -> colorIndex--
            MIDDLE_BUTTON -> colorIndex = 0
        }

        if (colorIndex >= colors.size) {
            colorIndex = 0
        }
        if (colorIndex < 0) {
            colorIndex = colors.size - 1
        }

        super.onMouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun draw(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (isHidden) {
            return
        }

        matrices.push()

        val styleType = if (isLowered) {
            "on"
        } else {
            "off"
        }

        val topLeft = style.asColor("top_left.$styleType")
        val background = style.asColor("background.$styleType")
        val bottomRight = style.asColor("bottom_right.$styleType")

        BaseRenderer.drawBeveledPanel(matrices, provider, x, y, z, width, height, topLeft, background, bottomRight)
        if (inset) {
            BaseRenderer.drawBeveledPanel(
                matrices,
                provider,
                x + 2,
                y + 2,
                z,
                width - 4,
                height - 4,
                bottomRight,
                background,
                topLeft
            )
        }

        color.sprite.draw(matrices, provider, x + 3, y + 3, z, width - 6, height - 6, false)

        matrices.pop()

        super.draw(matrices, provider)
    }
}

interface IconButtonType {
    val colorName: Text
    val sprite: SpriteSheet.Sprite
}
