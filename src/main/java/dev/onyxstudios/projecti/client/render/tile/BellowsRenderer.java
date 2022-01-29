package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.projecti.client.models.BellowsModel;
import dev.onyxstudios.projecti.tileentity.BellowsTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class BellowsRenderer extends GeoBlockRenderer<BellowsTileEntity> {

    public BellowsRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new BellowsModel());
    }

    @Override
    public void rotateBlock(Direction facing, MatrixStack stack) {
        //Because the model is facing west by default, always rotate it one clockwise to get it north to start
        super.rotateBlock(facing.getClockWise(), stack);
    }
}
