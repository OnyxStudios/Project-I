package dev.onyxstudios.projecti.api.block;

import net.minecraft.util.IStringSerializable;

public enum StamperStatus implements IStringSerializable {
    OPEN,
    CLOSED;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
