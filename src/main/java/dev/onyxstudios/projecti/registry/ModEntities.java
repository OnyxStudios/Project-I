package dev.onyxstudios.projecti.registry;

import com.google.common.collect.ImmutableSet;
import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.blockentity.*;
import dev.onyxstudios.projecti.entity.BlueCrystalEntity;
import dev.onyxstudios.projecti.entity.KnowledgeEntity;
import dev.onyxstudios.projecti.entity.SoulEntity;
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
    public static final BlockEntityType<CrystalBlockEntity> CRYSTAL = BlockEntityType.Builder.of(CrystalBlockEntity::new, ModBlocks.BLUE_CRYSTAL).build(null);
    public static final BlockEntityType<StamperBlockEntity> STAMPER = BlockEntityType.Builder.of(StamperBlockEntity::new, ModBlocks.CIRCUIT_STAMPER).build(null);
    public static final BlockEntityType<BellowsBlockEntity> BELLOWS = BlockEntityType.Builder.of(BellowsBlockEntity::new, ModBlocks.BELLOWS).build(null);
    public static final BlockEntityType<AlembicBlockEntity> ALEMBIC = BlockEntityType.Builder.of(AlembicBlockEntity::new,
            ModBlocks.FUNNEL, ModBlocks.SPIRAL, ModBlocks.SPLITTER, ModBlocks.GOURD, ModBlocks.DECANTER).build(null);
    public static final BlockEntityType<BoneCageBlockEntity> BONE_CAGE = BlockEntityType.Builder.of(BoneCageBlockEntity::new, ModBlocks.BONE_CAGE).build(null);
    public static final BlockEntityType<SoulRelayBlockEntity> SOUL_RELAY = BlockEntityType.Builder.of(SoulRelayBlockEntity::new, ModBlocks.SOUL_RELAY).build(null);
    public static final BlockEntityType<BlowMoldBlockEntity> BLOW_MOLD = BlockEntityType.Builder.of(BlowMoldBlockEntity::new, ModBlocks.BLOW_MOLD).build(null);

    //Entities
    public static final EntityType<KnowledgeEntity> KNOWLEDGE_ENTITY = EntityType.Builder.<KnowledgeEntity>of(KnowledgeEntity::new, MobCategory.MISC).sized(2, 2).build("knowledge");
    public static final EntityType<BlueCrystalEntity> BLUE_CRYSTAL_ENTITY = EntityType.Builder.<BlueCrystalEntity>of(BlueCrystalEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build("blue_crystal");
    public static final EntityType<SoulEntity> SOUL_ENTITY = EntityType.Builder.<SoulEntity>of(SoulEntity::new, MobCategory.MISC).build("soul");

    public static final PoiType SOUL_RELAY_INTEREST = new PoiType("soul_relay", ImmutableSet.of(ModBlocks.SOUL_RELAY.defaultBlockState()), 0, 1);

    public static final RegistryObject<MemoryModuleType<GlobalPos>> RELAY_WALK_TARGET = MEMORY_MODULES.register("relay_walk_target", () -> new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));

    public static void init() {
        TILES.register("crystal_tile_type", () -> CRYSTAL);
        TILES.register("stamper_tile_type", () -> STAMPER);
        TILES.register("bellows_tile_type", () -> BELLOWS);
        TILES.register("alembic_tile_type", () -> ALEMBIC);
        TILES.register("bone_cage_tile_type", () -> BONE_CAGE);
        TILES.register("soul_relay_tile_type", () -> SOUL_RELAY);
        TILES.register("blow_mold_tile_type", () -> BLOW_MOLD);

        ENTITIES.register("knowledge", () -> KNOWLEDGE_ENTITY);
        ENTITIES.register("blue_crystal", () -> BLUE_CRYSTAL_ENTITY);
        ENTITIES.register("soul", () -> SOUL_ENTITY);

        POI.register("soul_relay", () -> SOUL_RELAY_INTEREST);
    }

    public static void attributeEvent(EntityAttributeCreationEvent event) {
        //TODO: Make proper attributes than defaults
        event.put(KNOWLEDGE_ENTITY, Mob.createLivingAttributes().build());
        event.put(SOUL_ENTITY, Mob.createMobAttributes().build());
    }
}
