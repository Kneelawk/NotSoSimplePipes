package com.kneelawk.notsosimplepipes

import com.kneelawk.notsosimplepipes.item.ItemPseudoVoidContainer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

object NSSPItems {
    val ITEM_GROUP_NOT_SO_SIMPLE_PIPES: ItemGroup

    val ITEM_PIPE_LAVA_ITEM: BlockItem
    val ITEM_PIPE_VOID_FLUID: BlockItem
    val ITEM_PIPE_ITEM_SOURCE: BlockItem
    val ITEM_PIPE_FLUID_SOURCE: BlockItem

    val ITEM_PSEUDO_VOID: Item
    val ITEM_PSEUDO_VOID_CONTAINER: Item

    init {
        ITEM_GROUP_NOT_SO_SIMPLE_PIPES =
            FabricItemGroupBuilder.build(NSSPConstants.identifier("main"), NSSPItems::mainItemGroupItem)

        val itemSettings = Item.Settings().group(ITEM_GROUP_NOT_SO_SIMPLE_PIPES)

        ITEM_PIPE_LAVA_ITEM = BlockItem(NSSPBlocks.BLOCK_PIPE_LAVA_ITEM, itemSettings)
        ITEM_PIPE_VOID_FLUID = BlockItem(NSSPBlocks.BLOCK_PIPE_VOID_FLUID, itemSettings)
        ITEM_PIPE_ITEM_SOURCE = BlockItem(NSSPBlocks.BLOCK_PIPE_ITEM_SOURCE, itemSettings)
        ITEM_PIPE_FLUID_SOURCE = BlockItem(NSSPBlocks.BLOCK_PIPE_FLUID_SOURCE, itemSettings)

        ITEM_PSEUDO_VOID = Item(itemSettings)
        ITEM_PSEUDO_VOID_CONTAINER = ItemPseudoVoidContainer(itemSettings)
    }

    fun register() {
        register(ITEM_PIPE_LAVA_ITEM, "pipe_lava_item")
        register(ITEM_PIPE_VOID_FLUID, "pipe_void_fluid")
        register(ITEM_PIPE_ITEM_SOURCE, "pipe_item_source")
        register(ITEM_PIPE_FLUID_SOURCE, "pipe_fluid_source")

        register(ITEM_PSEUDO_VOID, "pseudo_void")
        register(ITEM_PSEUDO_VOID_CONTAINER, "pseudo_void_container")
    }

    private fun register(item: Item, name: String) {
        Registry.register(Registry.ITEM, NSSPConstants.identifier(name), item)
    }

    private fun mainItemGroupItem(): ItemStack {
        return ItemStack(ITEM_PIPE_LAVA_ITEM)
    }
}