package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;

public class TileEntityBoneCage extends TileEntityBase {

    private EntityType<?> storedEntity;
    private CompoundNBT entityTag;

    private boolean powered;

    public TileEntityBoneCage() {
        super(ModEntities.BONE_CAGE_TYPE.get());
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public boolean isPowered() {
        return powered;
    }
}
