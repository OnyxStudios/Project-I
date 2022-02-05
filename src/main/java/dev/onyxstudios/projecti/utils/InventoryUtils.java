package dev.onyxstudios.projecti.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryUtils {

    public static void dropInventoryItems(Level level, BlockPos pos, ItemStackHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
        }
    }
}
