package dev.onyxstudios.projecti.client.render.tile;

import dev.onyxstudios.projecti.client.models.SoulRelayModel;
import dev.onyxstudios.projecti.tileentity.SoulRelayTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SoulRelayRenderer extends GeoBlockRenderer<SoulRelayTileEntity> {

    public SoulRelayRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new SoulRelayModel());
    }
}
