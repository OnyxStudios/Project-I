package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.api.block.StamperStatus;
import dev.onyxstudios.projecti.tileentity.TileEntityStamper;
import dev.onyxstudios.projecti.utils.BlockUtils;
import dev.onyxstudios.projecti.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class CircuitStamperBlock extends ContainerBlock {

    public static final EnumProperty<StamperStatus> STATUS = EnumProperty.create("status", StamperStatus.class);

    public CircuitStamperBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion());
        registerDefaultState(getStateDefinition().any().setValue(STATUS, StamperStatus.OPEN).setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tile = world.getBlockEntity(pos);
        ItemStack heldStack = player.getItemInHand(hand);

        if (!world.isClientSide && state.getValue(STATUS) == StamperStatus.OPEN && tile instanceof TileEntityStamper && rayTraceResult.getDirection() == Direction.UP) {
            TileEntityStamper stamper = (TileEntityStamper) tile;
            if (!heldStack.isEmpty() || (heldStack.isEmpty() && player.isShiftKeyDown())) {
                int slot;
                Vector3d hitVec = rayTraceResult.getLocation().subtract(new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
                Direction direction = state.getValue(HORIZONTAL_FACING);

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
                            if (!player.inventory.add(stack)) {
                                world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack.copy()));
                            }
                        }
                    }
                }
            } else if (!stamper.getInventory().getStackInSlot(4).isEmpty() && !player.isShiftKeyDown()) {
                player.setItemInHand(hand, stamper.getInventory().getStackInSlot(4).copy());
                stamper.getInventory().setStackInSlot(4, ItemStack.EMPTY);
            }

            world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), Constants.BlockFlags.BLOCK_UPDATE);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            //If the generator has an inventory, drop stacks stored
            if (world.getBlockEntity(pos) instanceof TileEntityStamper) {
                InventoryUtils.dropInventoryItems(world, pos, ((TileEntityStamper) world.getBlockEntity(pos)).getInventory());
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moving) {
        super.neighborChanged(state, world, pos, block, neighborPos, moving);
        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof TileEntityStamper) {
            ((TileEntityStamper) tile).run();
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.setValue(HORIZONTAL_FACING, direction.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING, STATUS);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        VoxelShape bottomShape = Block.box(1, 0, 1, 15, 1, 15);
        VoxelShape baseShape = VoxelShapes.or(bottomShape, Block.box(0, 1, 0, 16, 3, 16));
        VoxelShape topShape;
        if (state.getValue(STATUS) == StamperStatus.OPEN) {
            topShape = Block.box(0, 0, 13, 16, 16, 16);
            topShape = BlockUtils.rotateShape(topShape, state.getValue(HORIZONTAL_FACING));
        } else {
            topShape = Block.box(0, 3, 0, 16, 5, 16);
        }

        return VoxelShapes.or(baseShape, topShape);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new TileEntityStamper();
    }
}
