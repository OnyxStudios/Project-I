package nerdhub.projecti.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.ProjectI;
import nerdhub.projecti.client.models.BlueCrystalModel;
import nerdhub.projecti.tiles.TileEntityCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

public class BlueCrystalRenderer extends TileEntityRenderer<TileEntityCrystal> {

    public static final ResourceLocation WHITE = new ResourceLocation(ProjectI.MODID, "textures/blocks/white.png");

    @Override
    public void render(TileEntityCrystal tile, double x, double y, double z, float partialTicks, int stage) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlphaTest();
        GlStateManager.translated(x + 0.5, y + 2, z + 0.5);
        GlStateManager.rotatef(180, 1, 0, 0);
        GlStateManager.scaled(1.2, 1.3, 1.2);

        GlStateManager.rotated(tile.randomDir.getHorizontalIndex() * 90, 0, 1, 0);
        Minecraft.getInstance().getTextureManager().bindTexture(WHITE);
        BlueCrystalModel.INSTANCE.render(tile);
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityCrystal tileEntityCrystal) {
        return true;
    }
}
