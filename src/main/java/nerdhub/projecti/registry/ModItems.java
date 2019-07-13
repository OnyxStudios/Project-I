package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.items.ItemBase;
import nerdhub.projecti.items.ItemSoulBone;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static ItemSoulBone SOUL_BONE = new ItemSoulBone();
    public static ItemBase YELLOW_CRYSTAL = new ItemBase("yellow_crystal");

    public static Item BLUE_CRYSTAL_ITEM = new BlockItem(ModBlocks.BLUE_CRYSTAL, new Item.Properties().group(ProjectI.modItemGroup)).setRegistryName("blue_crystal");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(SOUL_BONE, YELLOW_CRYSTAL, BLUE_CRYSTAL_ITEM);
    }
}
