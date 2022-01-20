package dev.onyxstudios.projecti.entity;

import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlueCrystalEntity extends ItemEntity {

    private int inLavaTicks;

    public BlueCrystalEntity(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlueCrystalEntity(World world, ItemStack stack, ItemEntity originalEntity) {
        super(ModEntities.ENTITY_BLUE_CRYSTAL.get(), world);
        load(originalEntity.saveWithoutId(new CompoundNBT()));
        setItem(stack);
    }

    @Override
    public void tick() {
        super.tick();

        if (isInLava()) {
            inLavaTicks++;

            if (inLavaTicks >= 60 && !level.isClientSide) {
                level.setBlock(blockPosition(), ModBlocks.MOLTEN_BLUE_CRYSTAL_BLOCK.get().defaultBlockState(), 3);
                this.remove();
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
