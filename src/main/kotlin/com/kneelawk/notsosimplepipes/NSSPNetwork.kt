package com.kneelawk.notsosimplepipes

import com.kneelawk.notsosimplepipes.handler.HandlerPipeItemSource
import com.kneelawk.notsosimplepipes.pipes.TilePipeItemSource
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.PacketContext
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos

object NSSPNetwork {
    val C2S_UPDATE_ITEM_SOURCE_SPEED = NSSPConstants.identifier("c2s_update_item_source_speed")
    val C2S_UPDATE_ITEM_SOURCE_INTERVAL = NSSPConstants.identifier("c2s_update_item_source_interval")
    val C2S_UPDATE_ITEM_SOURCE_COLOR = NSSPConstants.identifier("c2s_update_item_source_color")
    val S2C_UPDATE_ITEM_SOURCE_SPEED = NSSPConstants.identifier("s2c_update_item_source_speed")
    val S2C_UPDATE_ITEM_SOURCE_INTERVAL = NSSPConstants.identifier("s2c_update_item_source_interval")
    val S2C_UPDATE_ITEM_SOURCE_COLOR = NSSPConstants.identifier("s2c_update_item_source_color")

    fun register() {
        ServerSidePacketRegistry.INSTANCE.register(C2S_UPDATE_ITEM_SOURCE_SPEED, NSSPNetwork::onUpdateItemSourceSpeed)
        ServerSidePacketRegistry.INSTANCE.register(
            C2S_UPDATE_ITEM_SOURCE_INTERVAL,
            NSSPNetwork::onUpdateItemSourceInterval
        )
        ServerSidePacketRegistry.INSTANCE.register(C2S_UPDATE_ITEM_SOURCE_COLOR, NSSPNetwork::onUpdateItemSourceColor)
    }

    private fun onUpdateItemSourceSpeed(context: PacketContext, buf: PacketByteBuf) {
        val pos = buf.readBlockPos()
        val speed = buf.readDouble().coerceIn(TilePipeItemSource.MIN_SPEED, TilePipeItemSource.MAX_SPEED)
        context.taskQueue.execute {
            val tile = context.player.world.getBlockEntity(pos)
            val handler = context.player.currentScreenHandler
            if (tile == null || tile !is TilePipeItemSource || handler == null || handler !is HandlerPipeItemSource || handler.tile.pos != pos) {
                return@execute
            }

            tile.speed = speed

            val players = PlayerStream.watching(tile)
            players.filter { it != context.player }.forEach { sendUpdateItemSourceSpeed(it, pos, speed) }
        }
    }

    private fun onUpdateItemSourceInterval(context: PacketContext, buf: PacketByteBuf) {
        val pos = buf.readBlockPos()
        val interval = buf.readInt().coerceAtLeast(TilePipeItemSource.MIN_INTERVAL)
        context.taskQueue.execute {
            val tile = context.player.world.getBlockEntity(pos)
            val handler = context.player.currentScreenHandler
            if (tile == null || tile !is TilePipeItemSource || handler == null || handler !is HandlerPipeItemSource || handler.tile.pos != pos) {
                return@execute
            }

            tile.interval = interval

            val players = PlayerStream.watching(tile)
            players.filter { it != context.player }.forEach { sendUpdateItemSourceInterval(it, pos, interval) }
        }
    }

    private fun onUpdateItemSourceColor(context: PacketContext, buf: PacketByteBuf) {
        val pos = buf.readBlockPos()
        val color = buf.readByte().coerceIn(0, 16)
        context.taskQueue.execute {
            val tile = context.player.world.getBlockEntity(pos)
            val handler = context.player.currentScreenHandler
            if (tile == null || tile !is TilePipeItemSource || handler == null || handler !is HandlerPipeItemSource || handler.tile.pos != pos) {
                return@execute
            }

            val dyeColor = if (color < 16) {
                DyeColor.byId(color.toInt())
            } else {
                null
            }

            tile.color = dyeColor

            val players = PlayerStream.watching(tile)
            players.filter { it != context.player }.forEach { sendUpdateItemSourceColor(it, pos, dyeColor) }
        }
    }

    fun sendUpdateItemSourceSpeed(player: PlayerEntity, pos: BlockPos, speed: Double) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeDouble(speed)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, S2C_UPDATE_ITEM_SOURCE_SPEED, buf)
    }

    fun sendUpdateItemSourceInterval(player: PlayerEntity, pos: BlockPos, interval: Int) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeInt(interval)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, S2C_UPDATE_ITEM_SOURCE_INTERVAL, buf)
    }

    fun sendUpdateItemSourceColor(player: PlayerEntity, pos: BlockPos, color: DyeColor?) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeByte(color?.id ?: 16)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, S2C_UPDATE_ITEM_SOURCE_COLOR, buf)
    }
}