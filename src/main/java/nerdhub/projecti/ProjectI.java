package nerdhub.projecti;

import nerdhub.projecti.registry.ModEntities;
import nerdhub.projecti.registry.ModItems;
import nerdhub.projecti.registry.recipe.StamperRecipe;
import nerdhub.projecti.registry.recipe.StamperRecipeSerializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ProjectI.MODID)
public class ProjectI {

    public static final Logger LOGGER = LogManager.getLogger("ProjectI");
    public static final String MODID = "projecti";
    public static final ItemGroup modItemGroup = new ItemGroup("projecti") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.KNOWLEDGE_GEM);
        }
    };

    public static final IRecipeType STAMPER = IRecipeType.register(MODID + ":stamper");
    public static final StamperRecipeSerializer<StamperRecipe> STAMPER_SERIALIZER = new StamperRecipeSerializer<>(StamperRecipe::new);

    public ProjectI() {
        //FluidRegistry.enableUniversalBucket();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    public void init(FMLCommonSetupEvent event) {
    }

    public void initClient(FMLClientSetupEvent event) {
        ModEntities.renderEntities();
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(ProjectI.STAMPER_SERIALIZER.setRegistryName(new ResourceLocation(MODID, "stamper")));
    }
}
