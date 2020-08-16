package com.kneelawk.notsosimplepipes.mixin;

import alexiil.mc.mod.pipes.blocks.PipeFlowItem;
import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.blocks.TravellingItem;
import alexiil.mc.mod.pipes.client.render.PipeItemTileRenderer;
import com.kneelawk.notsosimplepipes.mixinapi.PipeItemTileRendererExt;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

/**
 * Make some slight adjustments to how SimplePipes renders items in pipes.
 */
@Mixin(PipeItemTileRenderer.class)
@SuppressWarnings("unused")
public class PipeItemTileRendererMixin {
    @Inject(method = "render", at = @At(value = "JUMP", ordinal = 6), remap = false,
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRender(TilePipe pipe, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                          int light, int overlay, CallbackInfo ci, World world, long now, PipeFlowItem itemFlow,
                          Iterable<TravellingItem> toRender, boolean sawWool, Iterator<TravellingItem> var13,
                          TravellingItem item) {
        PipeItemTileRendererExt.onRender(matrices, vertexConsumers, light, overlay, item);
    }
}
