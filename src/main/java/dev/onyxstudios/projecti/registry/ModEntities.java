package dev.onyxstudios.projecti.registry;

import com.google.common.collect.ImmutableSet;
import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.blocks.BlowMoldBlock;
import dev.onyxstudios.projecti.entity.BlueCrystalEntity;
import dev.onyxstudios.projecti.entity.KnowledgeEntity;
import dev.onyxstudios.projecti.entity.SoulEntity;
import dev.onyxstudios.projecti.tileentity.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class ModEntities {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProjectI.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ProjectI.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ProjectI.MODID);
    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, ProjectI.MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, ProjectI.MODID);

    //Tile Entities
    public static RegistryObject<TileEntityType<CrystalTileEntity>> CRYSTAL_TYPE = TILES.register("crystal_tile_type", () -> TileEntityType.Builder.of(CrystalTileEntity::new, ModBlocks.BLUE_CRYSTAL.get()).build(null));
    public static RegistryObject<TileEntityType<StamperTileEntity>> STAMPER_TYPE = TILES.register("stamper_tile_type", () -> TileEntityType.Builder.of(StamperTileEntity::new, ModBlocks.STAMPER.get()).build(null));
    public static RegistryObject<TileEntityType<BellowsTileEntity>> BELLOWS_TYPE = TILES.register("bellows_tile_type", () -> TileEntityType.Builder.of(BellowsTileEntity::new, ModBlocks.BELLOWS.get()).build(null));
    public static RegistryObject<TileEntityType<AlembicTileEntity>> ALEMBIC_TYPE = TILES.register("alembic_tile_type", () -> TileEntityType.Builder.of(AlembicTileEntity::new,
            ModBlocks.FUNNEL.get(), ModBlocks.SPIRAL.get(), ModBlocks.SPLITTER.get(), ModBlocks.GOURD.get(), ModBlocks.DECANTER.get()).build(null));
    public static RegistryObject<TileEntityType<BoneCageTileEntity>> BONE_CAGE_TYPE = TILES.register("bone_cage_tile_type", () -> TileEntityType.Builder.of(BoneCageTileEntity::new, ModBlocks.BONE_CAGE.get()).build(null));
    public static RegistryObject<TileEntityType<SoulRelayTileEntity>> SOUL_RELAY_TYPE = TILES.register("soul_relay_tile_type", () -> TileEntityType.Builder.of(SoulRelayTileEntity::new, ModBlocks.SOUL_RELAY.get()).build(null));
    public static RegistryObject<TileEntityType<BlowMoldTileEntity>> BLOW_MOLD_TYPE = TILES.register("blow_mold_tile_type", () -> TileEntityType.Builder.of(BlowMoldTileEntity::new, ModBlocks.BLOW_MOLD.get()).build(null));

    //Entities
    private static final EntityType<KnowledgeEntity> KNOWLEDGE_TYPE = EntityType.Builder.<KnowledgeEntity>of(KnowledgeEntity::new, EntityClassification.MISC).sized(2, 2).build("knowledge");
    private static final EntityType<BlueCrystalEntity> BLUE_CRYSTAL_TYPE = EntityType.Builder.<BlueCrystalEntity>of(BlueCrystalEntity::new, EntityClassification.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build("blue_crystal");
    private static final EntityType<SoulEntity> SOUL_TYPE = EntityType.Builder.<SoulEntity>of(SoulEntity::new, EntityClassification.MISC).build("soul");

    public static final RegistryObject<EntityType<KnowledgeEntity>> KNOWLEDGE_ENTITY = ENTITIES.register("knowledge", () -> KNOWLEDGE_TYPE);
    public static final RegistryObject<EntityType<BlueCrystalEntity>> BLUE_CRYSTAL_ENTITY = ENTITIES.register("blue_crystal", () -> BLUE_CRYSTAL_TYPE);
    public static final RegistryObject<EntityType<SoulEntity>> SOUL_ENTITY = ENTITIES.register("soul", () -> SOUL_TYPE);

    public static final RegistryObject<PointOfInterestType> SOUL_RELAY_INTEREST = POI.register("soul_relay", () ->
            new PointOfInterestType("soul_relay", ImmutableSet.of(ModBlocks.SOUL_RELAY.get().defaultBlockState()), 0, 1));

    public static final RegistryObject<MemoryModuleType<GlobalPos>> RELAY_WALK_TARGET = MEMORY_MODULES.register("relay_walk_target", () -> new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));

    public static void attributeEvent(EntityAttributeCreationEvent event) {
        //TODO: Make proper attributes than defaults
        event.put(KNOWLEDGE_ENTITY.get(), MobEntity.createLivingAttributes().build());
        event.put(SOUL_ENTITY.get(), MobEntity.createMobAttributes().build());
    }
}
