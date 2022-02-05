package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.api.block.BoneCageType;
import dev.onyxstudios.projecti.blocks.BoneCageBlock;
import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BoneCageItem extends BlockItem {

    public BoneCageItem() {
        super(ModBlocks.BONE_CAGE.get(), new Item.Properties().tab(ProjectI.TAB));
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        boolean success = super.placeBlock(context, state);
        BlockPos pos = context.getClickedPos();
        if (success) {
            BlockState aboveState = ModBlocks.BONE_CAGE.get().defaultBlockState()
                    .setValue(BoneCageBlock.CAGE_TYPE, BoneCageType.TOP)
                    .setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
            context.getLevel().setBlockAndUpdate(pos.above(), aboveState);
        } else {
            context.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        return success;
    }

    @Override
    public boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (level.isEmptyBlock(pos.above())) {
            return super.canPlace(context, state);
        }

        return false;
    }
}
