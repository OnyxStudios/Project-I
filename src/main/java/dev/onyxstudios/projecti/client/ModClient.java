package dev.onyxstudios.projecti.client;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.client.render.tile.*;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClient {

    public static ResourceLocation CRYSTAL_NOISE = new ResourceLocation(ProjectI.MODID, "blocks/crystal_noise");

    public static ResourceLocation SHORT_CONNECTOR_LOC = new ResourceLocation(ProjectI.MODID, "block/alembic_short_connector");
    public static ResourceLocation LONG_CONNECTOR_LOC = new ResourceLocation(ProjectI.MODID, "block/alembic_long_connector");
    public static IBakedModel SHORT_CONNECTOR;
    public static IBakedModel LONG_CONNECTOR;

    public static void init() {
        initEntityRenders();
        initLayers();
        initTESRS();
    }

    private static void initTESRS() {
        ClientRegistry.bindTileEntityRenderer(ModEntities.CRYSTAL_TYPE.get(), BlueCrystalRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModEntities.STAMPER_TYPE.get(), CircuitStamperRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModEntities.BELLOWS_TYPE.get(), BellowsRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModEntities.ALEMBIC_TYPE.get(), AlembicRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModEntities.BONE_CAGE_TYPE.get(), BoneCageRenderer::new);
    }

    private static void initLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.BLUE_CRYSTAL.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.STAMPER.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BELLOWS.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BONE_CAGE.get(), RenderType.cutout());

        RenderTypeLookup.setRenderLayer(ModBlocks.FUNNEL.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.DECANTER.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GOURD.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.SPIRAL.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.SPLITTER.get(), RenderType.translucent());
    }

    private static void initEntityRenders() {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager rendererManager = mc.getEntityRenderDispatcher();
        rendererManager.register(ModEntities.ENTITY_BLUE_CRYSTAL.get(), new ItemRenderer(rendererManager, mc.getItemRenderer()));
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        event.addSprite(CRYSTAL_NOISE);
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(SHORT_CONNECTOR_LOC);
        ModelLoader.addSpecialModel(LONG_CONNECTOR_LOC);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        SHORT_CONNECTOR = event.getModelManager().getModel(SHORT_CONNECTOR_LOC);
        LONG_CONNECTOR = event.getModelManager().getModel(LONG_CONNECTOR_LOC);
    }
}
