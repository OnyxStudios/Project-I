package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.client.ModClient;
import dev.onyxstudios.projecti.tileentity.AlembicTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class AlembicRenderer extends TileEntityRenderer<AlembicTileEntity> {

    public AlembicRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(AlembicTileEntity tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockModelRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();

        boolean isFunnel = tile.getAlembicType() == AlembicType.FUNNEL;
        if (tile.hasChild()) {
            matrixStack.pushPose();
            rotateConnector(matrixStack, tile.getChildDir(), !isFunnel);
            renderer.renderModel(matrixStack.last(), buffer.getBuffer(Atlases.translucentCullBlockSheet()), null, getModel(!isFunnel), 1, 1, 1, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            matrixStack.popPose();
        }

        if (tile.hasParent()) {
            matrixStack.pushPose();
            rotateConnector(matrixStack, tile.getParentDir(), false);
            renderer.renderModel(matrixStack.last(), buffer.getBuffer(Atlases.translucentCullBlockSheet()), null, getModel(!isFunnel), 1, 1, 1, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
            matrixStack.popPose();
        }
    }

    private void rotateConnector(MatrixStack stack, Direction direction, boolean output) {
        rotateHorizontal(stack, direction);
        if (output) {
            stack.translate(1, 1, 0);
            stack.mulPose(Vector3f.ZP.rotationDegrees(180));
        }
    }

    protected void rotateHorizontal(MatrixStack stack, Direction direction) {
        switch (direction) {
            case SOUTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                stack.translate(-1, 0, -1);
                break;
            case WEST:
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                stack.translate(-1, 0, 0);
                break;
            case NORTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(0));
                break;
            case EAST:
                stack.mulPose(Vector3f.YP.rotationDegrees(270));
                stack.translate(0, 0, -1);
                break;
        }
    }

    private IBakedModel getModel(boolean shortModel) {
        return shortModel ? ModClient.SHORT_CONNECTOR : ModClient.LONG_CONNECTOR;
    }

    @Override
    public boolean shouldRenderOffScreen(AlembicTileEntity tile) {
        return true;
    }
}
