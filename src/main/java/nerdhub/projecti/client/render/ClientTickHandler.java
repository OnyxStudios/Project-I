package nerdhub.projecti.client.render;

import nerdhub.projecti.ProjectI;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTickHandler {

    public static int ticksInGame;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        ticksInGame++;
    }
}
