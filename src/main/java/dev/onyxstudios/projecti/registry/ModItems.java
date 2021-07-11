package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectI.MODID);
}
