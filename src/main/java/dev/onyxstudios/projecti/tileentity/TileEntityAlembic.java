package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.blocks.AlembicBlock;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class TileEntityAlembic extends TileEntityBase {

    private Direction parentDir;
    private Direction childDir;

    public TileEntityAlembic() {
        super(ModEntities.ALEMBIC_TYPE.get());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (parentDir != null)
            compound.putString("parentDir", parentDir.getName());

        if (childDir != null)
            compound.putString("childDir", childDir.getName());
        return compound;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        if (compound.contains("parentDir"))
            this.parentDir = Direction.byName(compound.getString("parentDir"));

        if (compound.contains("childDir"))
            this.childDir = Direction.byName(compound.getString("childDir"));
    }

    public void setChild(Direction direction) {
        this.childDir = direction;
    }

    public void setParent(Direction direction) {
        this.parentDir = direction;
    }

    public TileEntityAlembic getParent() {
        return getTile(parentDir);
    }

    public TileEntityAlembic getChild() {
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

    private TileEntityAlembic getTile(Direction direction) {
        if (direction == null || !hasLevel()) return null;
        TileEntity tile = level.getBlockEntity(getBlockPos().relative(direction));
        if (!(tile instanceof TileEntityAlembic)) return null;

        return (TileEntityAlembic) tile;
    }
}
