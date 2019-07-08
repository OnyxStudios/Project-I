package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    }
}
