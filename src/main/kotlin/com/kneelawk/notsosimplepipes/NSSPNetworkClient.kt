package com.kneelawk.notsosimplepipes

import com.kneelawk.notsosimplepipes.handler.HandlerPipeItemSource
import com.kneelawk.notsosimplepipes.pipes.TilePipeItemSource
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.BlockPos

object NSSPNetworkClient {
    val S2C_UPDATE_ITEM_SOURCE_SPEED = NSSPConstants.identifier("s2c_update_item_source_speed")
    val S2C_UPDATE_ITEM_SOURCE_INTERVAL = NSSPConstants.identifier("s2c_update_item_source_interval")

    fun register() {
        ClientSidePacketRegistry.INSTANCE.register(S2C_UPDATE_ITEM_SOURCE_SPEED, NSSPNetworkClient::onUpdateItemSourceSpeed)
        ClientSidePacketRegistry.INSTANCE.register(S2C_UPDATE_ITEM_SOURCE_INTERVAL, NSSPNetworkClient::onUpdateItemSourceInterval)
    }

    private fun onUpdateItemSourceSpeed(context: PacketContext, buf: PacketByteBuf) {
        val pos = buf.readBlockPos()
        val speed = buf.readDouble()
        context.taskQueue.execute {
            val tile = context.player.world.getBlockEntity(pos)
            if (tile == null || tile !is TilePipeItemSource) {
                return@execute
            }

            tile.speed = speed

            val handler = context.player.currentScreenHandler
            if (handler != null && handler is HandlerPipeItemSource && handler.tile.pos == pos) {
                handler.s2cUpdateSpeed(speed)
            }
        }
    }

    private fun onUpdateItemSourceInterval(context: PacketContext, buf: PacketByteBuf) {
        val pos = buf.readBlockPos()
        val interval = buf.readInt()
        context.taskQueue.execute {
            val tile = context.player.world.getBlockEntity(pos)
            if (tile == null || tile !is TilePipeItemSource) {
                return@execute
            }

            tile.interval = interval

            val handler = context.player.currentScreenHandler
            if (handler != null && handler is HandlerPipeItemSource && handler.tile.pos == pos) {
                handler.s2cUpdateInterval(interval)
            }
        }
    }

    fun sendUpdateItemSourceSpeed(pos: BlockPos, speed: Double) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeDouble(speed)
        ClientSidePacketRegistry.INSTANCE.sendToServer(NSSPNetwork.C2S_UPDATE_ITEM_SOURCE_SPEED, buf)
    }

    fun sendUpdateItemSourceInterval(pos: BlockPos, interval: Int) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeInt(interval)
        ClientSidePacketRegistry.INSTANCE.sendToServer(NSSPNetwork.C2S_UPDATE_ITEM_SOURCE_INTERVAL, buf)
    }
}