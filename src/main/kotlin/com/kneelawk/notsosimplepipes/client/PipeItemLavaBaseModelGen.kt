package com.kneelawk.notsosimplepipes.client

import alexiil.mc.mod.pipes.blocks.TilePipe
import alexiil.mc.mod.pipes.client.model.ModelUtil
import alexiil.mc.mod.pipes.client.model.MutableQuad
import alexiil.mc.mod.pipes.client.model.PipeBaseModelGenStandard
import alexiil.mc.mod.pipes.client.model.SpriteSupplier
import com.kneelawk.notsosimplepipes.mixinapi.PipeBaseModelGenerator
import com.kneelawk.notsosimplepipes.util.ReflectionField
import com.kneelawk.notsosimplepipes.util.ReflectionMethod
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.texture.Sprite
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object PipeItemLavaBaseModelGen : PipeBaseModelGenerator {
    private val getSprite = ReflectionMethod.new<PipeBaseModelGenStandard, Sprite>(
        "getSprite",
        SpriteSupplier::class,
        TilePipe.PipeBlockModelState::class,
        Direction::class
    )
    private val STANDARD_QUADS =
        ReflectionField.new<PipeBaseModelGenStandard, Array<Array<Array<MutableQuad?>>>>("QUADS")[null]
    private val LAVA_QUADS: Array<MutableQuad>

    init {
        val center = Vec3d(0.5, 0.5, 0.5)
        val radius = Vec3d(3.0 / 16.0, 3.0 / 16.0, 3.0 / 16.0)
        val uvs = ModelUtil.UvFaceData.from16(5, 5, 11, 11)

        LAVA_QUADS = Array(6) { index ->
            val face = Direction.byId(index)

            val quad = ModelUtil.createFace(face, center, radius, uvs)
            quad.setDiffuse(quad.normalvf())
            quad
        }
    }

    override fun generateCutout(sprites: SpriteSupplier, key: TilePipe.PipeBlockModelState): MutableList<BakedQuad> {
        val baked = mutableListOf<BakedQuad>()

        for (face in Direction.values()) {
            val connected = key.isConnected(face)
            val sprite = if (connected) {
                getSprite(null, sprites, key, face)
            } else {
                PipeBaseModelGenStandard.getCenterSprite(sprites, key.block)
            }
            val quadsIndex = if (connected) { 1 } else { 0}
            bakeQuads(STANDARD_QUADS[quadsIndex][face.ordinal], baked, sprite)
        }

        bakeQuads(LAVA_QUADS, baked, sprites.getBlockSprite("minecraft:block/lava_still"))

        return baked
    }

    private fun bakeQuads(quads: Array<out MutableQuad?>, baked: MutableList<BakedQuad>, sprite: Sprite) {
        for (quad in quads) {
            quad?.let {
                val copy = MutableQuad(it)
                copy.sprite = sprite
                copy.texFromSprite(sprite)
                baked.add(copy.toBakedBlock())
            }
        }
    }
}