package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.StamperStatus;
import dev.onyxstudios.projecti.blockentity.StamperBlockEntity;
import dev.onyxstudios.projecti.utils.BlockUtils;
import dev.onyxstudios.projecti.utils.InventoryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CircuitStamperBlock extends BaseEntityBlock {

    public static final EnumProperty<StamperStatus> STATUS = EnumProperty.create("status", StamperStatus.class);

    public CircuitStamperBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .noOcclusion());
        registerDefaultState(getStateDefinition().any().setValue(STATUS, StamperStatus.OPEN)
                .setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        BlockEntity tile = level.getBlockEntity(pos);
        ItemStack heldStack = player.getItemInHand(hand);

        if (!level.isClientSide && state.getValue(STATUS) == StamperStatus.OPEN && tile instanceof StamperBlockEntity && rayTraceResult.getDirection() == Direction.UP) {
            StamperBlockEntity stamper = (StamperBlockEntity) tile;
            if (!heldStack.isEmpty() || (heldStack.isEmpty() && player.isShiftKeyDown())) {
                int slot;
                Vec3 hitVec = rayTraceResult.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
                Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);

                double x = 0;
                double z = 0;
                switch (direction) {
                    case NORTH:
                        x = hitVec.x;
                        z = 1 - hitVec.z;
                        break;
                    case SOUTH:
                        x = 1 - hitVec.x;
                        z = hitVec.z;
                        break;
                    case EAST:
                        x = hitVec.z;
                        z = hitVec.x;
                        break;
                    case WEST:
                        x = 1 - hitVec.z;
                        z = 1 - hitVec.x;
                        break;
                }

                if (x > 0.0625f && x < 0.9375f && z > 0.0625f && z < 0.9375f) {
                    double offset = 0.03125f;
                    if (z > 0.5 + offset && x < 0.5 - offset) {
                        slot = 3;
                    } else if (z > 0.5 + offset && x > 0.5 + offset) {
                        slot = 2;
                    } else if (z < 0.5 - offset && x < 0.5 - offset) {
                        slot = 1;
                    } else if (z < 0.5 - offset && x > 0.6 + offset) {
                        slot = 0;
                    } else {
                        slot = -1;
                    }

                    if (slot >= 0) {
                        ItemStack stack = stamper.getInventory().getStackInSlot(slot);

                        if (stack.isEmpty() && !heldStack.isEmpty()) {
                            stamper.getInventory().setStackInSlot(slot, heldStack.split(1));
                        } else if (!stack.isEmpty() && !heldStack.isEmpty()) {
                            if (stack.sameItem(heldStack) && stack.getCount() < 64) {
                                int amount = Math.min(heldStack.getCount(), 64 - stack.getCount());
                                stamper.getInventory().getStackInSlot(slot).grow(amount);
                                heldStack.shrink(amount);
                            }
                        } else if (!stack.isEmpty() && heldStack.isEmpty()) {
                            if (!player.getInventory().add(stack)) {
                                level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack.copy()));
                            }
                        }
                    }
                }
            } else if (!stamper.getInventory().getStackInSlot(4).isEmpty() && !player.isShiftKeyDown()) {
                player.setItemInHand(hand, stamper.getInventory().getStackInSlot(4).copy());
                stamper.getInventory().setStackInSlot(4, ItemStack.EMPTY);
            }

            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), Block.UPDATE_ALL);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            //If the stamper has an inventory, drop stacks stored
            if (level.getBlockEntity(pos) instanceof StamperBlockEntity stamperEntity) {
                InventoryUtils.dropInventoryItems(level, pos, stamperEntity.getInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean moving) {
        super.neighborChanged(state, level, pos, block, neighborPos, moving);
        BlockEntity tile = level.getBlockEntity(pos);

        if (tile instanceof StamperBlockEntity) {
            ((StamperBlockEntity) tile).run();
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(HorizontalDirectionalBlock.FACING, direction.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING, STATUS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        VoxelShape bottomShape = Block.box(1, 0, 1, 15, 1, 15);
        VoxelShape baseShape = Shapes.or(bottomShape, Block.box(0, 1, 0, 16, 3, 16));
        VoxelShape topShape;
        if (state.getValue(STATUS) == StamperStatus.OPEN) {
            topShape = Block.box(0, 0, 13, 16, 16, 16);
            topShape = BlockUtils.rotateShape(topShape, state.getValue(HorizontalDirectionalBlock.FACING));
        } else {
            topShape = Block.box(0, 3, 0, 16, 5, 16);
        }

        return Shapes.or(baseShape, topShape);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StamperBlockEntity(pos, state);
    }
}
