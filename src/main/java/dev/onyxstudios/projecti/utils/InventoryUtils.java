package dev.onyxstudios.projecti.utils;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryUtils {

    public static void dropInventoryItems(World world, BlockPos pos, ItemStackHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
        }
    }
}
