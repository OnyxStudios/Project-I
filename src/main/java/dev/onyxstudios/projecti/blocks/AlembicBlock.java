package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.tileentity.AlembicTileEntity;
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

    private final AlembicType type;

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
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, world, pos, oldState, moving);
        if (world.isClientSide()) return;

        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof AlembicTileEntity) {

            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.relative(direction);
                if (connectAlembics(world, pos, offsetPos, direction)) {
                    world.sendBlockUpdated(pos, state, world.getBlockState(pos), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    world.sendBlockUpdated(offsetPos, world.getBlockState(offsetPos), world.getBlockState(offsetPos), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    break;
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos, isMoving);
        if (world.isClientSide()) return;

        if (neighborBlock != this) {
            TileEntity tile = world.getBlockEntity(pos);

            if (tile instanceof AlembicTileEntity) {
                AlembicTileEntity alembic = (AlembicTileEntity) tile;
                boolean flag = world.hasNeighborSignal(pos);

                if (alembic.isPowered() != flag) {
                    alembic.setPowered(flag);
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof AlembicTileEntity) {
            AlembicTileEntity alembic = (AlembicTileEntity) world.getBlockEntity(pos);

            if (alembic.hasParent()) {
                alembic.getParent().removeChild();
            }

            if (alembic.hasChild()) {
                alembic.getChild().removeParent();
                alembic.getChild().validatePath();
            }
        }

        super.onRemove(state, world, pos, newState, moved);
    }

    public boolean connectAlembics(World world, BlockPos pos, BlockPos neighborPos, Direction neighborDirection) {
        AlembicTileEntity alembic = (AlembicTileEntity) world.getBlockEntity(pos);
        TileEntity neighborEntity = world.getBlockEntity(neighborPos);
        if (alembic != null && neighborEntity instanceof AlembicTileEntity) {
            AlembicTileEntity neighborAlembic = (AlembicTileEntity) neighborEntity;
            boolean isFunnel = getAlembicType() == AlembicType.FUNNEL;
            boolean neighborFunnel = neighborAlembic.getAlembicType() == AlembicType.FUNNEL;

            if (isFunnel && !neighborFunnel) {
                if (!neighborAlembic.hasParent()) {
                    neighborAlembic.setParent(neighborDirection.getOpposite());
                    alembic.setChild(neighborDirection);
                    return true;
                }
            } else if (!isFunnel && (neighborFunnel || neighborAlembic.hasParent())) {
                if (!neighborAlembic.hasChild()) {
                    neighborAlembic.setChild(neighborDirection.getOpposite());
                    alembic.setParent(neighborDirection);
                    return true;
                }
            }
        }

        return false;
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
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new AlembicTileEntity();
    }
}
