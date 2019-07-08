package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
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
    }
}
