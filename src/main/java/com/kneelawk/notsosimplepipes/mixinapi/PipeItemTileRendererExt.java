package com.kneelawk.notsosimplepipes.mixinapi;

import alexiil.mc.mod.pipes.blocks.TravellingItem;
import alexiil.mc.mod.pipes.client.model.ModelUtil;
import alexiil.mc.mod.pipes.client.model.MutableQuad;
import alexiil.mc.mod.pipes.client.model.MutableVertex;
import com.kneelawk.notsosimplepipes.client.NSSPSprites;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PipeItemTileRendererExt {
    private static final MutableQuad[] COLOR_QUADS = new MutableQuad[6];

    static {
        Vec3d center = new Vec3d(0, 0, 0);
        Vec3d radius = new Vec3d(0.5, 0.5, 0.5);
        ModelUtil.UvFaceData uvs = new ModelUtil.UvFaceData();
        uvs.minU = uvs.minV = 0f;
        uvs.maxU = uvs.maxV = 1f;
        for (Direction face : Direction.values()) {
            MutableQuad quad = ModelUtil.createFace(face, center, radius, uvs);
            quad.setDiffuse(quad.normalvf());
            COLOR_QUADS[face.ordinal()] = quad;
        }
    }

    public static void onRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay,
                                TravellingItem item) {
        matrices.scale(5f / 8f, 5f / 8f, 5f / 8f);

        DyeColor color = item.colour;
        if (color != null) {
            SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX,
                    NSSPSprites.INSTANCE.colorIdentifier(color));
            VertexConsumer layer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

            for (MutableQuad colorQuad : COLOR_QUADS) {
                MutableQuad quad = new MutableQuad(colorQuad);
                quad.lighti(light);
                quad.texFromSprite(spriteIdentifier.getSprite());

                addVertex(layer, matrices, quad.vertex_0, overlay);
                addVertex(layer, matrices, quad.vertex_1, overlay);
                addVertex(layer, matrices, quad.vertex_2, overlay);
                addVertex(layer, matrices, quad.vertex_3, overlay);
            }
        }
    }

    private static void addVertex(VertexConsumer layer, MatrixStack matrices, MutableVertex vertex, int overlay) {
        layer.vertex(matrices.peek().getModel(), vertex.position_x, vertex.position_y, vertex.position_z)
                .color(vertex.colour_r, vertex.colour_g, vertex.colour_b, vertex.colour_a)
                .texture(vertex.tex_u, vertex.tex_v)
                .overlay(overlay)
                .light(vertex.light_sky << 4, vertex.light_block << 4)
                .normal(matrices.peek().getNormal(), vertex.normal_x, vertex.normal_y, vertex.normal_z)
                .next();
    }
}
