package dev.onyxstudios.projecti.client.render.tile;

import dev.onyxstudios.projecti.client.models.BellowsModel;
import dev.onyxstudios.projecti.tileentity.TileEntityBellows;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class BellowsRenderer extends GeoBlockRenderer<TileEntityBellows> {

    public BellowsRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new BellowsModel());
    }
}
