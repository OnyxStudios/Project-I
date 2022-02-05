package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.onyxstudios.projecti.blockentity.StamperBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CircuitStamperRenderer implements BlockEntityRenderer<StamperBlockEntity> {

    @Override
    public void render(StamperBlockEntity stamper, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLight, int combinedOverlay) {
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

    private void drawStack(ItemStack stack, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLight, int combinedOverlay, double x, double y, double z) {
        if (stack.isEmpty()) return;
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE, combinedLight, combinedOverlay, matrixStack, renderTypeBuffer, 0);
        matrixStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(StamperBlockEntity tile) {
        return true;
    }
}
