package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.items.BaseItem;
import dev.onyxstudios.projecti.items.KnowledgeGemItem;
import dev.onyxstudios.projecti.items.SoulBoneItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectI.MODID);

    public static RegistryObject<KnowledgeGemItem> KNOWLEDGE_GEM = itemRegistry.register("knowledge_gem", KnowledgeGemItem::new);
    public static RegistryObject<SoulBoneItem> SOUL_BONE = itemRegistry.register("soul_bone", SoulBoneItem::new);
    public static RegistryObject<Item> YELLOW_CRYSTAL = itemRegistry.register("yellow_crystal", BaseItem::new);
    public static RegistryObject<Item> FUNNEL_MOLD = itemRegistry.register("funnel_mold", BaseItem::new);
    public static RegistryObject<Item> DECANTER_MOLD = itemRegistry.register("decanter_mold", BaseItem::new);
    public static RegistryObject<Item> SPLITTER_MOLD = itemRegistry.register("splitter_mold", BaseItem::new);
    public static RegistryObject<Item> GOURD_MOLD = itemRegistry.register("gourd_mold", BaseItem::new);
    public static RegistryObject<Item> SPIRAL_MOLD = itemRegistry.register("spiral_mold", BaseItem::new);
    public static RegistryObject<Item> GEM_MOLD = itemRegistry.register("gem_mold", BaseItem::new);
}
