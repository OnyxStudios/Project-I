package nerdhub.projecti.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    public static void translateAgainstPlayer(BlockPos pos, boolean offset) {
        double x = pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
        double y = pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
        double z = pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;

        if (offset) {
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
        } else {
            GlStateManager.translated(x, y, z);
        }
    }

    public static void renderFluid(FluidStack fluid, BlockPos pos, double x, double y, double z, double width, double height, double length) {
        double x1 = (1d - width) / 2d;
        double y1 = (1d - height) / 2d;
        double z1 = (1d - length) / 2d;
        renderFluid(fluid, pos, x, y, z, x1, y1, z1, 1d - x1, 1d - y1, 1d - z1);
    }

    public static void renderFluid(FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2) {
        int color = 0xFFFFFFFF;
        renderFluid(fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color);
    }

    public static void renderFluid(FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        Minecraft mc = Minecraft.getInstance();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int brightness = mc.world.getCombinedLight(pos, fluid.getFluid().getLuminosity());

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        setupRenderState();
        GlStateManager.translated(x, y, z);

        TextureAtlasSprite still = mc.getTextureMap().getSprite(fluid.getFluid().getStill(fluid));
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.DOWN, color, brightness);
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.NORTH, color, brightness);
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.EAST, color, brightness);
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.SOUTH, color, brightness);
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.WEST, color, brightness);
        addTexturedQuad(buffer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.UP, color, brightness);

        tessellator.draw();

        cleanupRenderState();
    }

    public static void setupRenderState() {
        GlStateManager.pushMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        } else {
            GL11.glShadeModel(GL11.GL_FLAT);
        }
    }

    public static void addTexturedQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double z, double width, double height, double length, Direction face, int color, int brightness) {
        if (sprite == null) {
            return;
        }

        int firstLightValue = brightness >> 0x10 & 0xFFFF;
        int secondLightValue = brightness & 0xFFFF;
        int alpha = color >> 24 & 0xFF;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;

        addTextureQuad(buffer, sprite, x, y, z, width, height, length, face, red, green, blue, alpha, firstLightValue, secondLightValue);
    }

    public static void addTextureQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double z, double width, double height, double length, Direction face, int red, int green, int blue, int alpha, int light1, int light2) {
        double minU;
        double maxU;
        double minV;
        double maxV;

        double size = 16f;

        double x2 = x + width;
        double y2 = y + height;
        double z2 = z + length;

        double u = x % 1d;
        double u1 = u + width;

        while (u1 > 1f) {
            u1 -= 1f;
        }

        double vy = y % 1d;
        double vy1 = vy + height;

        while (vy1 > 1f) {
            vy1 -= 1f;
        }

        double vz = z % 1d;
        double vz1 = vz + length;

        while (vz1 > 1f) {
            vz1 -= 1f;
        }

        switch (face) {
            case DOWN:
            case UP:
                minU = sprite.getInterpolatedU(u * size);
                maxU = sprite.getInterpolatedU(u1 * size);
                minV = sprite.getInterpolatedV(vz * size);
                maxV = sprite.getInterpolatedV(vz1 * size);
                break;
            case NORTH:
            case SOUTH:
                minU = sprite.getInterpolatedU(u1 * size);
                maxU = sprite.getInterpolatedU(u * size);
                minV = sprite.getInterpolatedV(vy * size);
                maxV = sprite.getInterpolatedV(vy1 * size);
                break;
            case WEST:
            case EAST:
                minU = sprite.getInterpolatedU(vz1 * size);
                maxU = sprite.getInterpolatedU(vz * size);
                minV = sprite.getInterpolatedV(vy * size);
                maxV = sprite.getInterpolatedV(vy1 * size);
                break;
            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }

        switch (face) {
            case DOWN:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;
            case UP:
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case NORTH:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
            case SOUTH:
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case WEST:
                buffer.pos(x, y, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y, z2).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z2).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x, y2, z).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case EAST:
                buffer.pos(x2, y, z).color(red, green, blue, alpha).tex(minU, maxV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z).color(red, green, blue, alpha).tex(minU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y2, z2).color(red, green, blue, alpha).tex(maxU, minV).lightmap(light1, light2).endVertex();
                buffer.pos(x2, y, z2).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
        }
    }

    public static void cleanupRenderState() {
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
    }
}
