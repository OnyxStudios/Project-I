package dev.onyxstudios.projecti.registry.events;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    //TODO Re-Add once soul bones are a thing
    /*@SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();

        if(!(entity instanceof PlayerEntity) && source != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

            if(player.getHeldItemMainhand().isItemEqual(new ItemStack(Items.BONE))) {
                if(!player.inventory.addItemStackToInventory(new ItemStack(ModItems.SOUL_BONE))) {
                    if(!player.world.isRemote) {
                        player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(ModItems.SOUL_BONE)));
                    }
                }

                player.getHeldItemMainhand().shrink(1);
            }
        }
    }*/
}
