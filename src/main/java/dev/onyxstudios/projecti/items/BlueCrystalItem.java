package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.entity.EntityBlueCrystal;
import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlueCrystalItem extends BlockItem {

    public BlueCrystalItem() {
        super(ModBlocks.BLUE_CRYSTAL.get(), new Properties().tab(ProjectI.TAB).fireResistant());
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        if(!(location instanceof ItemEntity)) return null;
        return new EntityBlueCrystal(world, itemstack, (ItemEntity) location);
    }
}
