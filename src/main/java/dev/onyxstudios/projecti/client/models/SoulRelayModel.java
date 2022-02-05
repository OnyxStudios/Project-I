package dev.onyxstudios.projecti.client.models;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.tileentity.SoulRelayBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoulRelayModel extends AnimatedGeoModel<SoulRelayBlockEntity> {

    @Override
    public ResourceLocation getModelLocation(SoulRelayBlockEntity object) {
        return new ResourceLocation(ProjectI.MODID, "geo/soul_relay.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SoulRelayBlockEntity object) {
        return new ResourceLocation("textures/block/stone.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SoulRelayBlockEntity animatable) {
        return new ResourceLocation(ProjectI.MODID, "animations/soul_relay.animation.json");
    }
}
