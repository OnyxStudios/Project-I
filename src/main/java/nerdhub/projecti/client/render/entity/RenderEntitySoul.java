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
        soulEntity.setPosition(entity.posX, entity.posY, entity.posZ);
        soulEntity.lastTickPosX = entity.lastTickPosX;
        soulEntity.lastTickPosY = entity.lastTickPosY;
        soulEntity.lastTickPosZ = entity.lastTickPosZ;
        soulEntity.setMotion(entity.getMotion());
        soulEntity.onGround = entity.onGround;
        soulEntity.prevPosX = entity.prevPosX;
        soulEntity.prevPosY = entity.prevPosY;
        soulEntity.prevPosZ = entity.prevPosZ;
        soulEntity.rotationPitch = entity.rotationPitch;
        soulEntity.rotationYaw = entity.rotationYaw;
        soulEntity.rotationYawHead = entity.rotationYawHead;
        soulEntity.prevRotationPitch = entity.prevRotationPitch;
        soulEntity.prevRotationYaw = entity.prevRotationYaw;
        soulEntity.prevRotationYawHead = entity.prevRotationYawHead;
        soulEntity.limbSwing = entity.limbSwing;
        soulEntity.limbSwingAmount = entity.limbSwingAmount;
        soulEntity.prevLimbSwingAmount = entity.prevLimbSwingAmount;
        soulEntity.isSwingInProgress = entity.isSwingInProgress;
        soulEntity.swingProgress = entity.swingProgress;
        soulEntity.prevSwingProgress = entity.prevSwingProgress;
        soulEntity.renderYawOffset = entity.renderYawOffset;
        soulEntity.prevRenderYawOffset = entity.prevRenderYawOffset;
        soulEntity.ticksExisted = entity.ticksExisted;
        soulEntity.removed = false;
        soulEntity.isAirBorne = entity.isAirBorne;
        soulEntity.setSneaking(entity.isSneaking());
        soulEntity.setSprinting(entity.isSprinting());
        soulEntity.setInvisible(entity.isInvisible());

        return soulEntity;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
