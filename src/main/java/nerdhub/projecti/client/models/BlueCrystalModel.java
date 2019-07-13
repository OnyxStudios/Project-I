package nerdhub.projecti.client.models;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.projecti.tiles.TileEntityCrystal;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelBox;

public class BlueCrystalModel extends Model {

    public static final BlueCrystalModel INSTANCE = new BlueCrystalModel();

    private final RendererModel base;
    private final RendererModel bone;
    private final RendererModel bone2;
    private final RendererModel bone3;
    private final RendererModel crystal1;
    private final RendererModel crystal2;
    private final RendererModel crystal3;
    private final RendererModel crystal4;

    public BlueCrystalModel() {
        textureWidth = 16;
        textureHeight = 16;

        base = new RendererModel(this);
        base.setRotationPoint(0.0F, 24.0F, 0.0F);
        base.cubeList.add(new ModelBox(base, 0, 0, -1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F, false));

        bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(bone, -0.2618F, -0.2618F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F, -1.0F, 1.0F, 1, 1, 1, 0.0F, false));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(bone2, -0.4363F, -0.6109F, -0.0873F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, 1.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, -2.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));

        bone3 = new RendererModel(this);
        bone3.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(bone3, -0.3491F, 0.0F, 0.1745F);
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 1.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -2.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 0.0F, -1.0F, -2.0F, 1, 1, 1, 0.0F, false));

        crystal1 = new RendererModel(this);
        crystal1.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(crystal1, -0.3491F, -0.5236F, 0.0F);
        crystal1.cubeList.add(new ModelBox(crystal1, 0, 0, -2.0F, -7.0F, 0.0F, 2, 7, 2, 0.0F, false));

        crystal2 = new RendererModel(this);
        crystal2.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(crystal2, 0.3491F, -0.4363F, 0.0F);
        crystal2.cubeList.add(new ModelBox(crystal2, 0, 0, -1.0F, -6.0F, -2.0F, 2, 6, 2, 0.0F, false));

        crystal3 = new RendererModel(this);
        crystal3.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(crystal3, -0.4363F, -0.4363F, 0.6109F);
        crystal3.cubeList.add(new ModelBox(crystal3, 0, 0, -1.0F, -7.0F, -1.0F, 2, 7, 2, 0.0F, false));

        crystal4 = new RendererModel(this);
        crystal4.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(crystal4, 0.1745F, -0.5236F, -0.5236F);
        crystal4.cubeList.add(new ModelBox(crystal4, 0, 0, -2.0F, -7.0F, -2.0F, 2, 7, 2, 0.0F, false));
    }

    public void render(TileEntityCrystal tile) {
        GlStateManager.color4f(69 / 255f, 136 / 255f, 245 / 255f, 0.6f);
        base.render(0.0625F);
        bone.render(0.0625F);
        bone2.render(0.0625F);
        bone3.render(0.0625F);
        if(tile.stage >= 1) {
            crystal1.render(0.0625F);
        }

        if(tile.stage >= 2) {
            crystal2.render(0.0625F);
        }

        if(tile.stage >= 3) {
            crystal3.render(0.0625F);
        }

        if(tile.stage >= 4) {
            if(tile.rare) {
                GlStateManager.clearColor(1, 1, 1, 1);
                GlStateManager.color4f(255 / 255f, 201 / 255f, 94 / 255f, 0.6f);

            }
            crystal4.render(0.0625F);
        }
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
