package dev.onyxstudios.projecti.items;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.api.block.BoneCageType;
import dev.onyxstudios.projecti.blocks.BoneCageBlock;
import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoneCageItem extends BlockItem {

    public BoneCageItem() {
        super(ModBlocks.BONE_CAGE.get(), new Item.Properties().tab(ProjectI.TAB));
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        boolean success = super.placeBlock(context, state);
        BlockPos pos = context.getClickedPos();
        if (success) {
            BlockState aboveState = ModBlocks.BONE_CAGE.get().defaultBlockState().setValue(BoneCageBlock.CAGE_TYPE, BoneCageType.TOP).setValue(HorizontalBlock.FACING, context.getHorizontalDirection().getOpposite());
            context.getLevel().setBlockAndUpdate(pos.above(), aboveState);
        }else {
            context.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        return success;
    }

    @Override
    public boolean canPlace(BlockItemUseContext context, BlockState state) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();

        if (world.isEmptyBlock(pos.above())) {
            return super.canPlace(context, state);
        }

        return false;
    }
}
