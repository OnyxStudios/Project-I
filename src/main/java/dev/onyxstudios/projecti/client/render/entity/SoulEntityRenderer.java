package dev.onyxstudios.projecti.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.onyxstudios.projecti.client.ModRenderTypes;
import dev.onyxstudios.projecti.entity.SoulEntity;
import dev.onyxstudios.projecti.mixins.client.LivingEntityRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoulEntityRenderer extends EntityRenderer<SoulEntity> {

    private static final Map<EntityType<? extends LivingEntity>, LivingEntity> FAKES = new HashMap<>();

    public SoulEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SoulEntity soulEntity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        super.render(soulEntity, yaw, partialTicks, stack, buffer, light);

        LivingEntity entity = getSoulToRender(soulEntity);
        LivingEntityRenderer<LivingEntity, ? extends EntityModel<?>> renderer = getRenderer(entity);
        if (renderer == null) return;

        renderEntity(soulEntity, entity, renderer, stack, buffer, partialTicks, yaw, light);
    }

    private void renderEntity(SoulEntity soulEntity, LivingEntity fakeEntity, LivingEntityRenderer<LivingEntity, ? extends EntityModel<?>> renderer, PoseStack stack, MultiBufferSource buffer, float partialTicks, float yaw, int light) {
        stack.pushPose();
        renderer.getModel().attackTime = fakeEntity.getAttackAnim(partialTicks);

        boolean shouldSit = soulEntity.isPassenger() && (soulEntity.getVehicle() != null && soulEntity.getVehicle().shouldRiderSit());
        renderer.getModel().riding = shouldSit;
        renderer.getModel().young = soulEntity.isBaby();
        float f = Mth.rotLerp(partialTicks, soulEntity.yBodyRotO, soulEntity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, soulEntity.yHeadRotO, soulEntity.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && soulEntity.getVehicle() instanceof LivingEntity vehicleEntity) {
            f = Mth.rotLerp(partialTicks, vehicleEntity.yBodyRotO, vehicleEntity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(partialTicks, soulEntity.xRotO, soulEntity.getXRot());
        if (LivingEntityRenderer.isEntityUpsideDown(soulEntity)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        if (soulEntity.getPose() == Pose.SLEEPING) {
            Direction direction = soulEntity.getBedOrientation();
            if (direction != null) {
                float f4 = soulEntity.getEyeHeight(Pose.STANDING) - 0.1F;
                stack.translate((-direction.getStepX()) * f4, 0.0D, (-direction.getStepZ()) * f4);
            }
        }

        float f7 = soulEntity.tickCount + partialTicks;
        ((LivingEntityRendererAccessor) renderer).invokeSetupRotations(fakeEntity, stack, f7, f, partialTicks);

        stack.scale(-1.0F, -1.0F, 1.0F);
        ((LivingEntityRendererAccessor) renderer).invokeScale(fakeEntity, stack, partialTicks);
        stack.translate(0.0D, (double) -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && soulEntity.isAlive()) {
            f8 = Mth.lerp(partialTicks, soulEntity.animationSpeedOld, soulEntity.animationSpeed);
            f5 = soulEntity.animationPosition - soulEntity.animationSpeed * (1.0F - partialTicks);
            if (soulEntity.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        renderer.getModel().prepareMobModel(fakeEntity, f5, f8, partialTicks);
        renderer.getModel().setupAnim(fakeEntity, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = !soulEntity.isInvisible();
        boolean flag1 = minecraft.player != null && !flag && !soulEntity.isInvisibleTo(minecraft.player);

        ResourceLocation texture = renderer.getTextureLocation(fakeEntity);
        RenderType renderType = ModRenderTypes.SOUL_BLOOM.apply(texture);

        if (renderType != null) {
            VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
            int i = LivingEntityRenderer.getOverlayCoords(fakeEntity, 0);
            renderer.getModel().renderToBuffer(stack, vertexconsumer, light, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }

        if (!soulEntity.isSpectator()) {
            List<RenderLayer> layers = ((LivingEntityRendererAccessor) renderer).getLayers();
            for (RenderLayer renderlayer : layers) {
                renderlayer.render(stack, buffer, light, fakeEntity, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        stack.popPose();
    }

    private LivingEntityRenderer<LivingEntity, ? extends EntityModel<?>> getRenderer(LivingEntity entity) {
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        return renderer instanceof LivingEntityRenderer ? (LivingEntityRenderer<LivingEntity, ? extends EntityModel<?>>) renderer : null;
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
            entity.setXRot(soulEntity.getXRot());
            entity.setYRot(soulEntity.getYRot());
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
