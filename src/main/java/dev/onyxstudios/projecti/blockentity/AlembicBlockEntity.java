package dev.onyxstudios.projecti.blockentity;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.api.block.BoneCageType;
import dev.onyxstudios.projecti.blocks.AlembicBlock;
import dev.onyxstudios.projecti.blocks.BoneCageBlock;
import dev.onyxstudios.projecti.data.AlembicSaveData;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlembicBlockEntity extends BaseBlockEntity {

    private Direction parentDir;
    private Direction childDir;
    private BlockPos cagePos;

    private boolean powered;
    private boolean spawnParticles;
    private boolean checkedOrder;
    private int age;

    private AlembicBlockEntity nextAlembic = this;
    private int checkCooldown;
    private int index;

    public AlembicBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.ALEMBIC, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (parentDir != null)
            tag.putString("parentDir", parentDir.getName());

        if (childDir != null)
            tag.putString("childDir", childDir.getName());

        if (cagePos != null)
            tag.put("cagePos", NbtUtils.writeBlockPos(cagePos));

        tag.putBoolean("powered", powered);
        tag.putBoolean("spawnParticles", spawnParticles);
        tag.putBoolean("checkedOrder", checkedOrder);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.parentDir = null;
        if (tag.contains("parentDir"))
            this.parentDir = Direction.byName(tag.getString("parentDir"));

        this.childDir = null;
        if (tag.contains("childDir"))
            this.childDir = Direction.byName(tag.getString("childDir"));

        cagePos = null;
        if (tag.contains("cagePos"))
            cagePos = NbtUtils.readBlockPos(tag.getCompound("cagePos"));

        powered = tag.getBoolean("powered");
        spawnParticles = tag.getBoolean("spawnParticles");
        checkedOrder = tag.getBoolean("checkedOrder");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlembicBlockEntity blockEntity) {
        blockEntity.age++;
        if (blockEntity.spawnParticles) {
            ServerLevel serverLevel = (ServerLevel) level;
            if (blockEntity.age % 10 == 0) {
                serverLevel.sendParticles(ModParticles.GLOW.get(), pos.getX() + 0.5f, pos.getY() + 0.9f, pos.getZ() + 0.5f, 0, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, 1);
            }
        }

        if (blockEntity.getAlembicType() == AlembicType.FUNNEL && blockEntity.cagePos != null) {
            if (blockEntity.powered && !blockEntity.checkedOrder) {
                blockEntity.checkCooldown++;
                if (blockEntity.checkCooldown >= 30) {
                    blockEntity.checkCooldown = 0;

                    ServerLevel serverLevel = (ServerLevel) level;
                    AlembicSaveData saveData = AlembicSaveData.get(serverLevel);

                    if (blockEntity.index < saveData.getAlembicList().size()) {
                        if (blockEntity.index == 0 && !blockEntity.validateRelays()) {
                            blockEntity.checkCooldown = 0;
                            blockEntity.checkedOrder = true;
                            blockEntity.nextAlembic = blockEntity;
                            blockEntity.spawnParticles = false;
                            blockEntity.setChanged();
                            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                            return;
                        }

                        AlembicBlockEntity child = blockEntity.nextAlembic.getChild();
                        if (child == null || child.getAlembicType() != saveData.getAlembicList().get(blockEntity.index)) {
                            blockEntity.checkCooldown = 0;
                            blockEntity.checkedOrder = true;
                            blockEntity.nextAlembic = blockEntity;
                            blockEntity.setChanged();
                            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                            return;
                        }

                        child.spawnParticles = true;
                        blockEntity.nextAlembic = child;
                        level.sendBlockUpdated(child.getBlockPos(), child.getBlockState(), child.getBlockState(), Block.UPDATE_ALL);
                        blockEntity.index++;
                    } else {
                        BlockState cageState = level.getBlockState(blockEntity.cagePos);
                        if (cageState.is(ModBlocks.BONE_CAGE)) {
                            BlockPos cageTilePos = blockEntity.cagePos;
                            BoneCageType type = cageState.getValue(BoneCageBlock.CAGE_TYPE);
                            if (type == BoneCageType.TOP) cageTilePos = blockEntity.cagePos.below();
                            BoneCageBlockEntity boneCageTile = ModEntities.BONE_CAGE.getBlockEntity(level, cageTilePos);

                            if (boneCageTile != null && boneCageTile.hasEntity()) {
                                boneCageTile.createSoul(blockEntity.nextAlembic.getBlockPos().relative(blockEntity.nextAlembic.parentDir.getOpposite()));
                            }
                        }

                        blockEntity.checkedOrder = true;
                        blockEntity.nextAlembic = blockEntity;
                        blockEntity.setChanged();
                        level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    private boolean validateRelays() {
        if (level == null || level.isClientSide() || cagePos == null) return false;
        BlockState cageState = level.getBlockState(cagePos);
        if (cageState.is(ModBlocks.BONE_CAGE)) {
            Direction direction = cageState.getValue(HorizontalDirectionalBlock.FACING);
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
        SoulRelayBlockEntity relay = ModEntities.SOUL_RELAY.getBlockEntity(level, pos);
        return relay != null && relay.isPowered();
    }

    private void stopParticles() {
        AlembicBlockEntity parent = this;
        this.spawnParticles = false;

        while (parent != null) {
            AlembicBlockEntity child = parent.getChild();
            if (child != null) {
                child.spawnParticles = false;
                level.sendBlockUpdated(child.getBlockPos(), child.getBlockState(), child.getBlockState(), Block.UPDATE_ALL);
            }

            parent = child;
        }
    }

    public void removeChild() {
        if (childDir == null || level == null) return;
        childDir = null;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void removeParent() {
        if (parentDir == null || level == null) return;
        parentDir = null;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public AlembicBlockEntity getParent() {
        return getTile(parentDir);
    }

    public AlembicBlockEntity getChild() {
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
            AlembicBlockEntity parent = getParent();
            if (parent == null) {
                parentDir = null;
                validatePath();
            } else if (parent.getAlembicType() != AlembicType.FUNNEL) {
                parent.validatePath();
            }
        }

        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Block.UPDATE_ALL);
    }

    private AlembicBlockEntity getTile(Direction direction) {
        if (direction == null || level == null) return null;
        BlockEntity tile = level.getBlockEntity(getBlockPos().relative(direction));
        if (!(tile instanceof AlembicBlockEntity)) return null;

        return (AlembicBlockEntity) tile;
    }
}
