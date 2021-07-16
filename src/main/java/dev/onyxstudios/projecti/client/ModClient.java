package dev.onyxstudios.projecti.client;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.client.render.tile.BlueCrystalRenderer;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClient {

    public static ResourceLocation CRYSTAL_NOISE = new ResourceLocation(ProjectI.MODID, "blocks/crystal_noise");

    public static void init() {
        initEntityRenders();
        initLayers();
        initTESRS();
    }

    private static void initTESRS() {
        ClientRegistry.bindTileEntityRenderer(ModEntities.CRYSTAL_TYPE.get(), BlueCrystalRenderer::new);
    }

    private static void initLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.BLUE_CRYSTAL.get(), RenderType.translucent());
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
}
