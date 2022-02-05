package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.entity.BlueCrystalEntity;
import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BlueCrystalItem extends BlockItem {

    public BlueCrystalItem() {
        super(ModBlocks.BLUE_CRYSTAL, new Properties().tab(ProjectI.TAB).fireResistant());
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack itemstack) {
        if (!(location instanceof ItemEntity)) return null;
        return new BlueCrystalEntity(level, itemstack, (ItemEntity) location);
    }
}
