package dev.onyxstudios.projecti.registry;

import com.google.common.collect.ImmutableSet;
import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.entity.BlueCrystalEntity;
import dev.onyxstudios.projecti.entity.KnowledgeEntity;
import dev.onyxstudios.projecti.entity.SoulEntity;
import dev.onyxstudios.projecti.tileentity.*;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class ModEntities {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProjectI.MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ProjectI.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ProjectI.MODID);
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, ProjectI.MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, ProjectI.MODID);

    //Tile Entities
    public static RegistryObject<BlockEntityType<CrystalBlockEntity>> CRYSTAL_TYPE = TILES.register("crystal_tile_type", () -> BlockEntityType.Builder.of(CrystalBlockEntity::new, ModBlocks.BLUE_CRYSTAL.get()).build(null));
    public static RegistryObject<BlockEntityType<StamperBlockEntity>> STAMPER_TYPE = TILES.register("stamper_tile_type", () -> BlockEntityType.Builder.of(StamperBlockEntity::new, ModBlocks.STAMPER.get()).build(null));
    public static RegistryObject<BlockEntityType<BellowsBlockEntity>> BELLOWS_TYPE = TILES.register("bellows_tile_type", () -> BlockEntityType.Builder.of(BellowsBlockEntity::new, ModBlocks.BELLOWS.get()).build(null));
    public static RegistryObject<BlockEntityType<AlembicBlockEntity>> ALEMBIC_TYPE = TILES.register("alembic_tile_type", () -> BlockEntityType.Builder.of(AlembicBlockEntity::new,
            ModBlocks.FUNNEL.get(), ModBlocks.SPIRAL.get(), ModBlocks.SPLITTER.get(), ModBlocks.GOURD.get(), ModBlocks.DECANTER.get()).build(null));
    public static RegistryObject<BlockEntityType<BoneCageBlockEntity>> BONE_CAGE_TYPE = TILES.register("bone_cage_tile_type", () -> BlockEntityType.Builder.of(BoneCageBlockEntity::new, ModBlocks.BONE_CAGE.get()).build(null));
    public static RegistryObject<BlockEntityType<SoulRelayBlockEntity>> SOUL_RELAY_TYPE = TILES.register("soul_relay_tile_type", () -> BlockEntityType.Builder.of(SoulRelayBlockEntity::new, ModBlocks.SOUL_RELAY.get()).build(null));
    public static RegistryObject<BlockEntityType<BlowMoldBlockEntity>> BLOW_MOLD_TYPE = TILES.register("blow_mold_tile_type", () -> BlockEntityType.Builder.of(BlowMoldBlockEntity::new, ModBlocks.BLOW_MOLD.get()).build(null));

    //Entities
    private static final EntityType<KnowledgeEntity> KNOWLEDGE_TYPE = EntityType.Builder.<KnowledgeEntity>of(KnowledgeEntity::new, MobCategory.MISC).sized(2, 2).build("knowledge");
    private static final EntityType<BlueCrystalEntity> BLUE_CRYSTAL_TYPE = EntityType.Builder.<BlueCrystalEntity>of(BlueCrystalEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build("blue_crystal");
    private static final EntityType<SoulEntity> SOUL_TYPE = EntityType.Builder.<SoulEntity>of(SoulEntity::new, MobCategory.MISC).build("soul");

    public static final RegistryObject<EntityType<KnowledgeEntity>> KNOWLEDGE_ENTITY = ENTITIES.register("knowledge", () -> KNOWLEDGE_TYPE);
    public static final RegistryObject<EntityType<BlueCrystalEntity>> BLUE_CRYSTAL_ENTITY = ENTITIES.register("blue_crystal", () -> BLUE_CRYSTAL_TYPE);
    public static final RegistryObject<EntityType<SoulEntity>> SOUL_ENTITY = ENTITIES.register("soul", () -> SOUL_TYPE);

    public static final RegistryObject<PoiType> SOUL_RELAY_INTEREST = POI.register("soul_relay", () ->
            new PoiType("soul_relay", ImmutableSet.of(ModBlocks.SOUL_RELAY.get().defaultBlockState()), 0, 1));

    public static final RegistryObject<MemoryModuleType<GlobalPos>> RELAY_WALK_TARGET = MEMORY_MODULES.register("relay_walk_target", () -> new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));

    public static void attributeEvent(EntityAttributeCreationEvent event) {
        //TODO: Make proper attributes than defaults
        event.put(KNOWLEDGE_ENTITY.get(), Mob.createLivingAttributes().build());
        event.put(SOUL_ENTITY.get(), Mob.createMobAttributes().build());
    }
}
