package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BaseItem extends Item {

    public BaseItem() {
        this(new Item.Properties().tab(ProjectI.TAB));
    }

    public BaseItem(FoodProperties food) {
        super(new Properties().tab(ProjectI.TAB).food(food));
    }

    public BaseItem(Properties properties) {
        super(properties);
    }
}
