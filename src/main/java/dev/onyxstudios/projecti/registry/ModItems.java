package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.items.BaseItem;
import dev.onyxstudios.projecti.items.KnowledgeGemItem;
import dev.onyxstudios.projecti.items.SoulBoneItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectI.MODID);

    public static RegistryObject<KnowledgeGemItem> KNOWLEDGE_GEM = ITEMS.register("knowledge_gem", KnowledgeGemItem::new);
    public static RegistryObject<SoulBoneItem> SOUL_BONE = ITEMS.register("soul_bone", SoulBoneItem::new);
    public static RegistryObject<Item> YELLOW_CRYSTAL = ITEMS.register("yellow_crystal", BaseItem::new);
    public static RegistryObject<Item> FUNNEL_MOLD = ITEMS.register("funnel_mold", BaseItem::new);
    public static RegistryObject<Item> DECANTER_MOLD = ITEMS.register("decanter_mold", BaseItem::new);
    public static RegistryObject<Item> SPLITTER_MOLD = ITEMS.register("splitter_mold", BaseItem::new);
    public static RegistryObject<Item> GOURD_MOLD = ITEMS.register("gourd_mold", BaseItem::new);
    public static RegistryObject<Item> SPIRAL_MOLD = ITEMS.register("spiral_mold", BaseItem::new);
    public static RegistryObject<Item> GEM_MOLD = ITEMS.register("gem_mold", BaseItem::new);
}
