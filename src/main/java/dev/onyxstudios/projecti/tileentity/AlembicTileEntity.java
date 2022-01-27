package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.api.block.BoneCageType;
import dev.onyxstudios.projecti.blocks.AlembicBlock;
import dev.onyxstudios.projecti.blocks.BoneCageBlock;
import dev.onyxstudios.projecti.data.AlembicSaveData;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class AlembicTileEntity extends BaseTileEntity implements ITickableTileEntity {

    private Direction parentDir;
    private Direction childDir;
    private BlockPos cagePos;

    private boolean powered;
    private boolean spawnParticles;
    private boolean checkedOrder;
    private int age;

    private AlembicTileEntity nextAlembic = this;
    private int checkCooldown;
    private int index;

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

        if (cagePos != null)
            compound.put("cagePos", NBTUtil.writeBlockPos(cagePos));

        compound.putBoolean("powered", powered);
        compound.putBoolean("spawnParticles", spawnParticles);
        compound.putBoolean("checkedOrder", checkedOrder);

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

        cagePos = null;
        if (compound.contains("cagePos"))
            cagePos = NBTUtil.readBlockPos(compound.getCompound("cagePos"));

        powered = compound.getBoolean("powered");
        spawnParticles = compound.getBoolean("spawnParticles");
        checkedOrder = compound.getBoolean("checkedOrder");
    }

    @Override
    public void tick() {
        if (level.isClientSide()) return;

        age++;
        if (spawnParticles) {
            ServerWorld serverLevel = (ServerWorld) level;
            if (age % 10 == 0) {
                BlockPos pos = getBlockPos();
                serverLevel.sendParticles(ModParticles.GLOW.get(), pos.getX() + 0.5f, pos.getY() + 0.9f, pos.getZ() + 0.5f, 0, 0, pos.getY() + 2, 0, 1);
            }
        }

        if (getAlembicType() == AlembicType.FUNNEL && cagePos != null) {
            if (powered && !checkedOrder) {
                checkCooldown++;
                if (checkCooldown >= 30) {
                    checkCooldown = 0;

                    ServerWorld serverLevel = (ServerWorld) level;
                    AlembicSaveData saveData = AlembicSaveData.get(serverLevel);

                    if (index < saveData.getAlembicList().size()) {
                        if (index == 0 && !validateRelays()) {
                            checkCooldown = 0;
                            checkedOrder = true;
                            nextAlembic = this;
                            spawnParticles = false;
                            this.setChanged();
                            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
                            return;
                        }

                        AlembicTileEntity child = nextAlembic.getChild();
                        if (child == null || child.getAlembicType() != saveData.getAlembicList().get(index)) {
                            checkCooldown = 0;
                            checkedOrder = true;
                            nextAlembic = this;
                            this.setChanged();
                            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
                            return;
                        }

                        child.spawnParticles = true;
                        nextAlembic = child;
                        level.sendBlockUpdated(child.getBlockPos(), child.getBlockState(), child.getBlockState(), Constants.BlockFlags.DEFAULT);
                        index++;
                    } else {
                        BlockState cageState = level.getBlockState(cagePos);
                        if (cageState.is(ModBlocks.BONE_CAGE.get())) {
                            BlockPos cageTilePos = cagePos;
                            BoneCageType type = cageState.getValue(BoneCageBlock.CAGE_TYPE);
                            if (type == BoneCageType.TOP) cageTilePos = cagePos.below();
                            BoneCageTileEntity boneCageTile = ModEntities.BONE_CAGE_TYPE.get().getBlockEntity(level, cageTilePos);

                            if (boneCageTile != null && boneCageTile.hasEntity()) {
                                boneCageTile.createSoul(nextAlembic.getBlockPos().relative(nextAlembic.parentDir.getOpposite()));
                            }
                        }

                        checkedOrder = true;
                        nextAlembic = this;
                        this.setChanged();
                        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
                    }
                }
            }
        }
    }

    private boolean validateRelays() {
        if (level == null || level.isClientSide() || cagePos == null) return false;
        BlockState cageState = level.getBlockState(cagePos);
        if (cageState.is(ModBlocks.BONE_CAGE.get())) {
            Direction direction = cageState.getValue(HorizontalBlock.FACING);
            BoneCageType type = cageState.getValue(BoneCageBlock.CAGE_TYPE);

            BlockPos rightPos = cagePos.relative(direction.getClockWise());
            BlockPos leftPos = cagePos.relative(direction.getCounterClockWise());
            return isValidRelay(rightPos)
                    && isValidRelay(leftPos)
                    && isValidRelay(rightPos.relative(type.getCageDirection()))
                    && isValidRelay(leftPos.relative(type.getCageDirection()));
        }

        return false;
    }

    private boolean isValidRelay(BlockPos pos) {
        SoulRelayTileEntity relay = ModEntities.SOUL_RELAY_TYPE.get().getBlockEntity(level, pos);
        return relay != null && relay.isPowered();
    }

    private void stopParticles() {
        AlembicTileEntity parent = this;
        this.spawnParticles = false;

        while (parent != null) {
            AlembicTileEntity child = parent.getChild();
            if (child != null) {
                child.spawnParticles = false;
                level.sendBlockUpdated(child.getBlockPos(), child.getBlockState(), child.getBlockState(), Constants.BlockFlags.DEFAULT);
            }

            parent = child;
        }
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

    public void setCage(BlockPos cage) {
        this.cagePos = cage;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        checkedOrder = false;
        nextAlembic = this;
        index = 0;
        checkCooldown = 0;
        if (!powered) {
            this.stopParticles();
        } else {
            spawnParticles = true;
        }

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
