package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static Tags.IOptionalNamedTag<Fluid> MOLTEN_BLUE_CRYSTAL = FluidTags.createOptional(new ResourceLocation(ProjectI.MODID, "molten_blue_crystal"));
}
