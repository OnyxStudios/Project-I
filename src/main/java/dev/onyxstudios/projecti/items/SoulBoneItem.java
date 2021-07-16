package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.util.List;

public class SoulBoneItem extends BaseItem {

    public static final int MAX_TIME = 100;

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        World world = entity.level;
        if (!world.isClientSide() && entity.isInWater() && world.getBlockState(entity.blockPosition().below()).getMaterial().isSolid()) {
            List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().expandTowards(1, 1, 1));

            for (ItemEntity item : items) {
                if (item.getItem().sameItem(Items.LAPIS_LAZULI.getDefaultInstance())) {
                    BlockState state = ModBlocks.BLUE_CRYSTAL.get().defaultBlockState();
                    if (entity.getAge() >= MAX_TIME && ModBlocks.BLUE_CRYSTAL.get().canSurvive(state, world, entity.blockPosition())) {
                        entity.remove();
                        item.remove();
                        world.setBlock(entity.blockPosition(), state, 3);
                    }
                }
            }
        }

        return false;
    }
}
