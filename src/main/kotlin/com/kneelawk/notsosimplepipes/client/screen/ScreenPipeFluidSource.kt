package com.kneelawk.notsosimplepipes.client.screen

import com.kneelawk.notsosimplepipes.handler.HandlerPipeFluidSource
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import spinnery.client.screen.BaseHandledScreen
import spinnery.widget.WAbstractWidget
import spinnery.widget.WPanel
import spinnery.widget.WSlot
import spinnery.widget.api.Position
import spinnery.widget.api.Size

class ScreenPipeFluidSource(handler: HandlerPipeFluidSource, playerInv: PlayerInventory, name: Text) :
    BaseHandledScreen<HandlerPipeFluidSource>(handler, playerInv, name) {

    init {
        val mainPanel =
            `interface`.createChild(
                ::WPanel,
                Position.of(0f, 0f, 0f),
                Size.of(16f + 9f * 18f, 16f + 14f + 5f * 18f + 8f)
            )
        mainPanel.setLabel<WPanel>(name)
        mainPanel.setOnAlign(WAbstractWidget::center)
        mainPanel.center()
        `interface`.add(mainPanel)

        `interface`.createChild(
            ::WSlot,
            Position.of(mainPanel, 8f + 4f * 18f, 8f + 14f),
            Size.of(18f, 18f)
        ).setSlotNumber<WSlot>(0).setInventoryNumber<WSlot>(HandlerPipeFluidSource.PIPE_INVENTORY)
        WSlot.addPlayerInventory(Position.of(mainPanel, 8f, 8f + 14f + 18f + 4f), Size.of(18f, 18f), `interface`)
    }
}