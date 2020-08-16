package com.kneelawk.notsosimplepipes.client

import com.kneelawk.notsosimplepipes.NSSPConstants
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier

object NSSPSprites {
    fun register() {
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
            .register(ClientSpriteRegistryCallback(NSSPSprites::registerSprites))
    }

    private fun registerSprites(atlasTexture: SpriteAtlasTexture, registry: ClientSpriteRegistryCallback.Registry) {
        for (color in DyeColor.values()) {
            registry.register(colorIdentifier(color))
        }
    }

    fun colorIdentifier(color: DyeColor): Identifier {
        return NSSPConstants.identifier("item_color/${color.getName()}")
    }
}