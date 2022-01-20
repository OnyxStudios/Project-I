package dev.onyxstudios.projecti.client.render.tile;

import dev.onyxstudios.projecti.client.models.SoulRelayModel;
import dev.onyxstudios.projecti.tileentity.TileEntitySoulRelay;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SoulRelayRenderer extends GeoBlockRenderer<TileEntitySoulRelay> {

    public SoulRelayRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new SoulRelayModel());
    }
}
