package dev.onyxstudios.projecti.api.block;

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

public enum BoneCageType implements IStringSerializable {
    TOP(Direction.DOWN),
    BOTTOM(Direction.UP);

    private final Direction cageDirection;

    BoneCageType(Direction partDirection) {
        this.cageDirection = partDirection;
    }

    public Direction getCageDirection() {
        return cageDirection;
    }

    @Override
    public String getSerializedName() {
        return this.toString().toLowerCase();
    }
}
