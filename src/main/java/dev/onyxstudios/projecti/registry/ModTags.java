package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static Tags.IOptionalNamedTag<Fluid> MOLTEN_BLUE_CRYSTAL = FluidTags.createOptional(new ResourceLocation(ProjectI.MODID, "molten_blue_crystal"));
}
