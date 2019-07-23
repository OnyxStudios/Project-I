package nerdhub.projecti.items;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.entity.EntityKnowledge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemKnowledgeGem extends ItemBase {

    public ItemKnowledgeGem() {
        super("knowledge_gem", new Properties().group(ProjectI.modItemGroup).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            world.addEntity(new EntityKnowledge(world, player));
        }

        return super.onItemRightClick(world, player, hand);
    }
}
