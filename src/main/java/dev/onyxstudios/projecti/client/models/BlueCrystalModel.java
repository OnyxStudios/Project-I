package dev.onyxstudios.projecti.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.onyxstudios.projecti.client.ModClient;
import dev.onyxstudios.projecti.mixins.client.ModelRendererAccessor;
import dev.onyxstudios.projecti.tileentity.TileEntityCrystal;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Vector4f;

public class BlueCrystalModel extends Model {

    public static final BlueCrystalModel INSTANCE = new BlueCrystalModel();
    public static final RenderMaterial CRYSTAL_RESOURCE_LOCATION = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, ModClient.CRYSTAL_NOISE);

    private static Vector4f CRYSTAL_COLOR = new Vector4f(69 / 255f, 136 / 255f, 245 / 255f, 0.6f);
    private static Vector4f RARE_COLOR = new Vector4f(255 / 255f, 201 / 255f, 94 / 255f, 0.6f);

    private final ModelRenderer modelBase;
    private final ModelRendererScale[] crystalPillars = new ModelRendererScale[4];

    private final ModelRenderer cosmetic1;
    private final ModelRenderer cosmetic2;
    private final ModelRenderer cosmetic3;
    private final ModelRenderer cosmetic4;
    private final ModelRenderer cosmetic5;
    private final ModelRenderer cosmetic6;
    private final ModelRenderer cosmetic7;
    private final ModelRenderer cosmetic8;
    private final ModelRenderer cosmetic9;
    private final ModelRenderer cosmetic10;
    private final ModelRenderer cosmetic11;
    private final ModelRenderer cosmetic12;

    public BlueCrystalModel() {
        super(RenderType::entityTranslucent);
        texWidth = 16;
        texHeight = 16;

        modelBase = new ModelRenderer(this);
        modelBase.setPos(0.0F, 24.0F, 0.0F);
        modelBase.texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        crystalPillars[0] = new ModelRendererScale(this);
        crystalPillars[0].setPos(-4.5F, -1.0F, 1.5F);
        setRotationAngle(crystalPillars[0], -0.1043F, -0.6509F, -0.2659F);
        crystalPillars[0].texOffs(1, 0).addBox(0.5F, -13.0F, -1.5F, 3.0F, 14.0F, 3.0F, 0.0F, false);

        crystalPillars[1] = new ModelRendererScale(this);
        crystalPillars[1].setPos(1.5F, 0.0F, 3.5F);
        setRotationAngle(crystalPillars[1], -0.2814F, -0.3531F, 0.2076F);
        crystalPillars[1].texOffs(2, 0).addBox(-1.5F, -14.0F, -2.5F, 3.0F, 14.0F, 3.0F, 0.0F, false);

        crystalPillars[2] = new ModelRendererScale(this);
        crystalPillars[2].setPos(3.5F, 0.0F, -1.5F);
        setRotationAngle(crystalPillars[2], 0.2564F, -0.0524F, 0.2557F);
        crystalPillars[2].texOffs(2, 0).addBox(-2.5F, -14.0F, -1.5F, 3.0F, 14.0F, 3.0F, 0.0F, false);

        crystalPillars[3] = new ModelRendererScale(this);
        crystalPillars[3].setPos(-1.5573F, 0.0F, -1.3112F);
        setRotationAngle(crystalPillars[3], 0.4499F, -0.2143F, -0.2768F);
        crystalPillars[3].texOffs(0, 0).addBox(-1.5F, -14.3099F, -1.5F, 3.0F, 14.0F, 3.0F, 0.0F, false);

        cosmetic1 = new ModelRenderer(this);
        cosmetic1.setPos(-1.5F, -2.5F, 1.5F);
        modelBase.addChild(cosmetic1);
        setRotationAngle(cosmetic1, 0.0267F, 1.0663F, 0.5651F);
        cosmetic1.texOffs(0, 0).addBox(0.0F, -1.5F, -0.75F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cosmetic2 = new ModelRenderer(this);
        cosmetic2.setPos(0.0F, -2.5F, 1.5F);
        modelBase.addChild(cosmetic2);
        setRotationAngle(cosmetic2, 0.0086F, 0.6194F, 0.3783F);
        cosmetic2.texOffs(3, 6).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cosmetic3 = new ModelRenderer(this);
        cosmetic3.setPos(0.5F, -2.5F, -1.5F);
        modelBase.addChild(cosmetic3);
        setRotationAngle(cosmetic3, -0.9739F, -0.6067F, -0.3696F);
        cosmetic3.texOffs(3, 8).addBox(-0.25F, -0.75F, -1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cosmetic4 = new ModelRenderer(this);
        cosmetic4.setPos(0.5F, -2.5F, 0.5F);
        modelBase.addChild(cosmetic4);
        setRotationAngle(cosmetic4, 0.1554F, -0.3334F, -0.2132F);
        cosmetic4.texOffs(8, 8).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cosmetic5 = new ModelRenderer(this);
        cosmetic5.setPos(1.25F, -2.5F, -0.25F);
        modelBase.addChild(cosmetic5);
        setRotationAngle(cosmetic5, 0.0F, -0.5236F, -2.2689F);
        cosmetic5.texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cosmetic6 = new ModelRenderer(this);
        cosmetic6.setPos(-0.5F, -2.5F, 0.5F);
        modelBase.addChild(cosmetic6);
        setRotationAngle(cosmetic6, -0.2472F, -0.6599F, 0.3905F);
        cosmetic6.texOffs(4, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cosmetic7 = new ModelRenderer(this);
        cosmetic7.setPos(-0.5F, -2.5F, -0.5F);
        modelBase.addChild(cosmetic7);
        setRotationAngle(cosmetic7, 0.48F, 0.0F, 0.7418F);
        cosmetic7.texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cosmetic8 = new ModelRenderer(this);
        cosmetic8.setPos(0.0F, 0.0F, 0.0F);
        modelBase.addChild(cosmetic8);
        setRotationAngle(cosmetic8, 0.0F, 0.3491F, -0.1309F);
        cosmetic8.texOffs(7, 11).addBox(2.0F, -1.0F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        cosmetic9 = new ModelRenderer(this);
        cosmetic9.setPos(-3.5F, -0.5F, -0.5F);
        modelBase.addChild(cosmetic9);
        setRotationAngle(cosmetic9, -0.2856F, -0.5973F, 0.4812F);
        cosmetic9.texOffs(0, 0).addBox(0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cosmetic10 = new ModelRenderer(this);
        cosmetic10.setPos(-2.5F, -1.0F, 0.5F);
        modelBase.addChild(cosmetic10);
        setRotationAngle(cosmetic10, 0.6191F, -0.2701F, -0.2736F);
        cosmetic10.texOffs(0, 0).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cosmetic11 = new ModelRenderer(this);
        cosmetic11.setPos(0.5F, -0.5F, -2.5F);
        modelBase.addChild(cosmetic11);
        setRotationAngle(cosmetic11, 0.7261F, -0.1975F, 0.582F);
        cosmetic11.texOffs(3, 9).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cosmetic12 = new ModelRenderer(this);
        cosmetic12.setPos(-0.5F, -0.5F, 3.5F);
        modelBase.addChild(cosmetic12);
        setRotationAngle(cosmetic12, -0.4013F, -0.2013F, 0.0846F);
        cosmetic12.texOffs(7, 11).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int light, int lightOverlay, float passR, float passG, float passB, float passA) {
        modelBase.render(matrixStack, vertexBuilder, light, lightOverlay, CRYSTAL_COLOR.x(), CRYSTAL_COLOR.y(), CRYSTAL_COLOR.z(), CRYSTAL_COLOR.w());
    }

    public void renderCrystals(TileEntityCrystal tile, MatrixStack matrixStack, IVertexBuilder builder, int light, int lightOverlay) {
        for (int i = 0; i < 4; i++) {
            if (tile.stage < i + 1 && tile.stage != i) continue;

            Vector4f color = tile.rare && tile.rarePillar == i ? RARE_COLOR : CRYSTAL_COLOR;
            ModelRendererScale crystalRender = crystalPillars[i];

            matrixStack.pushPose();
            float scale = 1;
            if (tile.stage < i + 1)
                scale = (tile.age / (float) TileEntityCrystal.MAX_TICKS_PER_STAGE);

            crystalRender.renderWithScale(matrixStack, builder, 1, scale, 1, light, lightOverlay, color.x(), color.y(), color.z(), color.w());
            matrixStack.popPose();
        }
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    private static class ModelRendererScale extends ModelRenderer {

        public ModelRendererScale(Model model) {
            super(model);
        }

        public void renderWithScale(MatrixStack matrixStack, IVertexBuilder vertexBuilder, float scaleX, float scaleY, float scaleZ, int light, int lightOverlay, float r, float g, float b, float a) {
            if (this.visible) {
                ObjectList<ModelRenderer.ModelBox> cubes = ((ModelRendererAccessor) this).getCubes();
                ObjectList<ModelRenderer> children = ((ModelRendererAccessor) this).getChildren();
                if (!cubes.isEmpty() || !children.isEmpty()) {
                    matrixStack.pushPose();
                    this.translateAndRotate(matrixStack);
                    matrixStack.scale(scaleX, scaleY, scaleZ);
                    ((ModelRendererAccessor) this).invokerCompile(matrixStack.last(), vertexBuilder, light, lightOverlay, r, g, b, a);

                    for (ModelRenderer modelrenderer : children) {
                        modelrenderer.render(matrixStack, vertexBuilder, light, lightOverlay, r, g, b, a);
                    }

                    matrixStack.popPose();
                }
            }
        }
    }
}
