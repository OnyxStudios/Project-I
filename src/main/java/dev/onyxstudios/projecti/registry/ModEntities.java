package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.entity.BlueCrystalEntity;
import dev.onyxstudios.projecti.entity.KnowledgeEntity;
import dev.onyxstudios.projecti.tileentity.*;
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
    public static RegistryObject<TileEntityType<CrystalTileEntity>> CRYSTAL_TYPE = tileRegistry.register("crystal_tile_type", () -> TileEntityType.Builder.of(CrystalTileEntity::new, ModBlocks.BLUE_CRYSTAL.get()).build(null));
    public static RegistryObject<TileEntityType<StamperTileEntity>> STAMPER_TYPE = tileRegistry.register("stamper_tile_type", () -> TileEntityType.Builder.of(StamperTileEntity::new, ModBlocks.STAMPER.get()).build(null));
    public static RegistryObject<TileEntityType<BellowsTileEntity>> BELLOWS_TYPE = tileRegistry.register("bellows_tile_type", () -> TileEntityType.Builder.of(BellowsTileEntity::new, ModBlocks.BELLOWS.get()).build(null));
    public static RegistryObject<TileEntityType<AlembicTileEntity>> ALEMBIC_TYPE = tileRegistry.register("alembic_tile_type", () -> TileEntityType.Builder.of(AlembicTileEntity::new,
            ModBlocks.FUNNEL.get(), ModBlocks.SPIRAL.get(), ModBlocks.SPLITTER.get(), ModBlocks.GOURD.get(), ModBlocks.DECANTER.get()).build(null));
    public static RegistryObject<TileEntityType<BoneCageTileEntity>> BONE_CAGE_TYPE = tileRegistry.register("bone_cage_tile_type", () -> TileEntityType.Builder.of(BoneCageTileEntity::new, ModBlocks.BONE_CAGE.get()).build(null));
    public static RegistryObject<TileEntityType<SoulRelayTileEntity>> SOUL_RELAY_TYPE = tileRegistry.register("soul_relay_tile_type", () -> TileEntityType.Builder.of(SoulRelayTileEntity::new, ModBlocks.SOUL_RELAY.get()).build(null));

    //Entities
    private static final EntityType<KnowledgeEntity> KNOWLEDGE_TYPE = EntityType.Builder.<KnowledgeEntity>of(KnowledgeEntity::new, EntityClassification.MISC).sized(2, 2).build("entity_knowledge");
    private static final EntityType<BlueCrystalEntity> BLUE_CRYSTAL_TYPE = EntityType.Builder.<BlueCrystalEntity>of(BlueCrystalEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build("entity_blue_crystal");

    public static final RegistryObject<EntityType<KnowledgeEntity>> ENTITY_KNOWLEDGE = entityRegistry.register("knowledge", () -> KNOWLEDGE_TYPE);
    public static final RegistryObject<EntityType<BlueCrystalEntity>> ENTITY_BLUE_CRYSTAL = entityRegistry.register("blue_crystal", () -> BLUE_CRYSTAL_TYPE);
}
