package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.blockentity.AlembicBlockEntity;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class AlembicBlock extends BaseEntityBlock {

    public static VoxelShape SPIRAL_SHAPE = Block.box(3, 1, 3, 13, 13, 13);

    public static VoxelShape DECANTER_SHAPE = Block.box(2, 0, 2, 14, 15, 14);

    public static VoxelShape FUNNEL_SHAPE = Stream.of(
            Block.box(5, 6, 5, 11, 8, 11),
            Block.box(4, 5, 4, 12, 6, 12),
            Block.box(3, 0, 3, 13, 5, 13),
            Block.box(6.5, 8, 6.5, 9.5, 10, 9.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static VoxelShape GOURD_SHAPE = Stream.of(
            Block.box(4, 2, 4, 12, 7, 12),
            Block.box(4, 9, 4, 12, 14, 12),
            Block.box(6, 7, 6, 10, 9, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

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
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private final AlembicType type;

    public AlembicBlock(AlembicType type) {
        super(Properties.of(Material.GLASS)
                .strength(1.5f, 1.5f)
                .noOcclusion()
                .lightLevel(value -> 1)
                .sound(SoundType.GLASS)
        );

        this.type = type;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);
        if (level.isClientSide()) return;

        findCage(level, pos);
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof AlembicBlockEntity) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.relative(direction);
                if (connectAlembics(level, pos, offsetPos, direction)) {
                    level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                    level.sendBlockUpdated(offsetPos, level.getBlockState(offsetPos), level.getBlockState(offsetPos), Block.UPDATE_ALL);
                    break;
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        if (level.isClientSide()) return;

        if (neighborBlock != this) {
            findCage(level, pos);

            AlembicBlockEntity alembic = ModEntities.ALEMBIC.getBlockEntity(level, pos);
            if (alembic != null) {
                boolean flag = level.hasNeighborSignal(pos);

                if (alembic.isPowered() != flag) {
                    alembic.setPowered(flag);
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof AlembicBlockEntity alembic) {
            if (alembic.hasParent()) {
                alembic.getParent().removeChild();
            }

            if (alembic.hasChild()) {
                alembic.getChild().removeParent();
                alembic.getChild().validatePath();
            }
        }

        super.onRemove(state, level, pos, newState, moved);
    }

    public void findCage(Level level, BlockPos pos) {
        if (level.isClientSide() || getAlembicType() != AlembicType.FUNNEL) return;
        AlembicBlockEntity alembic = ModEntities.ALEMBIC.getBlockEntity(level, pos);

        if (alembic != null) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.relative(direction);
                if (level.getBlockState(offsetPos).is(ModBlocks.BONE_CAGE)) {
                    alembic.setCage(offsetPos);
                    break;
                }
            }
        }
    }

    public boolean connectAlembics(Level level, BlockPos pos, BlockPos neighborPos, Direction neighborDirection) {
        AlembicBlockEntity alembic = (AlembicBlockEntity) level.getBlockEntity(pos);
        BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
        if (alembic != null && neighborEntity instanceof AlembicBlockEntity neighborAlembic) {
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext selectionContext) {
        return switch (type) {
            case FUNNEL -> FUNNEL_SHAPE;
            case GOURD -> GOURD_SHAPE;
            case SPLITTER -> SPLITTER_SHAPE;
            case SPIRAL -> SPIRAL_SHAPE;
            case DECANTER -> DECANTER_SHAPE;
        };
    }

    public AlembicType getAlembicType() {
        return type;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModEntities.ALEMBIC, AlembicBlockEntity::tick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlembicBlockEntity(pos, state);
    }
}
