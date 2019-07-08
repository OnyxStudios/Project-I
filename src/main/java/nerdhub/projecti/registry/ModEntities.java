package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.client.render.entity.RenderEntitySoul;
import nerdhub.projecti.entity.EntitySoulBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static ResourceLocation SOUL_NAME = new ResourceLocation(ProjectI.MODID, "entity_soul");
    public static EntityType<EntitySoulBase> ENTITY_SOUL = EntityType.Builder.<EntitySoulBase>create(EntitySoulBase::new, EntityClassification.MISC).build(SOUL_NAME.toString());

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(ENTITY_SOUL.setRegistryName(SOUL_NAME));
    }

    public static void renderEntities() {
        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.entityRenderMap.put(EntitySoulBase.class, new RenderEntitySoul(rendererManager));
    }
}
