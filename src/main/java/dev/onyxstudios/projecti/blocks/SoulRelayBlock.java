package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.tileentity.SoulRelayTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class SoulRelayBlock extends ContainerBlock {

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public SoulRelayBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion());
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        checkPower(world, pos);
        checkVisitable(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos, isMoving);
        checkPower(world, pos);

        if (neighborBlock != this) {
            checkVisitable(world, pos);
        }
    }

    private void checkVisitable(World world, BlockPos pos) {
        if (world.isClientSide()) return;
        SoulRelayTileEntity soulRelay = ModEntities.SOUL_RELAY_TYPE.get().getBlockEntity(world, pos);

        if (soulRelay != null) {
            for (Direction direction : Direction.values()) {
                if (world.getBlockState(pos.relative(direction)).is(ModBlocks.BONE_CAGE.get()))
                    soulRelay.setCanVisit(false);
            }
        }
    }

    private void updatePower(World world, BlockPos pos, boolean powered) {
        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof SoulRelayTileEntity) {
            ((SoulRelayTileEntity) tile).setPowered(powered);

            if (world.getBlockState(pos.above()).is(this)) {
                updatePower(world, pos.above(), powered);
            }
        }
    }

    private void checkPower(World world, BlockPos pos) {
        if (world.isClientSide()) return;
        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof SoulRelayTileEntity) {
            SoulRelayTileEntity soulRelay = (SoulRelayTileEntity) tile;
            boolean flag = world.hasNeighborSignal(pos);
            if (!flag) {
                BlockPos below = pos.below();

                if (world.getBlockEntity(below) instanceof SoulRelayTileEntity) {
                    SoulRelayTileEntity belowRelay = (SoulRelayTileEntity) world.getBlockEntity(below);

                    if (belowRelay != null && belowRelay.isPowered())
                        flag = true;
                }
            }

            if (soulRelay.isPowered() != flag) {
                updatePower(world, pos, flag);
            }
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new SoulRelayTileEntity();
    }
}
