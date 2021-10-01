package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.tileentity.TileEntityAlembic;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class AlembicBlock extends ContainerBlock {

    public static VoxelShape SPIRAL_SHAPE = Block.box(3, 1, 3, 13, 13, 13);

    public static VoxelShape DECANTER_SHAPE = Block.box(2, 0, 2, 14, 15, 14);

    public static VoxelShape FUNNEL_SHAPE = Stream.of(
            Block.box(5, 6, 5, 11, 8, 11),
            Block.box(4, 5, 4, 12, 6, 12),
            Block.box(3, 0, 3, 13, 5, 13),
            Block.box(6.5, 8, 6.5, 9.5, 10, 9.5)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public static VoxelShape GOURD_SHAPE = Stream.of(
            Block.box(4, 2, 4, 12, 7, 12),
            Block.box(4, 9, 4, 12, 14, 12),
            Block.box(6, 7, 6, 10, 9, 10)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public static VoxelShape SPLITTER_SHAPE = Stream.of(
            Block.box(0, 0, 0, 5, 7, 6),
            Block.box(11, 0, 0, 16, 7, 6),
            Block.box(11, 0, 10, 16, 7, 16),
            Block.box(0, 0, 10, 5, 7, 16),
            Block.box(5, 5, 5, 11, 12, 11),
            Block.box(13, 3, 5.5, 14, 4, 10.5),
            Block.box(13, 4, 7.5, 14, 9, 8.5),
            Block.box(10.5, 8, 7.5, 13, 9, 8.5),
            Block.box(2, 3, 5.5, 3, 4, 10.5),
            Block.box(2, 4, 7.5, 3, 9, 8.5),
            Block.box(3, 8, 7.5, 5.5, 9, 8.5)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private AlembicType type;

    public AlembicBlock(AlembicType type) {
        super(Properties.of(Material.GLASS)
                .strength(1.5f, 1.5f)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion()
                .lightLevel(value -> 1)
                .sound(SoundType.GLASS)
        );
        this.type = type;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        if (world.isClientSide()) return;

        TileEntity tile = world.getBlockEntity(pos);
        boolean isFunnel = getAlembicType() == AlembicType.FUNNEL;
        if (tile instanceof TileEntityAlembic) {
            TileEntityAlembic alembic = (TileEntityAlembic) tile;
            TileEntity neighborTile = world.getBlockEntity(neighborPos);

            if ((isFunnel || alembic.hasParent()) && !alembic.hasChild() && neighborTile instanceof TileEntityAlembic) {
                BlockPos offset = pos.subtract(neighborPos);
                Direction direction = Direction.fromNormal(offset.getX(), offset.getY(), offset.getZ());
                if (direction == null || direction == Direction.DOWN || direction == Direction.UP) return;

                alembic.setChild(direction.getOpposite());
                ((TileEntityAlembic) neighborTile).setParent(direction);
                world.sendBlockUpdated(pos, state, world.getBlockState(pos), Constants.BlockFlags.DEFAULT);
                world.sendBlockUpdated(neighborPos, world.getBlockState(neighborPos), world.getBlockState(neighborPos), Constants.BlockFlags.DEFAULT);
            }
        }

        super.neighborChanged(state, world, pos, neighborBlock, neighborPos, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        switch (type) {
            case FUNNEL:
                return FUNNEL_SHAPE;
            case GOURD:
                return GOURD_SHAPE;
            case SPLITTER:
                return SPLITTER_SHAPE;
            case SPIRAL:
                return SPIRAL_SHAPE;
            case DECANTER:
                return DECANTER_SHAPE;
        }

        return super.getShape(state, world, pos, selectionContext);
    }

    public AlembicType getAlembicType() {
        return type;
    }

    @Override
    public float getShadeBrightness(BlockState state, IBlockReader world, BlockPos pos) {
        return 1;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new TileEntityAlembic();
    }
}
