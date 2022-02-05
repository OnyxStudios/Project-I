package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KnowledgeGemItem extends BaseItem {

    public KnowledgeGemItem() {
        super(new Properties().tab(ProjectI.TAB).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            //TODO Re-Add once Entity Knowledge is a thing
            //level.addFreshEntity(new EntityKnowledge(level, player));
        }

        return super.use(level, player, hand);
    }
}