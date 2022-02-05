package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SoulBoneItem extends BaseItem {

    public static final int MAX_TIME = 100;

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level;
        if (!level.isClientSide() && entity.isInWater() && level.getBlockState(entity.blockPosition().below()).getMaterial().isSolid()) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().expandTowards(1, 1, 1));

            for (ItemEntity item : items) {
                if (item.getItem().sameItem(Items.LAPIS_LAZULI.getDefaultInstance())) {
                    BlockState state = ModBlocks.BLUE_CRYSTAL.get().defaultBlockState();
                    if (entity.getAge() >= MAX_TIME && ModBlocks.BLUE_CRYSTAL.get().canSurvive(state, level, entity.blockPosition())) {
                        entity.remove(Entity.RemovalReason.DISCARDED);
                        item.remove(Entity.RemovalReason.DISCARDED);
                        level.setBlock(entity.blockPosition(), state, 3);
                    }
                }
            }
        }

        return false;
    }
}
