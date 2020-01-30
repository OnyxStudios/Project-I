package nerdhub.projecti.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.tiles.TileEntityBlowMold;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

import static nerdhub.projecti.client.render.RenderHelper.renderFluid;
import static nerdhub.projecti.client.render.RenderHelper.translateAgainstPlayer;

public class BlowMoldRenderer extends TileEntityRenderer<TileEntityBlowMold> {

    @Override
    public void render(TileEntityBlowMold tile, double x, double y, double z, float partialTicks, int stage) {
        long angle = (System.currentTimeMillis() / 10) % 360;

        if(tile.tank.getFluidAmount() >= 1000) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            translateAgainstPlayer(tile.getPos(), false);
            renderFluid(tile.tank.getFluid(), tile.getPos(), 0.18d, 0.35d, 0.18d, 0.016d, 0.0d, 0.016d, 0.68d, 1000 / tile.tank.getCapacity() * 0.5d, 0.68d);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        if(!tile.inventory.getStackInSlot(0).isEmpty()) {
            GlStateManager.translated(x + 0.5, y + 0.9, z + 0.35);
            GlStateManager.rotated(90, 1, 0, 0);
            GlStateManager.scaled(1.2, 1.2, 1.2);
            Minecraft.getInstance().getItemRenderer().renderItem(tile.inventory.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);

            if(!tile.inventory.getStackInSlot(1).isEmpty()) {
                //Undo then render result
                GlStateManager.rotated(-90, 1, 0 ,0);
                GlStateManager.scaled(0.5, 0.5, 0.35);
                GlStateManager.translated(0, 0.5, 0.3);
                GlStateManager.rotated(angle, 0, 1, 0);
                Minecraft.getInstance().getItemRenderer().renderItem(tile.inventory.getStackInSlot(1), ItemCameraTransforms.TransformType.GROUND);
            }
        }
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityBlowMold tile) {
        return super.isGlobalRenderer(tile);
    }
}
