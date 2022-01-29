package dev.onyxstudios.projecti.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.projecti.entity.SoulEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SoulEntityRenderer extends EntityRenderer<SoulEntity> {

    private static final Map<EntityType<? extends LivingEntity>, LivingEntity> FAKES = new HashMap<>();

    public SoulEntityRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(SoulEntity soulEntity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
        super.render(soulEntity, yaw, partialTicks, stack, buffer, light);

        Minecraft mc = Minecraft.getInstance();
        LivingEntity entity = getSoulToRender(soulEntity);
        //EntityRenderer<? super LivingEntity> renderer = mc.getEntityRenderDispatcher().getRenderer(entity);
        //ResourceLocation texture = renderer.getTextureLocation(entity);
        //RenderType renderType = RenderType.entityTranslucent(texture);

        stack.pushPose();
        //TODO: Once on 1.18 render soul with shader and translucency
        mc.getEntityRenderDispatcher().render(entity, 0, 0, 0, yaw, partialTicks, stack, buffer, light);
        stack.popPose();
    }

    private LivingEntity getSoulToRender(SoulEntity soulEntity) {
        LivingEntity entity = FAKES.computeIfAbsent(soulEntity.getTargetType(), entityType -> entityType.create(soulEntity.level));

        if (entity != null) {
            entity.setPos(soulEntity.getX(), soulEntity.getY(), soulEntity.getZ());
            entity.xOld = soulEntity.xOld;
            entity.yOld = soulEntity.yOld;
            entity.zOld = soulEntity.zOld;
            entity.xo = soulEntity.xo;
            entity.yo = soulEntity.yo;
            entity.zo = soulEntity.zo;
            entity.setDeltaMovement(soulEntity.getDeltaMovement());
            entity.setOnGround(soulEntity.isOnGround());
            entity.xRot = soulEntity.xRot;
            entity.yRot = soulEntity.yRot;
            entity.xRotO = soulEntity.xRotO;
            entity.yRotO = soulEntity.yRotO;
            entity.yHeadRot = soulEntity.yHeadRot;
            entity.yHeadRotO = soulEntity.yHeadRotO;
            entity.animationPosition = soulEntity.animationPosition;
            entity.animationSpeed = soulEntity.animationSpeed;
            entity.animationSpeedOld = soulEntity.animationSpeedOld;
            entity.swinging = soulEntity.swinging;
            entity.swingTime = soulEntity.swingTime;
            entity.swingingArm = soulEntity.swingingArm;
            entity.setShiftKeyDown(soulEntity.isShiftKeyDown());
            entity.setSprinting(soulEntity.isSprinting());
            entity.setInvisible(soulEntity.isInvisible());
            entity.tickCount = soulEntity.tickCount;
            entity.yBodyRot = soulEntity.yBodyRot;
            entity.yBodyRotO = soulEntity.yBodyRotO;
            entity.deathTime = soulEntity.deathTime;
            entity.hurtDir = soulEntity.hurtDir;
            entity.hurtDuration = soulEntity.hurtDuration;
            entity.hurtTime = soulEntity.hurtTime;
            entity.hurtMarked = soulEntity.hurtMarked;
        }

        return entity;
    }

    @Override
    public ResourceLocation getTextureLocation(SoulEntity entity) {
        return null;
    }
}
