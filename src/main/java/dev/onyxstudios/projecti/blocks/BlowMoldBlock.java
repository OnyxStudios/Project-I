package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.tileentity.BlowMoldTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class BlowMoldBlock extends ContainerBlock {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(7, 1, 7, 9, 3, 9),
            Block.box(1, 3, 1, 15, 4, 15),
            Block.box(0, 4, 2, 1, 14, 14),
            Block.box(15, 4, 2, 16, 14, 14),
            Block.box(2, 4, 0, 14, 14, 1),
            Block.box(2, 4, 15, 14, 14, 16),
            Block.box(1, 4, 1, 15, 16, 2),
            Block.box(1, 4, 14, 15, 16, 15),
            Block.box(1, 4, 2, 2, 16, 14),
            Block.box(14, 4, 2, 15, 16, 14)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public BlowMoldBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion());
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new BlowMoldTileEntity();
    }
}
