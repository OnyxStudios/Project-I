package dev.onyxstudios.projecti.client;

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
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClient {

    public static ResourceLocation CRYSTAL_NOISE = new ResourceLocation(ProjectI.MODID, "blocks/crystal_noise");

    public static ResourceLocation SHORT_CONNECTOR_LOC = new ResourceLocation(ProjectI.MODID, "block/alembic_short_connector");
    public static ResourceLocation LONG_CONNECTOR_LOC = new ResourceLocation(ProjectI.MODID, "block/alembic_long_connector");
    public static BakedModel SHORT_CONNECTOR;
    public static BakedModel LONG_CONNECTOR;

    public static void init() {
        initEntityRenders();
        initLayers();
        initTESRS();
    }

    public static void particleEvent(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.GLOW.get(), GlowParticle.Factory::new);
    }

    private static void initTESRS() {
        BlockEntityRenderers.register(ModEntities.CRYSTAL_TYPE.get(), ctx -> new BlueCrystalRenderer());
        BlockEntityRenderers.register(ModEntities.STAMPER_TYPE.get(), ctx -> new CircuitStamperRenderer());
        BlockEntityRenderers.register(ModEntities.BELLOWS_TYPE.get(), BellowsRenderer::new);
        BlockEntityRenderers.register(ModEntities.ALEMBIC_TYPE.get(), ctx -> new AlembicRenderer());
        BlockEntityRenderers.register(ModEntities.BONE_CAGE_TYPE.get(), ctx -> new BoneCageRenderer());
        BlockEntityRenderers.register(ModEntities.SOUL_RELAY_TYPE.get(), SoulRelayRenderer::new);
        BlockEntityRenderers.register(ModEntities.BLOW_MOLD_TYPE.get(), ctx -> new BlowMoldRenderer());
    }

    private static void initLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLUE_CRYSTAL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STAMPER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BELLOWS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BONE_CAGE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_RELAY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLOW_MOLD.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FUNNEL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DECANTER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOURD.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIRAL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPLITTER.get(), RenderType.translucent());
    }

    private static void initEntityRenders() {
        EntityRenderers.register(ModEntities.BLUE_CRYSTAL_ENTITY.get(), ItemEntityRenderer::new);
        EntityRenderers.register(ModEntities.SOUL_ENTITY.get(), SoulEntityRenderer::new);
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
}
