package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import dev.onyxstudios.projecti.tileentity.BlowMoldBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BlowMoldRenderer implements BlockEntityRenderer<BlowMoldBlockEntity> {

    @Override
    public void render(BlowMoldBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        long angle = (System.currentTimeMillis() / 10) % 360;

        if (tile.getTank().getFluidAmount() >= 1000) {
            renderFluidQuad(tile.getTank().getFluid(), matrixStack, buffer, combinedLight);
        }

        ItemStack stack = tile.getInventory().getStackInSlot(0);
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 0.9, 0.32);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
            matrixStack.scale(1.5f, 1.5f, 1.5f);
            mc.getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer, 0);
            matrixStack.popPose();
        }

        ItemStack stack2 = tile.getInventory().getStackInSlot(1);
        if (!stack2.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 1.25f, 0.5);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            mc.getItemRenderer().renderStatic(stack2, ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer, 0);
            matrixStack.popPose();
        }
    }

    private void renderFluidQuad(FluidStack fluid, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        Minecraft mc = Minecraft.getInstance();
        VertexConsumer builder = buffer.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite =
                mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluid.getFluid().getAttributes().getStillTexture());

        matrixStack.pushPose();
        matrixStack.translate(-0.125f, 0.75f, -0.125f);
        vertex(matrixStack.last().pose(), matrixStack.last().normal(), builder, 0.2f, 0, 0.2f, sprite.getU0(), sprite.getV1(), fluid.getFluid().getAttributes().getColor(), light);
        vertex(matrixStack.last().pose(), matrixStack.last().normal(), builder, 0.2f, 0, 1, sprite.getU0(), sprite.getV0(), fluid.getFluid().getAttributes().getColor(), light);
        vertex(matrixStack.last().pose(), matrixStack.last().normal(), builder, 1, 0, 1, sprite.getU1(), sprite.getV0(), fluid.getFluid().getAttributes().getColor(), light);
        vertex(matrixStack.last().pose(), matrixStack.last().normal(), builder, 1, 0, 0.2f, sprite.getU1(), sprite.getV1(), fluid.getFluid().getAttributes().getColor(), light);
        matrixStack.popPose();
    }

    private void vertex(Matrix4f stack, Matrix3f normal, VertexConsumer builder, float x, float y, float z, float u, float v, int color, int light) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        int alpha = color >> 24 & 0xFF;

        builder.vertex(stack, x, y, z)
                .color(red, green, blue, alpha)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 1, 1, 1)
                .endVertex();
    }
}
