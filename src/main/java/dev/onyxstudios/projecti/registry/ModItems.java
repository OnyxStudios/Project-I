package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.items.BaseItem;
import dev.onyxstudios.projecti.items.KnowledgeGemItem;
import dev.onyxstudios.projecti.items.SoulBoneItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectI.MODID);

    public static final KnowledgeGemItem KNOWLEDGE_GEM = new KnowledgeGemItem();
    public static final SoulBoneItem SOUL_BONE = new SoulBoneItem();
    public static final Item YELLOW_CRYSTAL = new BaseItem();
    public static final Item FUNNEL_MOLD = new BaseItem();
    public static final Item DECANTER_MOLD = new BaseItem();
    public static final Item SPLITTER_MOLD = new BaseItem();
    public static final Item GOURD_MOLD = new BaseItem();
    public static final Item SPIRAL_MOLD = new BaseItem();
    public static final Item GEM_MOLD = new BaseItem();

    public static void init() {
        ITEMS.register("knowledge_gem", () -> KNOWLEDGE_GEM);
        ITEMS.register("soul_bone", () -> SOUL_BONE);
        ITEMS.register("yellow_crystal", () -> YELLOW_CRYSTAL);
        ITEMS.register("funnel_mold", () -> FUNNEL_MOLD);
        ITEMS.register("decanter_mold", () -> DECANTER_MOLD);
        ITEMS.register("splitter_mold", () -> SPLITTER_MOLD);
        ITEMS.register("gourd_mold", () -> GOURD_MOLD);
        ITEMS.register("spiral_mold", () -> SPIRAL_MOLD);
        ITEMS.register("gem_mold", () -> GEM_MOLD);
    }
}
