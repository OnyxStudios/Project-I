package dev.onyxstudios.projecti.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.onyxstudios.projecti.entity.goals.FollowRelayTask;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SoulEntity extends AnimalEntity {

    public static final DataParameter<String> TARGET_DATA = EntityDataManager.defineId(SoulEntity.class, DataSerializers.STRING);
    private EntityType<? extends LivingEntity> targetEntityType = EntityType.SHEEP;

    private final List<BlockPos> blackList = new ArrayList<>();
    private int blacklistAge;

    public SoulEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        setInvulnerable(true);
        this.maxUpStep = 1;
    }

    public SoulEntity(World world, EntityType<? extends LivingEntity> targetEntityType) {
        super(ModEntities.SOUL_ENTITY.get(), world);

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
        //this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 6.0f));
        //this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
    }

    @Override
    public Brain<SoulEntity> makeBrain(Dynamic<?> dynamic) {
        Brain<SoulEntity> brain = brainProvider().makeBrain(dynamic);

        brain.addActivity(Activity.CORE, ImmutableList.of(
                //Pair.of(0, new FollowRelayTask()),
                Pair.of(0, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new WalkRandomlyTask(0.4F), 2), Pair.of(new WalkTowardsLookTargetTask(0.4F, 3), 2), Pair.of(new DummyTask(30, 60), 1))))
                //Pair.of(0, new DummyTask(30, 60)),
                //Pair.of(1, new WalkRandomlyTask(1.0f))
                //Pair.of(2, new SwimTask(1.0f))
        ));


        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setActiveActivityIfPossible(Activity.CORE);
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();

        return brain;
    }

    @Override
    public Brain.BrainCodec<SoulEntity> brainProvider() {
        return Brain.provider(ImmutableList.of(
                ModEntities.RELAY_WALK_TARGET.get(),
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.LOOK_TARGET
        ), ImmutableList.of());
    }

    @Override
    public Brain<SoulEntity> getBrain() {
        return (Brain<SoulEntity>) super.getBrain();
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        String idString = tag.getString("targetType");
        ResourceLocation id = new ResourceLocation(idString);
        targetEntityType = (EntityType<? extends LivingEntity>) ForgeRegistries.ENTITIES.getValue(id);
        getEntityData().set(TARGET_DATA, idString);

        ListNBT blacklistTag = tag.getList("blacklist", Constants.NBT.TAG_COMPOUND);
        for (INBT nbt : blacklistTag) {
            blackList.add(NBTUtil.readBlockPos((CompoundNBT) nbt));
        }

        blacklistAge = tag.getInt("blacklistAge");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("targetType", getEntityData().get(TARGET_DATA));

        ListNBT blacklistTag = new ListNBT();
        for (BlockPos pos : blackList) {
            blacklistTag.add(NBTUtil.writeBlockPos(pos));
        }

        tag.put("blacklist", blacklistTag);
        tag.putInt("blacklistAge", blacklistAge);
    }

    @Override
    public EntitySize getDimensions(Pose pose) {
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
                    item.remove();
                }
            }
        }
    }

    public void blacklistPos(BlockPos pos) {
        if (blackList.size() >= 10) {
            blackList.remove(0);
        }

        blackList.add(pos);
        blacklistAge = 0;
    }

    public boolean isBlacklisted(BlockPos pos) {
        return blackList.contains(pos);
    }

    @Override
    public void customServerAiStep() {
        this.getBrain().tick((ServerWorld) this.level, this);
        super.customServerAiStep();
    }

    @Override
    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public EntityType<? extends LivingEntity> getTargetType() {
        return targetEntityType;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    public boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
