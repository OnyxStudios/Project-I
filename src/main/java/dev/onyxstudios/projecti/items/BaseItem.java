package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class BaseItem extends Item {

    public BaseItem() {
        this(new Properties().tab(ProjectI.TAB));
    }

    public BaseItem(Food food) {
        super(new Properties().tab(ProjectI.TAB).food(food));
    }

    public BaseItem(Properties properties) {
        super(properties);
    }
}
