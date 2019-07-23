package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.blocks.BlockBlowMold;
import nerdhub.projecti.blocks.BlockBlueCrystal;
import nerdhub.projecti.blocks.BlockCircuitStamper;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static BlockBlueCrystal BLUE_CRYSTAL = new BlockBlueCrystal();
    public static BlockCircuitStamper CIRCUIT_STAMPER = new BlockCircuitStamper();
    public static BlockBlowMold BLOW_MOLD = new BlockBlowMold();

    //TODO WHEN FORGE'S FLUID API IS MADE
    //public static FluidMoltenBlueCrystal MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLUE_CRYSTAL, CIRCUIT_STAMPER, BLOW_MOLD);
    }

    //@SubscribeEvent
    //public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        //event.getRegistry().registerAll(MOLTEN_BLUE_CRYSTAL);
    //}
}
