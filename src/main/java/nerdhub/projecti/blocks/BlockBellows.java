package nerdhub.projecti.blocks;

import nerdhub.projecti.tiles.TileEntityBellows;
import nerdhub.projecti.utils.BlockUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;

import javax.annotation.Nullable;

public class BlockBellows extends ContainerBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public BlockBellows() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.2f));
        this.setRegistryName("bellows");
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
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
        builder.add(FACING);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);

        AxisAlignedBB backFace = BlockUtils.rotateBox(new AxisAlignedBB(0, 0, 16, 16, 16, 14), direction);
        AxisAlignedBB thirdConnection = BlockUtils.rotateBox(new AxisAlignedBB(2, 2, 14, 14, 14, 12), direction);
        AxisAlignedBB secondBase = BlockUtils.rotateBox(new AxisAlignedBB(1, 1, 11, 15, 15, 12), direction);
        AxisAlignedBB secondConnection = BlockUtils.rotateBox(new AxisAlignedBB(4, 4, 11, 12, 12, 8), direction);
        AxisAlignedBB firstBase = BlockUtils.rotateBox(new AxisAlignedBB(1, 1, 7, 15, 15, 8), direction);
        AxisAlignedBB frontFace = BlockUtils.rotateBox(new AxisAlignedBB(0, 0, 5, 16, 16, 3), direction);
        AxisAlignedBB pipe = BlockUtils.rotateBox(new AxisAlignedBB(7, 7, 0, 9, 9, 3), direction);

        return VoxelShapes.or(VoxelShapes.create(backFace), VoxelShapes.create(thirdConnection), VoxelShapes.create(secondBase), VoxelShapes.create(secondConnection), VoxelShapes.create(firstBase), VoxelShapes.create(frontFace), VoxelShapes.create(pipe));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityBellows();
    }
}
