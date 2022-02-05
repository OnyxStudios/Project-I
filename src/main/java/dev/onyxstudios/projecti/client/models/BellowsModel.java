package dev.onyxstudios.projecti.client.models;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.tileentity.BellowsBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BellowsModel extends AnimatedGeoModel<BellowsBlockEntity> {

    @Override
    public ResourceLocation getModelLocation(BellowsBlockEntity object) {
        return new ResourceLocation(ProjectI.MODID, "geo/bellows.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BellowsBlockEntity object) {
        return new ResourceLocation(ProjectI.MODID, "textures/blocks/bellows.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BellowsBlockEntity animatable) {
        return new ResourceLocation(ProjectI.MODID, "animations/bellows.animation.json");
    }
}
