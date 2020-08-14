package com.kneelawk.notsosimplepipes.mixin;

import com.kneelawk.notsosimplepipes.mixinapi.TypeableScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;

@Mixin(ScreenHandler.class)
public class ScreenHandlerAccessor implements TypeableScreenHandler {
    @Final
    @Mutable
    private ScreenHandlerType<?> type;

    @Override
    public void notsosimplepipes_setType(ScreenHandlerType<?> type) {
        this.type = type;
    }
}
