package nerdhub.projecti.client.render.entity;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.client.render.ShaderHelper;
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
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
        ShaderHelper.useShader(ShaderHelper.bloomShader, generateBloomCallback(new ResourceLocation("textures/entity/pig/pig.png"), new float[] {0.8f, 1, 0.8f, 1}, new float[] {1, 1, 1, 0.2f}));
        EntityRenderer render = Minecraft.getInstance().getRenderManager().getEntityRenderObject(soulEntity);
        render.bindEntityTexture(soulEntity);
        render.doRender(soulEntity, x, y, z, yaw, partialTicks);
        ShaderHelper.releaseShader();
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
        soulEntity.remove();
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

    public static Consumer<Integer> generateBloomCallback(ResourceLocation textureLoc, float[] color, float[] brightColor) {
        return (Integer shader) -> {
            int textureUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "texture");
            int colorUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "color");
            int brightColorUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "brightColor");

            GLX.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
            GlStateManager.enableTexture();
            Minecraft.getInstance().getTextureManager().bindTexture(textureLoc);
            ARBShaderObjects.glUniform1iARB(textureUniform, 0);

            ARBShaderObjects.glUniform4fARB(colorUniform, color[0], color[1], color[2], color[2]);
            ARBShaderObjects.glUniform4fARB(brightColorUniform, brightColor[0], brightColor[1], brightColor[2], brightColor[2]);
        };
    }
}
