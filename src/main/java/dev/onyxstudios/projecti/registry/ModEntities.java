package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.entity.EntityBlueCrystal;
import dev.onyxstudios.projecti.entity.EntityKnowledge;
import dev.onyxstudios.projecti.tileentity.TileEntityCrystal;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {

    public static final DeferredRegister<ContainerType<?>> containerRegistry = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProjectI.MODID);
    public static final DeferredRegister<TileEntityType<?>> tileRegistry = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ProjectI.MODID);
    public static final DeferredRegister<EntityType<?>> entityRegistry = DeferredRegister.create(ForgeRegistries.ENTITIES, ProjectI.MODID);

    //Tile Entities
    public static RegistryObject<TileEntityType<TileEntityCrystal>> CRYSTAL_TYPE = tileRegistry.register("crystal_tile_type", () -> TileEntityType.Builder.of(TileEntityCrystal::new, ModBlocks.BLUE_CRYSTAL.get()).build(null));

    //Entities
    private static final EntityType<EntityKnowledge> KNOWLEDGE_TYPE = EntityType.Builder.<EntityKnowledge>of(EntityKnowledge::new, EntityClassification.MISC).sized(2, 2).build("entity_knowledge");
    private static final EntityType<EntityBlueCrystal> BLUE_CRYSTAL_TYPE = EntityType.Builder.<EntityBlueCrystal>of(EntityBlueCrystal::new, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build("entity_blue_crystal");

    public static final RegistryObject<EntityType<EntityKnowledge>> ENTITY_KNOWLEDGE = entityRegistry.register("entity_knowledge", () -> KNOWLEDGE_TYPE);
    public static final RegistryObject<EntityType<EntityBlueCrystal>> ENTITY_BLUE_CRYSTAL = entityRegistry.register("entity_blue_crystal", () -> BLUE_CRYSTAL_TYPE);
}
