package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.items.ItemBase;
import nerdhub.projecti.items.ItemKnowledgeGem;
import nerdhub.projecti.items.ItemSoulBone;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static nerdhub.projecti.ProjectI.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    //Items
    public static ItemSoulBone SOUL_BONE = new ItemSoulBone();
    public static ItemBase YELLOW_CRYSTAL = new ItemBase("yellow_crystal");
    public static ItemKnowledgeGem KNOWLEDGE_GEM = new ItemKnowledgeGem();
    public static ItemBase FUNNEL_MOLD = new ItemBase("funnel_mold");
    public static ItemBase DECANTER_MOLD = new ItemBase("decanter_mold");
    public static ItemBase SPLITTER_MOLD = new ItemBase("splitter_mold");
    public static ItemBase GOURD_MOLD = new ItemBase("gourd_mold");
    public static ItemBase SPIRAL_MOLD = new ItemBase("spiral_mold");
    public static ItemBase GEM_MOLD = new ItemBase("gem_mold");

    //Item Blocks
    public static Item BLUE_CRYSTAL_ITEM = new BlockItem(ModBlocks.BLUE_CRYSTAL, new Item.Properties().group(ProjectI.modItemGroup)).setRegistryName(ModBlocks.BLUE_CRYSTAL.getRegistryName());
    public static Item CIRCUIT_STAMPER_ITEM = new BlockItem(ModBlocks.CIRCUIT_STAMPER, new Item.Properties().group(ProjectI.modItemGroup)).setRegistryName(ModBlocks.CIRCUIT_STAMPER.getRegistryName());
    public static Item BLOW_MOLD_ITEM = new BlockItem(ModBlocks.BLOW_MOLD, new Item.Properties().group(ProjectI.modItemGroup)).setRegistryName(ModBlocks.BLOW_MOLD.getRegistryName());

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(SOUL_BONE, YELLOW_CRYSTAL, BLUE_CRYSTAL_ITEM, CIRCUIT_STAMPER_ITEM, KNOWLEDGE_GEM, BLOW_MOLD_ITEM, FUNNEL_MOLD, DECANTER_MOLD, SPLITTER_MOLD, GOURD_MOLD, SPIRAL_MOLD, GEM_MOLD);
    }
}
