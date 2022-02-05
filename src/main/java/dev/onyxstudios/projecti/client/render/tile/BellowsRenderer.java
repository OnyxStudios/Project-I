package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.onyxstudios.projecti.client.models.BellowsModel;
import dev.onyxstudios.projecti.tileentity.BellowsBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class BellowsRenderer extends GeoBlockRenderer<BellowsBlockEntity> {

    public BellowsRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new BellowsModel());
    }

    @Override
    public void rotateBlock(Direction facing, PoseStack stack) {
        //Because the model is facing west by default, always rotate it one clockwise to get it north to start
        super.rotateBlock(facing.getClockWise(), stack);
    }
}
