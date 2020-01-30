package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

    public static final Tag<Fluid> MOLTEN_BLUE_CRYSTAL = new FluidTags.Wrapper(new ResourceLocation(ProjectI.MODID, "molten_blue_crystal"));
}
