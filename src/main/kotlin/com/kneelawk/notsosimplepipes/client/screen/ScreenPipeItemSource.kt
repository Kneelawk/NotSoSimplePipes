package com.kneelawk.notsosimplepipes.client.screen

import com.kneelawk.notsosimplepipes.NSSPConstants
import com.kneelawk.notsosimplepipes.client.widget.DyeColorButtonType
import com.kneelawk.notsosimplepipes.client.widget.KIconButton
import com.kneelawk.notsosimplepipes.handler.HandlerPipeItemSource
import com.kneelawk.notsosimplepipes.pipes.TilePipeItemSource
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import spinnery.client.render.TextRenderer
import spinnery.client.screen.BaseHandledScreen
import spinnery.widget.*
import spinnery.widget.api.Filter
import spinnery.widget.api.Position
import spinnery.widget.api.Size

class ScreenPipeItemSource(handler: HandlerPipeItemSource, playerInv: PlayerInventory, name: Text) :
    BaseHandledScreen<HandlerPipeItemSource>(handler, playerInv, name) {
    private val speedField: WTextField
    private val intervalField: WTextField
    private val colorButton: KIconButton<DyeColorButtonType>

    init {
        handler.screen = this

        val mainPanel =
            `interface`.createChild(::WPanel, Position.of(0f, 0f, 0f), Size.of(9f * 18f + 16f, 16f + 14f + 7f * 18f + 8f))
                .setParent<WPanel>(`interface`)
        mainPanel.setLabel<WPanel>(name)
        mainPanel.setOnAlign(WAbstractWidget::center)
        mainPanel.center()
        `interface`.add(mainPanel)

        WSlot.addArray(
            Position.of(mainPanel, 8f, 8f + 14f),
            Size.of(18f, 18f),
            `interface`,
            0,
            HandlerPipeItemSource.PIPE_INVENTORY,
            3,
            3
        )
        WSlot.addPlayerInventory(Position.of(mainPanel, 8f, 8f + 14f + 3f * 18f + 4f), Size.of(18f, 18f), `interface`)

        val speedText = NSSPConstants.guiLang("speed")
        val intervalText = NSSPConstants.guiLang("interval")
        val colorText = NSSPConstants.guiLang("color")

        `interface`.createChild(
            ::WStaticText,
            Position.of(mainPanel, 8f + 3f * 18f + 8f, 14f + 8f + 9f - TextRenderer.height() / 2f)
        ).setText<WStaticText>(speedText)
        `interface`.createChild(
            ::WStaticText,
            Position.of(mainPanel, 8f + 3f * 18f + 8f, 14f + 8f + 18f + 9f - TextRenderer.height() / 2f)
        ).setText<WStaticText>(intervalText)
        `interface`.createChild(
            ::WStaticText,
            Position.of(mainPanel, 8f + 3f * 18f + 8f, 14f + 8f + 2 * 18f + 9f - TextRenderer.height() / 2f)
        ).setText<WStaticText>(colorText)

        speedField = `interface`.createChild(
            ::WTextField,
            Position.of(mainPanel, 8f + 6f * 18f, 14f + 8f),
            Size.of(3f * 18f, 18f)
        ).setFilter<WTextField>(Filter.DOUBLE_FILTER).setText(handler.tile.speed.toString())
        intervalField = `interface`.createChild(
            ::WTextField,
            Position.of(mainPanel, 8f + 6f * 18f, 14f + 8f + 18f),
            Size.of(3f * 18f, 18f)
        ).setFilter<WTextField>(Filter.INTEGER_FILTER).setText(handler.tile.interval.toString())
        colorButton = `interface`.createChild(
            { KIconButton(DyeColorButtonType.values().asList(), true) },
            Position.of(mainPanel, 8f + 6f * 18f + 1f, 14f + 8f + 2f * 18f + 1f),
            Size.of(16f, 16f)
        ).setTheme(NSSPConstants.identifier("default"))
        colorButton.colorIndex = handler.tile.color?.id ?: 16

        speedField.setOnKeyReleased<WTextField> { _, _, _, _ ->
            handler.c2sUpdateSpeed(speedField.text.toDoubleOrNull() ?: TilePipeItemSource.MIN_SPEED)
        }
        intervalField.setOnKeyReleased<WTextField> { _, _, _, _ ->
            handler.c2sUpdateInterval(intervalField.text.toIntOrNull() ?: TilePipeItemSource.MIN_INTERVAL)
        }
        colorButton.setOnMouseClicked<KIconButton<DyeColorButtonType>> { _, _, _, _ ->
            handler.c2sUpdateColor(colorButton.color.dyeColor)
        }
    }

    fun setSpeed(speed: Double) {
        speedField.setText<WTextField>(speed.toString())
    }

    fun setInterval(interval: Int) {
        intervalField.setText<WTextField>(interval.toString())
    }

    fun setColor(color: DyeColor?) {
        colorButton.colorIndex = color?.id ?: 16
    }
}