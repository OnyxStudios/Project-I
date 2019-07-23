package nerdhub.projecti.blocks.fluid;

import net.minecraft.util.ResourceLocation;

public class FluidBase extends net.minecraftforge.fluids.Fluid {

    public FluidBase(ResourceLocation name) {
        super(name.getPath(), new ResourceLocation(name.getNamespace(), "blocks/fluids/" + name.getPath() + "_still"), new ResourceLocation(name.getNamespace(), "blocks/fluids/" + name.getPath() + "_flowing"));
    }
}
