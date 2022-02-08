package dev.onyxstudios.projecti.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor<T extends LivingEntity, M extends EntityModel<T>> {

    @Accessor("layers")
    List<RenderLayer<T, M>> getLayers();

    @Invoker("setupRotations")
    void invokeSetupRotations(T p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_);

    @Invoker("scale")
    void invokeScale(T p_115314_, PoseStack p_115315_, float p_115316_);
}
