package dev.onyxstudios.projecti;

import dev.onyxstudios.projecti.client.ModClient;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModItems;
import dev.onyxstudios.projecti.registry.ModRecipes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ProjectI.MODID)
public class ProjectI {

    public static final String MODID = "projecti";
    public static final Logger LOGGER = LogManager.getLogger(ProjectI.class);
    public static ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack makeIcon() {
            return Items.BONE.getDefaultInstance();
        }
    };

    public ProjectI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.blockRegistry.register(modBus);
        ModBlocks.itemRegistry.register(modBus);
        ModItems.itemRegistry.register(modBus);
        ModEntities.tileRegistry.register(modBus);
        ModEntities.containerRegistry.register(modBus);
        ModEntities.entityRegistry.register(modBus);
        ModRecipes.recipeRegistry.register(modBus);
    }

    public void init(FMLCommonSetupEvent event) {
    }

    public void initClient(FMLClientSetupEvent event) {
        ModClient.init();
    }
}
