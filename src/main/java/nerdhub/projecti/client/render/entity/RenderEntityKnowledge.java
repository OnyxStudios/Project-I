package nerdhub.projecti.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.ProjectI;
import nerdhub.projecti.client.render.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderEntityKnowledge extends LivingRenderer {

    public static final ResourceLocation BASE = new ResourceLocation(ProjectI.MODID, "textures/gui/knowledge_base.png");
    public static final ResourceLocation RING = new ResourceLocation(ProjectI.MODID, "textures/gui/knowledge_ring.png");

    public RenderEntityKnowledge(EntityRendererManager rendererManager) {
        super(rendererManager, null, 0);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.4, z);
        this.bindTexture(BASE);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.rotated(180 - mc.getRenderManager().playerViewY, 0, 1, 0);
        GlStateManager.scaled(2.5, 2.5, 2.5);
        GlStateManager.color4f(1, 1, 1, 0.85f);
        drawQuad();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        ShaderHelper.useShader(ShaderHelper.bloomShader, RenderEntitySoul.generateBloomCallback(RING, new float[] {0.5f, 0.5f, 0.5f, 0.8f}, new float[] {1, 1, 1, 0.5f}));
        this.bindTexture(RING);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.rotated(180 - mc.getRenderManager().playerViewY, 0, 1, 0);
        GlStateManager.color4f(1, 1, 1, 0.85f);

        long angle = (System.currentTimeMillis() / 20) % 360;
        GlStateManager.translated(0, 1, 0);
        GlStateManager.rotated(angle, 0, 0, 1);
        GlStateManager.translated(0, -1, 0);

        GlStateManager.scaled(4.5, 4.5, 4.5);

        drawQuad();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();
        ShaderHelper.releaseShader();
        GlStateManager.popMatrix();
    }

    public void drawQuad() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0D, 0D).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(0D, 1D).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(1D, 1D).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(1D, 0D).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();

    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
