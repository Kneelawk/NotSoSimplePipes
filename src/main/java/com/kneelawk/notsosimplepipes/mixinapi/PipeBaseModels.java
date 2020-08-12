package com.kneelawk.notsosimplepipes.mixinapi;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.Map;

public class PipeBaseModels {
    private static final Map<Block, Identifier> MODEL_CENTER_SPRITE_NAMES = Maps.newHashMap();
    private static final Map<Block, PipeBaseModelGenerator> MODEL_GENERATORS = Maps.newHashMap();

    public static void registerCenterSprite(Block block, Identifier spriteName) {
        MODEL_CENTER_SPRITE_NAMES.put(block, spriteName);
    }

    public static void registerModelGenerator(Block block, PipeBaseModelGenerator generator) {
        MODEL_GENERATORS.put(block, generator);
    }

    public static boolean containsCenterSprite(Block block) {
        return MODEL_CENTER_SPRITE_NAMES.containsKey(block);
    }

    public static boolean containsModelGenerator(Block block) {
        return MODEL_GENERATORS.containsKey(block);
    }

    public static Identifier getCenterSprite(Block block) {
        return MODEL_CENTER_SPRITE_NAMES.get(block);
    }

    public static PipeBaseModelGenerator getModelGenerator(Block block) {
        return MODEL_GENERATORS.get(block);
    }
}
