package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.blocks.AlembicBlock;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class AlembicTileEntity extends BaseTileEntity implements ITickableTileEntity {

    private Direction parentDir;
    private Direction childDir;

    private boolean powered;
    private int age;

    public AlembicTileEntity() {
        super(ModEntities.ALEMBIC_TYPE.get());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (parentDir != null)
            compound.putString("parentDir", parentDir.getName());

        if (childDir != null)
            compound.putString("childDir", childDir.getName());

        compound.putBoolean("powered", powered);
        return compound;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);

        this.parentDir = null;
        if (compound.contains("parentDir"))
            this.parentDir = Direction.byName(compound.getString("parentDir"));

        this.childDir = null;
        if (compound.contains("childDir"))
            this.childDir = Direction.byName(compound.getString("childDir"));

        powered = compound.getBoolean("powered");
    }

    @Override
    public void tick() {
        age++;
        if(!level.isClientSide()) {
            ServerWorld serverLevel = (ServerWorld) level;
            if (powered && age % 10 == 0) {
                BlockPos pos = getBlockPos();
                serverLevel.sendParticles(ModParticles.GLOW.get(), pos.getX() + 0.5f, pos.getY() + 0.9f, pos.getZ() + 0.5f, 0, 0, pos.getY() + 2, 0, 1);
            }
        }

        if (getAlembicType() != AlembicType.FUNNEL) return;
    }

    public void removeChild() {
        if (childDir == null || level == null) return;
        childDir = null;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public void removeParent() {
        if (parentDir == null || level == null) return;
        parentDir = null;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public void setChild(Direction direction) {
        this.childDir = direction;
    }

    public void setParent(Direction direction) {
        this.parentDir = direction;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public AlembicTileEntity getParent() {
        return getTile(parentDir);
    }

    public AlembicTileEntity getChild() {
        return getTile(childDir);
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public boolean hasChild() {
        return getChild() != null;
    }

    public Direction getParentDir() {
        return parentDir;
    }

    public Direction getChildDir() {
        return childDir;
    }

    public AlembicType getAlembicType() {
        return ((AlembicBlock) getBlockState().getBlock()).getAlembicType();
    }

    public boolean isPowered() {
        return powered;
    }

    public void validatePath() {
        if (level == null || getAlembicType() == AlembicType.FUNNEL) return;

        if (parentDir == null) {
            if (childDir != null) {
                getChild().setParent(null);
                getChild().validatePath();
                childDir = null;
            }
        } else {
            AlembicTileEntity parent = getParent();
            if (parent == null) {
                parentDir = null;
                validatePath();
            } else if (parent.getAlembicType() != AlembicType.FUNNEL) {
                parent.validatePath();
            }
        }

        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Constants.BlockFlags.DEFAULT);
    }

    private AlembicTileEntity getTile(Direction direction) {
        if (direction == null || level == null) return null;
        TileEntity tile = level.getBlockEntity(getBlockPos().relative(direction));
        if (!(tile instanceof AlembicTileEntity)) return null;

        return (AlembicTileEntity) tile;
    }
}
