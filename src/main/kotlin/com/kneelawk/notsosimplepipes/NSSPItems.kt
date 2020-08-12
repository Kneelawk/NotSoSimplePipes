package com.kneelawk.notsosimplepipes

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object NSSPItems {
    val ITEM_PIPE_LAVA_ITEM: BlockItem

    init {
        val pipeSettings = Item.Settings().group(ItemGroup.REDSTONE)

        ITEM_PIPE_LAVA_ITEM = BlockItem(NSSPBlocks.BLOCK_PIPE_LAVA_ITEM, pipeSettings)
    }

    fun register() {
        register(ITEM_PIPE_LAVA_ITEM, "pipe_lava_item")
    }

    fun register(item: Item, name: String) {
        Registry.register(Registry.ITEM, NSSPConstants.identifier(name), item)
    }
}