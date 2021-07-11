package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {

    public static final DeferredRegister<ContainerType<?>> containerRegistry = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProjectI.MODID);
    public static final DeferredRegister<TileEntityType<?>> tileRegistry = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ProjectI.MODID);
    public static final DeferredRegister<EntityType<?>> entityRegistry = DeferredRegister.create(ForgeRegistries.ENTITIES, ProjectI.MODID);
}
