package dev.onyxstudios.projecti.client.models;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.tileentity.SoulRelayTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoulRelayModel extends AnimatedGeoModel<SoulRelayTileEntity> {

    @Override
    public ResourceLocation getModelLocation(SoulRelayTileEntity object) {
        return new ResourceLocation(ProjectI.MODID, "geo/soul_relay.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SoulRelayTileEntity object) {
        return new ResourceLocation("textures/block/stone.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SoulRelayTileEntity animatable) {
        return new ResourceLocation(ProjectI.MODID, "animations/soul_relay.animation.json");
    }
}
