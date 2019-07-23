package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.client.render.entity.RenderEntityKnowledge;
import nerdhub.projecti.client.render.entity.RenderEntitySoul;
import nerdhub.projecti.client.render.tile.BlowMoldRenderer;
import nerdhub.projecti.client.render.tile.BlueCrystalRenderer;
import nerdhub.projecti.client.render.tile.CircuitStamperRenderer;
import nerdhub.projecti.entity.EntityKnowledge;
import nerdhub.projecti.entity.EntitySoulBase;
import nerdhub.projecti.tiles.TileEntityBlowMold;
import nerdhub.projecti.tiles.TileEntityCrystal;
import nerdhub.projecti.tiles.TileEntityStamper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static TileEntityType<TileEntityCrystal> CRYSTAL_TYPE = createTileType(TileEntityCrystal::new, "crystal_tile_entity", ModBlocks.BLUE_CRYSTAL);
    public static TileEntityType<TileEntityStamper> STAMPER_TYPE = createTileType(TileEntityStamper::new, "stamper_tile", ModBlocks.CIRCUIT_STAMPER);
    public static TileEntityType<TileEntityBlowMold> BLOW_MOLD_TYPE = createTileType(TileEntityBlowMold::new, "blow_mold_tile", ModBlocks.BLOW_MOLD);

    public static EntityType<EntitySoulBase> ENTITY_SOUL = EntityType.Builder.<EntitySoulBase>create(EntitySoulBase::new, EntityClassification.MISC).build("entity_soul");
    public static EntityType<EntityKnowledge> ENTITY_KNOWLEDGE = EntityType.Builder.<EntityKnowledge>create(EntityKnowledge::new, EntityClassification.MISC).size(2, 2).build("entity_knowledge");

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(CRYSTAL_TYPE, STAMPER_TYPE, BLOW_MOLD_TYPE);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(ENTITY_SOUL.setRegistryName("entity_soul"), ENTITY_KNOWLEDGE.setRegistryName("entity_knowledge"));
    }

    public static void renderEntities() {
        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.entityRenderMap.put(EntitySoulBase.class, new RenderEntitySoul(rendererManager));
        rendererManager.entityRenderMap.put(EntityKnowledge.class, new RenderEntityKnowledge(rendererManager));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystal.class, new BlueCrystalRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStamper.class, new CircuitStamperRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlowMold.class, new BlowMoldRenderer());
    }

    public static TileEntityType createTileType(Supplier factory, String name, Block... blocks) {
        return (TileEntityType) TileEntityType.Builder.create(factory, blocks).build(null).setRegistryName(name);
    }
}
