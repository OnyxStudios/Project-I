package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.blockentity.SoulRelayBlockEntity;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SoulRelayBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public SoulRelayBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .noOcclusion());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        checkPower(level, pos);
        checkVisitable(level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        checkPower(level, pos);

        if (neighborBlock != this) {
            checkVisitable(level, pos);
        }
    }

    private void checkVisitable(Level level, BlockPos pos) {
        if (level.isClientSide()) return;
        SoulRelayBlockEntity soulRelay = ModEntities.SOUL_RELAY.getBlockEntity(level, pos);

        if (soulRelay != null) {
            for (Direction direction : Direction.values()) {
                if (level.getBlockState(pos.relative(direction)).is(ModBlocks.BONE_CAGE))
                    soulRelay.setCanVisit(false);
            }
        }
    }

    private void updatePower(Level level, BlockPos pos, boolean powered) {
        BlockEntity tile = level.getBlockEntity(pos);

        if (tile instanceof SoulRelayBlockEntity soulRelay) {
            soulRelay.setPowered(powered);

            if (level.getBlockState(pos.above()).is(this)) {
                updatePower(level, pos.above(), powered);
            }
        }
    }

    private void checkPower(Level level, BlockPos pos) {
        if (level.isClientSide()) return;
        BlockEntity tile = level.getBlockEntity(pos);

        if (tile instanceof SoulRelayBlockEntity soulRelay) {
            boolean flag = level.hasNeighborSignal(pos);

            if (!flag) {
                BlockPos below = pos.below();

                if (level.getBlockEntity(below) instanceof SoulRelayBlockEntity belowRelay && belowRelay.isPowered())
                    flag = true;
            }

            if (soulRelay.isPowered() != flag) {
                updatePower(level, pos, flag);
            }
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulRelayBlockEntity(pos, state);
    }
}
