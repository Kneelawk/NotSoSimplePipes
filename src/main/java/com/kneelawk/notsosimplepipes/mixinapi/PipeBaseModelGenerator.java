package com.kneelawk.notsosimplepipes.mixinapi;

import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.client.model.SpriteSupplier;
import net.minecraft.client.render.model.BakedQuad;

import java.util.List;

/**
 * Called when generating a pipe model.
 */
public interface PipeBaseModelGenerator {
    List<BakedQuad> generateCutout(SpriteSupplier sprites, TilePipe.PipeBlockModelState key);
}
