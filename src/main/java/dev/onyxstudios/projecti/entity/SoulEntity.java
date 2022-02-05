package dev.onyxstudios.projecti.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.onyxstudios.projecti.api.block.DiagonalDirection;
import dev.onyxstudios.projecti.entity.goals.FollowRelayTask;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModItems;
import dev.onyxstudios.projecti.registry.ModParticles;
import dev.onyxstudios.projecti.tileentity.SoulRelayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SoulEntity extends Animal {

    public static final EntityDataAccessor<String> TARGET_DATA = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.STRING);
    private EntityType<? extends LivingEntity> targetEntityType = EntityType.SHEEP;

    private final List<BlockPos> blackList = new ArrayList<>();
    private int blacklistAge;

    public SoulEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        setInvulnerable(true);
        this.maxUpStep = 1;
    }

    public SoulEntity(Level level, EntityType<? extends LivingEntity> targetEntityType) {
        super(ModEntities.SOUL_ENTITY.get(), level);

        if (targetEntityType.getRegistryName() != null) {
            getEntityData().set(TARGET_DATA, targetEntityType.getRegistryName().toString());
            this.targetEntityType = targetEntityType;
        }

        this.maxUpStep = 1;
        setInvulnerable(true);
    }


    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_DATA, "minecraft:sheep");
    }

    @Override
    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new TemptGoal(this, 0.45, Ingredient.of(ModItems.SOUL_BONE.get()), false));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    @Override
    public Brain<SoulEntity> makeBrain(Dynamic<?> dynamic) {
        Brain<SoulEntity> brain = brainProvider().makeBrain(dynamic);

        float speed = 0.35f;
        Calendar calendar = Calendar.getInstance();
        //Shhhh don't tell anyone
        if (calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            speed = 0.8f;
        }

        brain.addActivity(Activity.CORE, ImmutableList.of(
                Pair.of(0, new FollowRelayTask())
        ));

        brain.addActivity(Activity.IDLE, ImmutableList.of(
                Pair.of(1, new RandomStroll(speed)),
                Pair.of(2, new MoveToTargetSink()),
                Pair.of(3, new Swim(0.35f))
        ));

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setActiveActivityIfPossible(Activity.CORE);
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }

    @Override
    public Brain.Provider<SoulEntity> brainProvider() {
        return Brain.provider(ImmutableList.of(
                ModEntities.RELAY_WALK_TARGET.get(),
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.PATH,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE
        ), ImmutableList.of());
    }

    @Override
    public Brain<SoulEntity> getBrain() {
        return (Brain<SoulEntity>) super.getBrain();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        String idString = tag.getString("targetType");
        ResourceLocation id = new ResourceLocation(idString);
        targetEntityType = (EntityType<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(id);
        getEntityData().set(TARGET_DATA, idString);

        ListTag blacklistTag = tag.getList("blacklist", Tag.TAG_COMPOUND);
        for (Tag nbt : blacklistTag) {
            blackList.add(NbtUtils.readBlockPos((CompoundTag) nbt));
        }

        blacklistAge = tag.getInt("blacklistAge");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("targetType", getEntityData().get(TARGET_DATA));

        ListTag blacklistTag = new ListTag();
        for (BlockPos pos : blackList) {
            blacklistTag.add(NbtUtils.writeBlockPos(pos));
        }

        tag.put("blacklist", blacklistTag);
        tag.putInt("blacklistAge", blacklistAge);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return targetEntityType.getDimensions();
    }

    @Override
    public void tick() {
        super.tick();

        blacklistAge++;
        if (blacklistAge >= 20 * 30) {
            blackList.clear();
            blacklistAge = 0;
        }

        if (!level.isClientSide()) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0.25));

            for (ItemEntity item : items) {
                if (item.getItem().getItem() == Items.BONE) {
                    level.addFreshEntity(new ItemEntity(level, item.getX(), item.getY(), item.getZ(), new ItemStack(ModItems.SOUL_BONE.get(), item.getItem().getCount())));
                    item.remove(RemovalReason.DISCARDED);
                }
            }
        }

        if (!level.isClientSide() && checkDiagonals(blockPosition()) && checkDiagonals(blockPosition().above())) {
            ServerLevel serverLevel = (ServerLevel) level;
            boolean changed = false;

            for (Direction direction : Direction.values()) {
                BlockPos offset = blockPosition().below().relative(direction);
                if (level.getBlockState(offset).is(Tags.Blocks.STONE)) {
                    serverLevel.sendParticles(ModParticles.GLOW.get(), getX(), getY() + 1.5, getZ(), 0, offset.getX() + 0.5, offset.getY() - 1, offset.getZ() + 0.5, 1);
                    serverLevel.sendParticles(ModParticles.GLOW.get(), getX(), getY() + 1.6, getZ(), 0, offset.getX() + 0.5, offset.getY() - 1, offset.getZ() + 0.5, 1);
                    serverLevel.sendParticles(ModParticles.GLOW.get(), getX(), getY() + 1.7, getZ(), 0, offset.getX() + 0.5, offset.getY() - 1, offset.getZ() + 0.5, 1);

                    changed = true;
                    level.setBlockAndUpdate(offset, ModBlocks.BENIGN_STONE.get().defaultBlockState());
                }
            }

            if (changed) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    public boolean checkDiagonals(BlockPos startPos) {
        for (DiagonalDirection value : DiagonalDirection.VALUES) {
            SoulRelayBlockEntity relay = ModEntities.SOUL_RELAY_TYPE.get().getBlockEntity(level, value.offset(startPos));
            if (relay == null || !relay.isPowered()) return false;
        }

        return true;
    }

    public void blacklistPos(BlockPos pos) {
        if (blackList.size() >= 10) {
            blackList.remove(0);
        }

        blackList.add(pos);
        blacklistAge = 0;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (TARGET_DATA.equals(dataParameter)) {
            targetEntityType = (EntityType<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(getEntityData().get(TARGET_DATA)));
        }
    }

    public boolean isBlacklisted(BlockPos pos) {
        return blackList.contains(pos);
    }

    @Override
    public void customServerAiStep() {
        this.getBrain().tick((ServerLevel) this.level, this);
        super.customServerAiStep();
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    public EntityType<? extends LivingEntity> getTargetType() {
        return targetEntityType;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    public boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
