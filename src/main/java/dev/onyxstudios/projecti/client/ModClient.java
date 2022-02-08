package dev.onyxstudios.projecti.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.client.render.entity.SoulEntityRenderer;
import dev.onyxstudios.projecti.client.render.tile.*;
import dev.onyxstudios.projecti.particle.GlowParticle;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClient {

    public static ResourceLocation CRYSTAL_NOISE = new ResourceLocation(ProjectI.MODID, "blocks/crystal_noise");

    public static ResourceLocation SHORT_CONNECTOR_LOC = new ResourceLocation(ProjectI.MODID, "block/alembic_short_connector");
    public static ResourceLocation LONG_CONNECTOR_LOC = new ResourceLocation(ProjectI.MODID, "block/alembic_long_connector");
    public static BakedModel SHORT_CONNECTOR;
    public static BakedModel LONG_CONNECTOR;

    public static ShaderInstance SOUL_BLOOM_SHADER;

    public static void init() {
        initEntityRenders();
        initLayers();
        initTESRS();
    }

    public static void particleEvent(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.GLOW.get(), GlowParticle.Factory::new);
    }

    private static void initTESRS() {
        BlockEntityRenderers.register(ModEntities.CRYSTAL, ctx -> new BlueCrystalRenderer());
        BlockEntityRenderers.register(ModEntities.STAMPER, ctx -> new CircuitStamperRenderer());
        BlockEntityRenderers.register(ModEntities.BELLOWS, BellowsRenderer::new);
        BlockEntityRenderers.register(ModEntities.ALEMBIC, ctx -> new AlembicRenderer());
        BlockEntityRenderers.register(ModEntities.BONE_CAGE, ctx -> new BoneCageRenderer());
        BlockEntityRenderers.register(ModEntities.SOUL_RELAY, SoulRelayRenderer::new);
        BlockEntityRenderers.register(ModEntities.BLOW_MOLD, ctx -> new BlowMoldRenderer());
    }

    private static void initLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLUE_CRYSTAL, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CIRCUIT_STAMPER, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BELLOWS, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BONE_CAGE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_RELAY, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLOW_MOLD, RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FUNNEL, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DECANTER, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOURD, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIRAL, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPLITTER, RenderType.translucent());
    }

    private static void initEntityRenders() {
        EntityRenderers.register(ModEntities.BLUE_CRYSTAL_ENTITY, ItemEntityRenderer::new);
        EntityRenderers.register(ModEntities.SOUL_ENTITY, SoulEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        event.addSprite(CRYSTAL_NOISE);
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ForgeModelBakery.addSpecialModel(SHORT_CONNECTOR_LOC);
        ForgeModelBakery.addSpecialModel(LONG_CONNECTOR_LOC);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        SHORT_CONNECTOR = event.getModelManager().getModel(SHORT_CONNECTOR_LOC);
        LONG_CONNECTOR = event.getModelManager().getModel(LONG_CONNECTOR_LOC);
    }

    @SubscribeEvent
    public static void registerShaderEvent(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation(ProjectI.MODID, "bloom"), DefaultVertexFormat.NEW_ENTITY),
                shaderInstance -> SOUL_BLOOM_SHADER = shaderInstance);
    }
}
