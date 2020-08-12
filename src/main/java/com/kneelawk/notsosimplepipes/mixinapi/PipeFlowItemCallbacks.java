package com.kneelawk.notsosimplepipes.mixinapi;

import alexiil.mc.mod.pipes.blocks.TravellingItem;

/**
 * PipeFlowItem callbacks.
 */
public interface PipeFlowItemCallbacks {
    /**
     * Called when an item first enters the center.
     *
     * @param item the current item entering the center.
     * @return whether the item should continue past the center or just disappear.
     */
    default boolean onItemEnterCenter(TravellingItem item) {
        return true;
    }

    /**
     * Called when an item is passing through the center and a new travelling item has been created for the item leaving
     * the center.
     *
     * @param oldItem the current item that has entered the center.
     * @param newItem the new item that will be leaving the center.
     * @return the modified new travelling item leaving the center.
     */
    default TravellingItem onItemPassCenter(TravellingItem oldItem, TravellingItem newItem) {
        return newItem;
    }
}
