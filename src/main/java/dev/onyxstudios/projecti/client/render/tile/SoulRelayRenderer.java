package dev.onyxstudios.projecti.client.render.tile;

import dev.onyxstudios.projecti.client.models.SoulRelayModel;
import dev.onyxstudios.projecti.tileentity.SoulRelayBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SoulRelayRenderer extends GeoBlockRenderer<SoulRelayBlockEntity> {

    public SoulRelayRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new SoulRelayModel());
    }
}
