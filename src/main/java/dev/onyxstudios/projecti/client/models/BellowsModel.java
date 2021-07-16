package dev.onyxstudios.projecti.client.models;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.tileentity.TileEntityBellows;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BellowsModel extends AnimatedGeoModel<TileEntityBellows> {

    @Override
    public ResourceLocation getModelLocation(TileEntityBellows object) {
        return new ResourceLocation(ProjectI.MODID, "geo/bellows.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityBellows object) {
        return new ResourceLocation(ProjectI.MODID, "textures/blocks/bellows.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityBellows animatable) {
        return new ResourceLocation(ProjectI.MODID, "animations/bellows.animation.json");
    }
}
