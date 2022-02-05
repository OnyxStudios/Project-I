package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.entity.SoulEntity;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class BoneCageBlockEntity extends BaseBlockEntity {

    private EntityType<?> storedEntity;
    private CompoundTag entityData;
    private boolean hasSoul = true;

    private boolean powered;

    public BoneCageBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.BONE_CAGE_TYPE.get(), pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("powered", powered);
        if (storedEntity != null)
            tag.putString("storedEntity", storedEntity.getRegistryName().toString());

        if (entityData != null)
            tag.put("entityData", entityData);

        tag.putBoolean("hasSoul", hasSoul);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        powered = tag.getBoolean("powered");

        storedEntity = null;
        entityData = null;
        if (tag.contains("storedEntity"))
            storedEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("storedEntity")));

        if (tag.contains("entityData"))
            entityData = tag.getCompound("entityData");

        hasSoul = tag.getBoolean("hasSoul");
    }

    public void releaseEntity() {
        if (level == null || level.isClientSide() || !hasEntity()) return;
        Entity entity = storedEntity.create(level);

        if (entity != null) {
            Direction facing = getBlockState().is(ModBlocks.BONE_CAGE.get()) ?
                    getBlockState().getValue(HorizontalDirectionalBlock.FACING) : Direction.NORTH;
            BlockPos spawnPos = getBlockPos().relative(facing);

            entity.load(entityData);
            entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            //TODO: Remove soul and kill entity slowly
            level.addFreshEntity(entity);
        }

        this.storedEntity = null;
        this.entityData = null;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void trapEntity(LivingEntity entity) {
        if (level == null || level.isClientSide()) return;

        storedEntity = entity.getType();
        entityData = entity.saveWithoutId(new CompoundTag());
        hasSoul = true;
        entity.remove(Entity.RemovalReason.DISCARDED);
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void createSoul(BlockPos pos) {
        if (level != null && !level.isClientSide() && storedEntity != null) {
            SoulEntity soulEntity = new SoulEntity(level, (EntityType<? extends LivingEntity>) storedEntity);
            soulEntity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            level.addFreshEntity(soulEntity);
        }
    }

    public boolean canTrap(LivingEntity entity) {
        return !(entity instanceof Monster) && entity.canChangeDimensions() && !(entity instanceof SoulEntity);
    }

    public boolean hasEntity() {
        return storedEntity != null && entityData != null;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public boolean isPowered() {
        return powered;
    }

    public EntityType<?> getStoredEntity() {
        return storedEntity;
    }

    public CompoundTag getEntityData() {
        return entityData;
    }
}
