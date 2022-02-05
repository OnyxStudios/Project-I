package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.client.ModClient;
import dev.onyxstudios.projecti.tileentity.AlembicBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;

public class AlembicRenderer implements BlockEntityRenderer<AlembicBlockEntity> {

    @Override
    public void render(AlembicBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();

        boolean isFunnel = tile.getAlembicType() == AlembicType.FUNNEL;
        if (tile.hasChild()) {
            matrixStack.pushPose();
            rotateConnector(matrixStack, tile.getChildDir(), !isFunnel);
            renderer.renderModel(matrixStack.last(), buffer.getBuffer(Sheets.translucentCullBlockSheet()), null, getModel(!isFunnel), 1, 1, 1, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            matrixStack.popPose();
        }

        if (tile.hasParent()) {
            matrixStack.pushPose();
            rotateConnector(matrixStack, tile.getParentDir(), false);
            renderer.renderModel(matrixStack.last(), buffer.getBuffer(Sheets.translucentCullBlockSheet()), null, getModel(!isFunnel), 1, 1, 1, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            matrixStack.popPose();
        }
    }

    private void rotateConnector(PoseStack stack, Direction direction, boolean output) {
        rotateHorizontal(stack, direction);
        if (output) {
            stack.translate(1, 1, 0);
            stack.mulPose(Vector3f.ZP.rotationDegrees(180));
        }
    }

    protected void rotateHorizontal(PoseStack stack, Direction direction) {
        switch (direction) {
            case SOUTH -> {
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                stack.translate(-1, 0, -1);
            }
            case WEST -> {
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                stack.translate(-1, 0, 0);
            }
            case NORTH -> stack.mulPose(Vector3f.YP.rotationDegrees(0));
            case EAST -> {
                stack.mulPose(Vector3f.YP.rotationDegrees(270));
                stack.translate(0, 0, -1);
            }
        }
    }

    private BakedModel getModel(boolean shortModel) {
        return shortModel ? ModClient.SHORT_CONNECTOR : ModClient.LONG_CONNECTOR;
    }

    @Override
    public boolean shouldRenderOffScreen(AlembicBlockEntity tile) {
        return true;
    }
}
