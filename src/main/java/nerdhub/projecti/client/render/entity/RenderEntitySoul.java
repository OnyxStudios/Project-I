package nerdhub.projecti.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.entity.EntitySoulBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderEntitySoul extends LivingRenderer {

    public RenderEntitySoul(EntityRendererManager rendererManager) {
        super(rendererManager, null, 0);
    }

    @Override
    public void doRender(LivingEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        LivingEntity soulEntity = this.getSoulToRender(entity.world, (EntitySoulBase) entity);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.02f);
        GlStateManager.color4f(1, 1, 1, 0.4f);
        EntityRenderer render = Minecraft.getInstance().getRenderManager().getEntityRenderObject(soulEntity);
        render.bindEntityTexture(soulEntity);
        render.doRender(soulEntity, x, y, z, yaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.popMatrix();
    }

    public LivingEntity getSoulToRender(World world, EntitySoulBase entity) {
        LivingEntity soulEntity = (LivingEntity) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity.getEntityType())).create(world);
        soulEntity.deserializeNBT(entity.serializeNBT());

        return soulEntity;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
