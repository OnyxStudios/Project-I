package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.entity.SoulEntity;
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
    private boolean hasSoul = true;

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

        compound.putBoolean("hasSoul", hasSoul);

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

        hasSoul = compound.getBoolean("hasSoul");
    }

    public void releaseEntity() {
        if (level == null || level.isClientSide() || !hasEntity()) return;
        Entity entity = storedEntity.create(level);

        if (entity != null) {
            Direction facing = getBlockState().is(ModBlocks.BONE_CAGE.get()) ? getBlockState().getValue(HorizontalBlock.FACING) : Direction.NORTH;
            BlockPos spawnPos = getBlockPos().relative(facing);

            entity.load(entityData);
            entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            //TODO: Remove soul and kill entity slowly
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
        hasSoul = true;
        entity.remove(true);
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public void createSoul(BlockPos pos) {
        if (level != null && !level.isClientSide() && storedEntity != null) {
            SoulEntity soulEntity = new SoulEntity(level, (EntityType<? extends LivingEntity>) storedEntity);
            soulEntity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            level.addFreshEntity(soulEntity);
        }
    }

    public boolean canTrap(LivingEntity entity) {
        return !(entity instanceof MonsterEntity) && entity.canChangeDimensions() && !(entity instanceof SoulEntity);
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
