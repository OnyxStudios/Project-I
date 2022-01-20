package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

public class BoneCageTileEntity extends BaseTileEntity {

    private EntityType<?> storedEntity;
    private CompoundNBT entityData;

    private boolean powered;

    public BoneCageTileEntity() {
        super(ModEntities.BONE_CAGE_TYPE.get());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putBoolean("powered", powered);
        if (storedEntity != null)
            compound.putString("storedEntity", storedEntity.getRegistryName().toString());

        if (entityData != null)
            compound.put("entityData", entityData);

        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        powered = compound.getBoolean("powered");

        storedEntity = null;
        entityData = null;
        if (compound.contains("storedEntity"))
            storedEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(compound.getString("storedEntity")));

        if (compound.contains("entityData"))
            entityData = compound.getCompound("entityData");
    }

    public void releaseEntity() {
        if (level == null || level.isClientSide() || !hasEntity()) return;
        Entity entity = storedEntity.create(level);

        if (entity != null) {
            Direction facing = getBlockState().is(ModBlocks.BONE_CAGE.get()) ? getBlockState().getValue(HorizontalBlock.FACING) : Direction.NORTH;
            BlockPos spawnPos = getBlockPos().relative(facing);

            entity.load(entityData);
            entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            level.addFreshEntity(entity);
        }

        this.storedEntity = null;
        this.entityData = null;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public void trapEntity(LivingEntity entity) {
        if (level == null || level.isClientSide()) return;

        storedEntity = entity.getType();
        entityData = entity.saveWithoutId(new CompoundNBT());
        entity.remove(true);
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public boolean canTrap(LivingEntity entity) {
        return !(entity instanceof MonsterEntity) && entity.canChangeDimensions();
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

    public CompoundNBT getEntityData() {
        return entityData;
    }
}
