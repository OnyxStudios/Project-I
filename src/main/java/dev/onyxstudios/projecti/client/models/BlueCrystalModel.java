package dev.onyxstudios.projecti.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector4f;
import dev.onyxstudios.projecti.blockentity.CrystalBlockEntity;
import dev.onyxstudios.projecti.client.ModClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;

public class BlueCrystalModel extends Model {

    public static final BlueCrystalModel INSTANCE = new BlueCrystalModel();
    public static final Material CRYSTAL_RESOURCE_LOCATION = new Material(InventoryMenu.BLOCK_ATLAS, ModClient.CRYSTAL_NOISE);

    private static final Vector4f CRYSTAL_COLOR = new Vector4f(0, 134 / 255f, 237 / 255f, 0.65f);
    private static final Vector4f DECO_COLOR = new Vector4f(35 / 255f, 138 / 255f, 217 / 255f, 0.45f);
    private static final Vector4f RARE_COLOR = new Vector4f(235 / 255f, 152 / 255f, 0, 0.35f);

    private final ModelPart base;
    private final ModelPart[] pillars;

    //Minecraft is stupid, STOP CHANGING RENDERING STUFF PLEASE
    //BANE OF MY EXISTENCE
    public BlueCrystalModel() {
        super(RenderType::entityTranslucent);
        this.base = createBasePart();
        this.pillars = createPillarsPart();
    }

    public ModelPart createBasePart() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 2).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(10, 14).addBox(-2.0F, -11.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 14).addBox(-1.0F, -11.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(7, 12).addBox(-1.0F, -11.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 7).addBox(1.0F, -11.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16).bakeRoot();
    }

    public ModelPart[] createPillarsPart() {
        ModelPart[] pillars = new ModelPart[4];

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(4, 4).addBox(1.0F, -9.0F, -4.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0));
        pillars[0] = LayerDefinition.create(meshdefinition, 16, 16).bakeRoot();

        partdefinition.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(4, 0).addBox(-4.0F, -8.0F, 1.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0));
        pillars[1] = LayerDefinition.create(meshdefinition, 16, 16).bakeRoot();

        partdefinition.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -7.0F, 1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0));
        pillars[2] = LayerDefinition.create(meshdefinition, 16, 16).bakeRoot();

        partdefinition.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(0, 5).addBox(-4.0F, -7.0F, -4.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0));
        pillars[3] = LayerDefinition.create(meshdefinition, 16, 16).bakeRoot();

        return pillars;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int lightOverlay, float passR, float passG, float passB, float passA) {
        base.render(poseStack, vertexConsumer, light, lightOverlay, CRYSTAL_COLOR.x(), CRYSTAL_COLOR.y(), CRYSTAL_COLOR.z(), CRYSTAL_COLOR.w());
    }

    public void renderPillars(CrystalBlockEntity tile, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int lightOverlay) {
        for (int i = 0; i < 4; i++) {
            float scale = 1;

            Vector4f color = tile.rare && tile.rarePillar == i ? RARE_COLOR : DECO_COLOR;
            ModelPart crystalPart = pillars[i];

            poseStack.pushPose();
            if (tile.stage < i + 1 && tile.stage != i)
                scale = 0.1f;
            else if (tile.stage < i + 1)
                scale = (tile.age / (float) CrystalBlockEntity.MAX_TICKS_PER_STAGE);

            poseStack.scale(1, scale, 1);
            crystalPart.render(poseStack, vertexConsumer, light, lightOverlay, color.x(), color.y(), color.z(), color.w());
            poseStack.popPose();
        }
    }
}
