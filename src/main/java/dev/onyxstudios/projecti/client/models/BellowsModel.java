package dev.onyxstudios.projecti.client.models;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.tileentity.BellowsTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BellowsModel extends AnimatedGeoModel<BellowsTileEntity> {

    @Override
    public ResourceLocation getModelLocation(BellowsTileEntity object) {
        return new ResourceLocation(ProjectI.MODID, "geo/bellows.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BellowsTileEntity object) {
        return new ResourceLocation(ProjectI.MODID, "textures/blocks/bellows.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BellowsTileEntity animatable) {
        return new ResourceLocation(ProjectI.MODID, "animations/bellows.animation.json");
    }
}
