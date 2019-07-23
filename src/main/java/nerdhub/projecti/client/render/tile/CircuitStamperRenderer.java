package nerdhub.projecti.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.blocks.BlockCircuitStamper;
import nerdhub.projecti.tiles.TileEntityStamper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class CircuitStamperRenderer extends TileEntityRenderer<TileEntityStamper> {

    @Override
    public void render(TileEntityStamper stamper, double x, double y, double z, float partialTicks, int stage) {
        GlStateManager.pushMatrix();
        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translated(x + 0.5, y + 0.15, z + 0.5);
        GlStateManager.rotated(-(stamper.getBlockState().get(BlockCircuitStamper.FACING).getOpposite().getHorizontalIndex() * 90), 0, 1, 0);
        double slotOffset = 0.25;
        drawStack(stamper.inventory.getStackInSlot(0), slotOffset, 0, slotOffset);
        drawStack(stamper.inventory.getStackInSlot(1), -slotOffset, 0, slotOffset);
        drawStack(stamper.inventory.getStackInSlot(2), slotOffset, 0, -slotOffset);
        drawStack(stamper.inventory.getStackInSlot(3), -slotOffset, 0, -slotOffset);
        drawStack(stamper.inventory.getStackInSlot(4), 0, 0.05, 0);
        GlStateManager.popMatrix();
    }

    public void drawStack(ItemStack stack, double x, double y, double z) {
        if(stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translated(x, y, z);
        GlStateManager.rotated(90, 1, 0, 0);
        GlStateManager.scaled(0.25, 0.25, 0.25);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityStamper stamper) {
        return true;
    }
}
