package nerdhub.projecti;

import nerdhub.projecti.registry.ModEntities;
import nerdhub.projecti.registry.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
            return new ItemStack(ModItems.SOUL_BONE);
        }
    };

    public ProjectI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
    }

    public void init(FMLCommonSetupEvent event) {
    }

    public void initClient(FMLClientSetupEvent event) {
        ModEntities.renderEntities();
    }
}
