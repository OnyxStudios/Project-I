package nerdhub.projecti.blocks;

import nerdhub.projecti.tiles.TileEntityStamper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCircuitStamper extends ContainerBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<StamperStatus> STATUS = EnumProperty.create("status", StamperStatus.class);

    public BlockCircuitStamper() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.5f));
        this.setRegistryName("circuit_stamper");
        this.setDefaultState(this.getStateContainer().getBaseState().with(STATUS, StamperStatus.OPEN).with(FACING, Direction.NORTH));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntityStamper stamper = (TileEntityStamper) world.getTileEntity(pos);
        ItemStack heldStack = player.getHeldItem(hand);
        if(!world.isRemote && state.get(STATUS) == StamperStatus.OPEN && stamper != null && rayTraceResult.getFace() == Direction.UP) {
            if(!heldStack.isEmpty() || (heldStack.isEmpty() && player.isSneaking())) {
                int slot;
                Vec3d hitVec = rayTraceResult.getHitVec().subtract(new Vec3d(pos));
                Direction direction = state.get(FACING);

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
                        ItemStack stack = stamper.inventory.getStackInSlot(slot);

                        if (stack.isEmpty() && !heldStack.isEmpty()) {
                            stamper.inventory.setStackInSlot(slot, heldStack.split(1));
                        } else if (!stack.isEmpty() && !heldStack.isEmpty()) {
                            if (stack.isItemEqual(heldStack) && stack.getCount() < 64) {
                                int amount = heldStack.getCount() < 64 - stack.getCount() ? heldStack.getCount() : 64 - stack.getCount();
                                stamper.inventory.getStackInSlot(slot).grow(amount);
                                heldStack.shrink(amount);
                            }
                        } else if (!stack.isEmpty() && heldStack.isEmpty()) {
                            if (!player.inventory.addItemStackToInventory(stack)) {
                                world.addEntity(new ItemEntity(world, player.posX, player.posY, player.posZ, stack.copy()));
                            }
                        }
                    }
                }
            }else if(!stamper.inventory.getStackInSlot(4).isEmpty() && !player.isSneaking()){
                player.setHeldItem(hand, stamper.inventory.getStackInSlot(4).copy());
                stamper.inventory.setStackInSlot(4, ItemStack.EMPTY);
            }
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }

        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean p_220069_6_) {
        TileEntityStamper stamper = (TileEntityStamper) world.getTileEntity(pos);

        if(stamper != null) {
            stamper.run();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATUS);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        VoxelShape bottomShape = Block.makeCuboidShape(1, 0, 1, 15, 1, 15);
        VoxelShape baseShape = VoxelShapes.or(bottomShape, Block.makeCuboidShape(0, 1, 0, 16, 3, 16));
        VoxelShape topShape;
        if(state.get(STATUS) == StamperStatus.OPEN) {
            switch (state.get(FACING)) {
                case SOUTH:
                    topShape = Block.makeCuboidShape(0, 0, 0, 16, 16, 3);
                    break;
                case EAST:
                    topShape = Block.makeCuboidShape(0, 0, 0, 3, 16, 16);
                    break;
                case WEST:
                    topShape = Block.makeCuboidShape(16, 0, 0, 13, 16, 16);
                    break;
                case NORTH:
                default:
                    topShape = Block.makeCuboidShape(0, 0, 13, 16, 16, 16);
                    break;
            }
        }else {
            topShape = Block.makeCuboidShape(0, 3, 0, 16, 5, 16);
        }

        return VoxelShapes.or(baseShape, topShape);
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityStamper();
    }

    public enum StamperStatus implements IStringSerializable {
        OPEN,
        CLOSED;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
    }
}
