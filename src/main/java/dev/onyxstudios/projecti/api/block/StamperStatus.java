package dev.onyxstudios.projecti.api.block;

import net.minecraft.util.StringRepresentable;

public enum StamperStatus implements StringRepresentable {
    OPEN,
    CLOSED;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
