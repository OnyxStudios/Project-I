package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.items.ItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static ItemBase SOUL_BONE = new ItemBase("soul_bone");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(SOUL_BONE);
    }
}
