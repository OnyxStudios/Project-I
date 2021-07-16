package dev.onyxstudios.projecti.mixins.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelRenderer.class)
public interface ModelRendererAccessor {

    @Accessor("cubes")
    ObjectList<ModelRenderer.ModelBox> getCubes();

    @Accessor("children")
    ObjectList<ModelRenderer> getChildren();

    @Invoker("compile")
    void invokerCompile(MatrixStack.Entry p_228306_1_, IVertexBuilder p_228306_2_, int p_228306_3_, int p_228306_4_, float p_228306_5_, float p_228306_6_, float p_228306_7_, float p_228306_8_);
}
