package com.kneelawk.notsosimplepipes.client

import com.kneelawk.notsosimplepipes.NSSPNetwork.C2S_UPDATE_ITEM_SOURCE_COLOR
import com.kneelawk.notsosimplepipes.NSSPNetwork.C2S_UPDATE_ITEM_SOURCE_INTERVAL
import com.kneelawk.notsosimplepipes.NSSPNetwork.C2S_UPDATE_ITEM_SOURCE_SPEED
import com.kneelawk.notsosimplepipes.NSSPNetwork.S2C_UPDATE_ITEM_SOURCE_COLOR
import com.kneelawk.notsosimplepipes.NSSPNetwork.S2C_UPDATE_ITEM_SOURCE_INTERVAL
import com.kneelawk.notsosimplepipes.NSSPNetwork.S2C_UPDATE_ITEM_SOURCE_SPEED
import com.kneelawk.notsosimplepipes.handler.HandlerPipeItemSource
import com.kneelawk.notsosimplepipes.pipes.TilePipeItemSource
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos

object NSSPNetworkClient {
    fun register() {
        ClientSidePacketRegistry.INSTANCE.register(
            S2C_UPDATE_ITEM_SOURCE_SPEED,
            NSSPNetworkClient::onUpdateItemSourceSpeed
        )
        ClientSidePacketRegistry.INSTANCE.register(
            S2C_UPDATE_ITEM_SOURCE_INTERVAL,
            NSSPNetworkClient::onUpdateItemSourceInterval
        )
        ClientSidePacketRegistry.INSTANCE.register(
            S2C_UPDATE_ITEM_SOURCE_COLOR,
            NSSPNetworkClient::onUpdateItemSourceColor
        )
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

    private fun onUpdateItemSourceColor(context: PacketContext, buf: PacketByteBuf) {
        val pos = buf.readBlockPos()
        val color = buf.readByte().coerceIn(0, 16)
        context.taskQueue.execute {
            val tile = context.player.world.getBlockEntity(pos)
            if (tile == null || tile !is TilePipeItemSource) {
                return@execute
            }

            val dyeColor = if (color < 16) {
                DyeColor.byId(color.toInt())
            } else {
                null
            }

            tile.color = dyeColor

            val handler = context.player.currentScreenHandler
            if (handler != null && handler is HandlerPipeItemSource && handler.tile.pos == pos) {
                handler.s2cUpdateColor(dyeColor)
            }
        }
    }

    fun sendUpdateItemSourceSpeed(pos: BlockPos, speed: Double) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeDouble(speed)
        ClientSidePacketRegistry.INSTANCE.sendToServer(C2S_UPDATE_ITEM_SOURCE_SPEED, buf)
    }

    fun sendUpdateItemSourceInterval(pos: BlockPos, interval: Int) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeInt(interval)
        ClientSidePacketRegistry.INSTANCE.sendToServer(C2S_UPDATE_ITEM_SOURCE_INTERVAL, buf)
    }

    fun sendUpdateItemSourceColor(pos: BlockPos, color: DyeColor?) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeBlockPos(pos)
        buf.writeByte(color?.id ?: 16)
        ClientSidePacketRegistry.INSTANCE.sendToServer(C2S_UPDATE_ITEM_SOURCE_COLOR, buf)
    }
}