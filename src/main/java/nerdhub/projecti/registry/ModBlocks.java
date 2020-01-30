package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.blocks.BlockBellows;
import nerdhub.projecti.blocks.BlockBlowMold;
import nerdhub.projecti.blocks.BlockBlueCrystal;
import nerdhub.projecti.blocks.BlockCircuitStamper;
import nerdhub.projecti.blocks.fluid.FluidBlock;
import nerdhub.projecti.blocks.fluid.FluidMoltenBlueCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static BlockBlueCrystal BLUE_CRYSTAL = new BlockBlueCrystal();
    public static BlockCircuitStamper CIRCUIT_STAMPER = new BlockCircuitStamper();
    public static BlockBlowMold BLOW_MOLD = new BlockBlowMold();
    public static BlockBellows BELLOWS = new BlockBellows();

    //Fluid Stuff
    public static FluidMoltenBlueCrystal MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal.Source();
    public static FluidMoltenBlueCrystal FLOWING_MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal.Flowing();
    public static Block MOLTEN_BLUE_CRYSTAL_BLOCK = new FluidBlock(MOLTEN_BLUE_CRYSTAL, Block.Properties.create(Material.LAVA).doesNotBlockMovement().tickRandomly().hardnessAndResistance(100.0F).lightValue(10).noDrops()).setRegistryName("molten_blue_crystal");

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLUE_CRYSTAL, CIRCUIT_STAMPER, BLOW_MOLD, BELLOWS, MOLTEN_BLUE_CRYSTAL_BLOCK);
    }

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        event.getRegistry().registerAll(MOLTEN_BLUE_CRYSTAL, FLOWING_MOLTEN_BLUE_CRYSTAL);
    }
}
