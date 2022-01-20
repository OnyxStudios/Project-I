package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.BoneCageType;
import dev.onyxstudios.projecti.tileentity.TileEntityBoneCage;
import dev.onyxstudios.projecti.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class BoneCageBlock extends Block {

    public static EnumProperty<BoneCageType> CAGE_TYPE = EnumProperty.create("cage_type", BoneCageType.class);
    public static BooleanProperty CAGE_OPEN = BooleanProperty.create("cage_open");

    private static final VoxelShape BOTTOM_OPEN_SHAPE = Stream.of(
            Block.box(1, 1, 15, 15, 16, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 1, 0, 1, 16, 16),
            Block.box(15, 1, 0, 16, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape TOP_OPEN_SHAPE = Stream.of(
            Block.box(1, 0, 15, 15, 15, 16),
            Block.box(0, 15, 0, 16, 16, 16),
            Block.box(0, 0, 0, 1, 15, 16),
            Block.box(15, 0, 0, 16, 15, 16)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape BOTTOM_CLOSED_SHAPE = Stream.of(
            Block.box(1, 1, 15, 15, 16, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 1, 0, 1, 16, 16),
            Block.box(15, 1, 0, 16, 16, 16),
            Block.box(1, 1, 0, 15, 16, 1)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape TOP_CLOSED_SHAPE = Stream.of(
            Block.box(1, 0, 15, 15, 15, 16),
            Block.box(0, 15, 0, 16, 16, 16),
            Block.box(0, 0, 0, 1, 15, 16),
            Block.box(15, 0, 0, 16, 15, 16),
            Block.box(1, 0, 0, 15, 15, 1)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public BoneCageBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(HorizontalBlock.FACING, Direction.NORTH).setValue(CAGE_TYPE, BoneCageType.BOTTOM).setValue(CAGE_OPEN, false));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        toggleCage(world, pos, state);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos, isMoving);
        if (world.isClientSide()) return;

        BoneCageType cageType = state.getValue(CAGE_TYPE);
        BlockPos cagePos = cageType == BoneCageType.BOTTOM ? pos : pos.below();
        boolean flag = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.relative(cageType.getCageDirection()));
        if (neighborBlock != this && world.getBlockEntity(cagePos) instanceof TileEntityBoneCage) {
            TileEntityBoneCage boneCage = (TileEntityBoneCage) world.getBlockEntity(cagePos);

            if (boneCage.isPowered() != flag) {
                if(!boneCage.isPowered()) {
                    toggleCage(world, pos, state);
                }

                boneCage.setPowered(flag);
            }
        }
    }

    private void toggleCage(World world, BlockPos pos, BlockState state) {
        boolean open = state.getValue(CAGE_OPEN);
        if (!world.isClientSide()) {
            BoneCageType cageType = state.getValue(CAGE_TYPE);
            tryHandleMob(world, cageType == BoneCageType.BOTTOM ? pos : pos.relative(Direction.DOWN));

            BlockPos otherPos = pos.relative(cageType.getCageDirection());
            BlockState other = world.getBlockState(otherPos);
            world.setBlockAndUpdate(pos, state.setValue(CAGE_OPEN, !open));
            world.setBlockAndUpdate(otherPos, other.setValue(CAGE_OPEN, !open));
        }

        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), open ? SoundEvents.WOODEN_DOOR_CLOSE : SoundEvents.WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1, 1);
    }

    private void tryHandleMob(World world, BlockPos basePos) {
        //TODO: Handle store and release of mobs
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);

        if (!world.isClientSide() && state.getBlock() != newState.getBlock()) {
            BoneCageType cageType = state.getValue(CAGE_TYPE);

            if (cageType == BoneCageType.TOP) {
                world.destroyBlock(pos.relative(Direction.DOWN), false);
            } else {
                world.destroyBlock(pos.relative(Direction.UP), false);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        boolean open = state.getValue(CAGE_OPEN);

        BoneCageType cageType = state.getValue(CAGE_TYPE);
        VoxelShape result;
        if (open) result = cageType == BoneCageType.TOP ? TOP_OPEN_SHAPE : BOTTOM_OPEN_SHAPE;
        else result = cageType == BoneCageType.TOP ? TOP_CLOSED_SHAPE : BOTTOM_CLOSED_SHAPE;

        return BlockUtils.rotateShape(result, state.getValue(HorizontalBlock.FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(HorizontalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(HorizontalBlock.FACING, rotation.rotate(state.getValue(HorizontalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HorizontalBlock.FACING)));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.FACING, CAGE_TYPE, CAGE_OPEN);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getValue(CAGE_TYPE) == BoneCageType.BOTTOM) {
            return new TileEntityBoneCage();
        }

        return null;
    }
}
