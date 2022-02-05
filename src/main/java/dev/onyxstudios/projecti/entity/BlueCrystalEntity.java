package dev.onyxstudios.projecti.entity;

import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BlueCrystalEntity extends ItemEntity {

    private int inLavaTicks;

    public BlueCrystalEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BlueCrystalEntity(Level level, ItemStack stack, ItemEntity originalEntity) {
        super(ModEntities.BLUE_CRYSTAL_ENTITY, level);
        load(originalEntity.saveWithoutId(new CompoundTag()));
        setItem(stack);
    }

    @Override
    public void tick() {
        super.tick();

        if (isInLava()) {
            inLavaTicks++;

            if (inLavaTicks >= 60 && !level.isClientSide) {
                level.setBlock(blockPosition(), ModBlocks.MOLTEN_BLUE_CRYSTAL_BLOCK.defaultBlockState(), 3);
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
