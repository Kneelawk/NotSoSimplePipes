package com.kneelawk.notsosimplepipes.item

import com.kneelawk.notsosimplepipes.NSSPItems
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class ItemPseudoVoidContainer(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.world.getBlockState(context.blockPos).block == Blocks.BEDROCK) {
            val player = context.player
            if (player == null || !player.abilities.creativeMode) {
                context.stack.decrement(1)
            }
            player?.giveItemStack(ItemStack(NSSPItems.ITEM_PSEUDO_VOID))
            return ActionResult.SUCCESS
        }
        return ActionResult.FAIL
    }
}