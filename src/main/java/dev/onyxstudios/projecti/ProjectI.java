package dev.onyxstudios.projecti;

import dev.onyxstudios.projecti.client.ModClient;
import dev.onyxstudios.projecti.registry.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(ProjectI.MODID)
public class ProjectI {

    public static final String MODID = "projecti";
    public static final Logger LOGGER = LogManager.getLogger(ProjectI.class);
    public static CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return ModItems.SOUL_BONE.get().getDefaultInstance();
        }
    };

    public ProjectI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModEntities::attributeEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModClient::particleEvent);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.ITEMS.register(modBus);
        ModBlocks.FLUIDS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.TILES.register(modBus);
        ModEntities.CONTAINERS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModEntities.MEMORY_MODULES.register(modBus);
        ModEntities.POI.register(modBus);
        ModRecipes.RECIPES.register(modBus);
        ModParticles.PARTICLES.register(modBus);
        GeckoLib.initialize();
    }

    public void init(FMLCommonSetupEvent event) {
    }

    public void initClient(FMLClientSetupEvent event) {
        ModClient.init();
    }
}
