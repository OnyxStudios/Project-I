package nerdhub.projecti.items;

import nerdhub.projecti.registry.ModBlocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.util.List;

public class ItemSoulBone extends ItemBase {

    public static final int MAX_TIME = 100;

    public ItemSoulBone() {
        super("soul_bone");
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        World world = entity.getEntityWorld();
        if(!world.isRemote && entity.isInWater() && world.getBlockState(entity.getPosition().down()).isSolid()) {
            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, entity.getBoundingBox().expand(1, 1, 1));

            for (ItemEntity item : items) {
                if (item.getItem().isItemEqual(new ItemStack(Items.LAPIS_LAZULI))) {
                    if(entity.getAge() >= MAX_TIME) {
                        entity.remove();
                        item.remove();
                        world.setBlockState(entity.getPosition(), ModBlocks.BLUE_CRYSTAL.getDefaultState(), 3);
                    }
                }
            }
        }

        return false;
    }
}
