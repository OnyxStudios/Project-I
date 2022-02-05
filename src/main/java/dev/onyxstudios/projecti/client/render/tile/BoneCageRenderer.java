package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.onyxstudios.projecti.blockentity.BoneCageBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

import java.util.HashMap;
import java.util.Map;

public class BoneCageRenderer implements BlockEntityRenderer<BoneCageBlockEntity> {

    private static final Map<EntityType<?>, Entity> FAKES = new HashMap<>();
    private static final float MAX_WIDTH = 0.75f;
    private static final float MAX_HEIGHT = 1.75f;

    @Override
    public void render(BoneCageBlockEntity tile, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (tile.getStoredEntity() != null) {
            Direction facing = tile.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            Minecraft mc = Minecraft.getInstance();
            Entity entity = FAKES.computeIfAbsent(tile.getStoredEntity(), entityType -> entityType.create(mc.level));

            if (entity != null) {
                stack.pushPose();
                stack.translate(0.5, 0.1, 0.5);
                rotateTo(facing, stack);

                if (entity.getBbWidth() > MAX_WIDTH || entity.getBbHeight() > MAX_HEIGHT) {
                    float scale = Math.min(MAX_WIDTH / entity.getBbWidth(), MAX_HEIGHT / entity.getBbHeight());
                    stack.scale(scale, scale, scale);
                }
                Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, 0, 0, stack, buffer, combinedLight);
                stack.popPose();
            }
        }
    }

    private void rotateTo(Direction facing, PoseStack stack) {
        switch (facing) {
            case SOUTH -> stack.mulPose(Vector3f.YP.rotationDegrees(0));
            case WEST -> stack.mulPose(Vector3f.YP.rotationDegrees(90));
            case NORTH -> stack.mulPose(Vector3f.YP.rotationDegrees(180));
            case EAST -> stack.mulPose(Vector3f.YP.rotationDegrees(-90));
        }
    }
}
