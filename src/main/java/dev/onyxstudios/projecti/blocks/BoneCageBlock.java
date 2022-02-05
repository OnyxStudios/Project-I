package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.BoneCageType;
import dev.onyxstudios.projecti.tileentity.BoneCageBlockEntity;
import dev.onyxstudios.projecti.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class BoneCageBlock extends Block implements EntityBlock {

    public static EnumProperty<BoneCageType> CAGE_TYPE = EnumProperty.create("cage_type", BoneCageType.class);
    public static BooleanProperty CAGE_OPEN = BooleanProperty.create("cage_open");

    private static final VoxelShape BOTTOM_OPEN_SHAPE = Stream.of(
            Block.box(1, 1, 15, 15, 16, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 1, 0, 1, 16, 16),
            Block.box(15, 1, 0, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape TOP_OPEN_SHAPE = Stream.of(
            Block.box(1, 0, 15, 15, 15, 16),
            Block.box(0, 15, 0, 16, 16, 16),
            Block.box(0, 0, 0, 1, 15, 16),
            Block.box(15, 0, 0, 16, 15, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape BOTTOM_CLOSED_SHAPE = Stream.of(
            Block.box(1, 1, 15, 15, 16, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 1, 0, 1, 16, 16),
            Block.box(15, 1, 0, 16, 16, 16),
            Block.box(1, 1, 0, 15, 16, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape TOP_CLOSED_SHAPE = Stream.of(
            Block.box(1, 0, 15, 15, 15, 16),
            Block.box(0, 15, 0, 16, 16, 16),
            Block.box(0, 0, 0, 1, 15, 16),
            Block.box(15, 0, 0, 16, 15, 16),
            Block.box(1, 0, 0, 15, 15, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BoneCageBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .setValue(CAGE_TYPE, BoneCageType.BOTTOM).setValue(CAGE_OPEN, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        toggleCage(level, pos, state);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        if (level.isClientSide()) return;

        BoneCageType cageType = state.getValue(CAGE_TYPE);
        BlockPos cagePos = cageType == BoneCageType.BOTTOM ? pos : pos.below();
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        boolean flag = level.getSignal(pos.relative(facing), facing) >= 13;

        if (neighborBlock != this && level.getBlockEntity(cagePos) instanceof BoneCageBlockEntity) {
            BoneCageBlockEntity boneCage = (BoneCageBlockEntity) level.getBlockEntity(cagePos);

            if (boneCage != null && boneCage.isPowered() != flag) {
                if (!boneCage.isPowered()) {
                    toggleCage(level, pos, state);
                }

                boneCage.setPowered(flag);
            }
        }
    }

    private void toggleCage(Level level, BlockPos pos, BlockState state) {
        boolean open = state.getValue(CAGE_OPEN);
        if (!level.isClientSide()) {
            BoneCageType cageType = state.getValue(CAGE_TYPE);
            tryHandleMob(level, cageType == BoneCageType.BOTTOM ? pos : pos.relative(Direction.DOWN));

            BlockPos otherPos = pos.relative(cageType.getCageDirection());
            BlockState other = level.getBlockState(otherPos);
            level.setBlockAndUpdate(pos, state.setValue(CAGE_OPEN, !open));
            level.setBlockAndUpdate(otherPos, other.setValue(CAGE_OPEN, !open));
        }

        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), open ? SoundEvents.WOODEN_DOOR_CLOSE : SoundEvents.WOODEN_DOOR_OPEN, SoundSource.BLOCKS, 1, 1);
    }

    private void tryHandleMob(Level level, BlockPos basePos) {
        BlockState state = level.getBlockState(basePos);
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        BoneCageType cageType = state.getValue(CAGE_TYPE);
        boolean open = state.getValue(CAGE_OPEN);
        BlockEntity tile = level.getBlockEntity(cageType == BoneCageType.BOTTOM ? basePos : basePos.below());
        if (!(tile instanceof BoneCageBlockEntity)) return;
        BoneCageBlockEntity boneCage = (BoneCageBlockEntity) tile;

        if (open) {
            int expandX = facing.getAxis() == Direction.Axis.X ? 0 : 2;
            int expandZ = facing.getAxis() == Direction.Axis.Z ? 0 : 2;
            BlockPos forward2 = basePos.relative(facing, 3);
            AABB boundingBox = new AABB(
                    new BlockPos(basePos.getX() - expandX, basePos.getY() - 2, basePos.getZ() - expandZ),
                    new BlockPos(forward2.getX() + expandX, forward2.getY() + 2, forward2.getZ() + expandZ)
            );

            LivingEntity entity = level.getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, null, basePos.getX(), basePos.getY(), basePos.getZ(), boundingBox);
            if (entity != null && !(entity instanceof Player) && boneCage.canTrap(entity)) {
                boneCage.trapEntity(entity);
            }
        } else {
            boneCage.releaseEntity();
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BoneCageType cageType = state.getValue(CAGE_TYPE);

        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            BlockEntity tile = level.getBlockEntity(cageType == BoneCageType.BOTTOM ? pos : pos.below());

            if (tile instanceof BoneCageBlockEntity) {
                BoneCageBlockEntity boneCage = (BoneCageBlockEntity) tile;
                boneCage.releaseEntity();
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            if (cageType == BoneCageType.TOP) {
                level.destroyBlock(pos.relative(Direction.DOWN), false);
            } else {
                level.destroyBlock(pos.relative(Direction.UP), false);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean open = state.getValue(CAGE_OPEN);

        BoneCageType cageType = state.getValue(CAGE_TYPE);
        VoxelShape result;
        if (open) result = cageType == BoneCageType.TOP ? TOP_OPEN_SHAPE : BOTTOM_OPEN_SHAPE;
        else result = cageType == BoneCageType.TOP ? TOP_CLOSED_SHAPE : BOTTOM_CLOSED_SHAPE;

        return BlockUtils.rotateShape(result, state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING, CAGE_TYPE, CAGE_OPEN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(CAGE_TYPE) == BoneCageType.BOTTOM) {
            return new BoneCageBlockEntity(pos, state);
        }

        return null;
    }
}
