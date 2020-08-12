package com.kneelawk.notsosimplepipes.mixin;

import alexiil.mc.mod.pipes.blocks.BlockPipe;
import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.client.model.PipeBaseModelGenStandard;
import alexiil.mc.mod.pipes.client.model.SpriteSupplier;
import com.kneelawk.notsosimplepipes.mixinapi.PipeBaseModels;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Used to help custom pipes render properly.
 */
@Mixin(PipeBaseModelGenStandard.class)
@SuppressWarnings("unused")
public class PipeBaseModelGenStandardMixin {
    @Inject(method = "getCenterSprite", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onGetCenterSprite(SpriteSupplier sprites, BlockPipe block, CallbackInfoReturnable<Sprite> cir) {
        if (PipeBaseModels.containsCenterSprite(block)) {
            cir.setReturnValue(sprites.getBlockSprite(PipeBaseModels.getCenterSprite(block)));
        }
    }

    @Inject(method = "generateCutout", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onGenerateCutout(SpriteSupplier sprites, TilePipe.PipeBlockModelState key,
                                         CallbackInfoReturnable<List<BakedQuad>> cir) {
        BlockPipe block = key.block;
        if (PipeBaseModels.containsModelGenerator(block)) {
            cir.setReturnValue(PipeBaseModels.getModelGenerator(block).generateCutout(sprites, key));
        }
    }
}
