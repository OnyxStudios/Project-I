package dev.onyxstudios.projecti.registry.events;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.entity.EntityBlueCrystal;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModItems;
import dev.onyxstudios.projecti.registry.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    //For dropping soul bones when killing entities
    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Entity source = event.getSource().getDirectEntity();

        if (!(entity instanceof PlayerEntity) && source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source;

            if (player.getMainHandItem().sameItem(Items.BONE.getDefaultInstance())) {
                if (!player.inventory.add(new ItemStack(ModItems.SOUL_BONE.get()))) {
                    if (!player.level.isClientSide()) {
                        player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(ModItems.SOUL_BONE.get())));
                    }
                }

                player.getMainHandItem().shrink(1);
            }
        }
    }

    //For custom fluid fog colors
    @SubscribeEvent
    public static void onFogColor(EntityViewRenderEvent.FogColors event) {
        if (event.getInfo().getFluidInCamera().is(ModTags.MOLTEN_BLUE_CRYSTAL)) {
            event.setRed(0.294f);
            event.setGreen(0.384f);
            event.setBlue(0.823f);
        }
    }
}
