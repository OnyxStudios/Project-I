package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.onyxstudios.projecti.client.models.BlueCrystalModel;
import dev.onyxstudios.projecti.tileentity.TileEntityCrystal;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;

public class BlueCrystalRenderer extends TileEntityRenderer<TileEntityCrystal> {

    public BlueCrystalRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileEntityCrystal tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        IVertexBuilder vertexBuilder = BlueCrystalModel.CRYSTAL_RESOURCE_LOCATION.buffer(buffer, RenderType::entityTranslucent);

        matrixStack.pushPose();
        //Translate extra 0.01 to prevent z-fighting
        matrixStack.translate(0.5, 1.51, 0.5);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tile.randomDir.get2DDataValue() * 90));
        BlueCrystalModel.INSTANCE.renderToBuffer(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tile.randomDir.get2DDataValue() * 90));
        BlueCrystalModel.INSTANCE.renderCrystals(tile, matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityCrystal tile) {
        return true;
    }
}
