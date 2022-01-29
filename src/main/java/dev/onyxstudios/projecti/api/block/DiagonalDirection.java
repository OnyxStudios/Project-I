package dev.onyxstudios.projecti.api.block;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public enum DiagonalDirection {
    NORTH_EAST(Direction.EAST, Direction.NORTH),
    SOUTH_EAST(Direction.EAST, Direction.SOUTH),
    NORTH_WEST(Direction.WEST, Direction.NORTH),
    SOUTH_WEST(Direction.WEST, Direction.SOUTH);

    public static final DiagonalDirection[] VALUES = DiagonalDirection.values();

    private final int xOffset;
    private final int zOffset;

    DiagonalDirection(Direction xDir, Direction zDir) {
        this.xOffset = xDir.getNormal().getX();
        this.zOffset = zDir.getNormal().getZ();
    }

    public BlockPos offset(BlockPos pos) {
        return pos.offset(xOffset, 0, zOffset);
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getZOffset() {
        return zOffset;
    }
}
