package com.kneelawk.notsosimplepipes.mixin;

import alexiil.mc.mod.pipes.blocks.PipeFlowItem;
import alexiil.mc.mod.pipes.blocks.TravellingItem;
import com.kneelawk.notsosimplepipes.mixinapi.PipeFlowItemCallbacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixing directly into another mod's code is pretty gross, but I believe it's better to have things break loudly (like
 * through mixins) rather than silently (like through re-implementation and reflection).
 */
@Mixin(PipeFlowItem.class)
@SuppressWarnings("unused")
public class PipeFlowItemMixin {
    @Inject(method = "onItemReachCenter", at = @At("HEAD"), remap = false, cancellable = true)
    private void onItemReachCenterHead(TravellingItem item, CallbackInfo ci) {
        if (this instanceof PipeFlowItemCallbacks) {
            if (!((PipeFlowItemCallbacks) this).onItemEnterCenter(item)) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "onItemReachCenter", name = "newItem",
            at = @At(value = "FIELD", target = "items:Lalexiil/mc/mod/pipes/util/DelayedList;",
                    remap = false), remap = false)
    private TravellingItem onItemReachCenterEditNewItem(TravellingItem newItem, TravellingItem item) {
        if (this instanceof PipeFlowItemCallbacks) {
            return ((PipeFlowItemCallbacks) this).onItemPassCenter(item, newItem);
        }
        return newItem;
    }
}
