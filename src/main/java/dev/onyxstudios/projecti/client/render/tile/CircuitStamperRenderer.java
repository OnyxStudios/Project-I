package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.projecti.tileentity.TileEntityStamper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.vector.Vector3f;

public class CircuitStamperRenderer extends TileEntityRenderer<TileEntityStamper> {

    public CircuitStamperRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(TileEntityStamper stamper, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.15, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-(stamper.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().get2DDataValue() * 90)));
        double slotOffset = 0.25;
        drawStack(stamper.getInventory().getStackInSlot(0), matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, slotOffset, 0, slotOffset);
        drawStack(stamper.getInventory().getStackInSlot(1), matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, -slotOffset, 0, slotOffset);
        drawStack(stamper.getInventory().getStackInSlot(2), matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, slotOffset, 0, -slotOffset);
        drawStack(stamper.getInventory().getStackInSlot(3), matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, -slotOffset, 0, -slotOffset);
        drawStack(stamper.getInventory().getStackInSlot(4), matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, 0, 0.05, 0);
        matrixStack.popPose();
    }

    private void drawStack(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay, double x, double y, double z) {
        if (stack.isEmpty()) return;
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrixStack, renderTypeBuffer);
        matrixStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityStamper tile) {
        return true;
    }
}
