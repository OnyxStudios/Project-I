package dev.onyxstudios.projecti.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.projecti.tileentity.TileEntityBoneCage;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class BoneCageRenderer extends TileEntityRenderer<TileEntityBoneCage> {

    private static final Map<EntityType<?>, Entity> FAKES = new HashMap<>();
    private static final float MAX_WIDTH = 0.75f;
    private static final float MAX_HEIGHT = 1.75f;

    public BoneCageRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(TileEntityBoneCage tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (tile.getStoredEntity() != null) {
            Direction facing = tile.getBlockState().getValue(HorizontalBlock.FACING);

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

    private void rotateTo(Direction facing, MatrixStack stack) {
        switch (facing) {
            case SOUTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(0));
                break;
            case WEST:
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case NORTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
            case EAST:
                stack.mulPose(Vector3f.YP.rotationDegrees(-90));
                break;
        }
    }
}
