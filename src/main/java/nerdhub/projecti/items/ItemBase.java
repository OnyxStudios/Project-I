package nerdhub.projecti.items;

import nerdhub.projecti.ProjectI;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(String name) {
        this(name, new Properties().group(ProjectI.modItemGroup));
    }

    public ItemBase(String name, Food food) {
        super(new Properties().group(ProjectI.modItemGroup).food(food));
        this.setRegistryName(name);
    }

    public ItemBase(String name, Properties properties) {
        super(properties);
        this.setRegistryName(name);
    }
}
