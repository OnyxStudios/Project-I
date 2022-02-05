package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.onyxstudios.projecti.client.models.BlueCrystalModel;
import dev.onyxstudios.projecti.tileentity.CrystalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class BlueCrystalRenderer implements BlockEntityRenderer<CrystalBlockEntity> {

    @Override
    public void render(CrystalBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        VertexConsumer vertexBuilder = BlueCrystalModel.CRYSTAL_RESOURCE_LOCATION.buffer(buffer, RenderType::entityTranslucent);

        matrixStack.pushPose();
        //Translate extra 0.01 to prevent z-fighting
        matrixStack.translate(0.5, 1.51, 0.5);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tile.randomDir.get2DDataValue() * 90));
        BlueCrystalModel.INSTANCE.renderToBuffer(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.001, 0.5);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tile.randomDir.get2DDataValue() * 90));
        BlueCrystalModel.INSTANCE.renderPillars(tile, matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(CrystalBlockEntity tile) {
        return true;
    }
}
