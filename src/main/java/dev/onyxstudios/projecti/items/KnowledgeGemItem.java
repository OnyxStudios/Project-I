package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class KnowledgeGemItem extends BaseItem {

    public KnowledgeGemItem() {
        super(new Properties().tab(ProjectI.TAB).stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide()) {
            //TODO Re-Add once Entity Knowledge is a thing
            //world.addFreshEntity(new EntityKnowledge(world, player));
        }

        return super.use(world, player, hand);
    }
}