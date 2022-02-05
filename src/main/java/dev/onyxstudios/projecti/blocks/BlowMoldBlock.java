package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.blockentity.BlowMoldBlockEntity;
import dev.onyxstudios.projecti.utils.InventoryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class BlowMoldBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(7, 1, 7, 9, 3, 9),
            Block.box(1, 3, 1, 15, 4, 15),
            Block.box(0, 4, 2, 1, 14, 14),
            Block.box(15, 4, 2, 16, 14, 14),
            Block.box(2, 4, 0, 14, 14, 1),
            Block.box(2, 4, 15, 14, 14, 16),
            Block.box(1, 4, 1, 15, 16, 2),
            Block.box(1, 4, 14, 15, 16, 15),
            Block.box(1, 4, 2, 2, 16, 14),
            Block.box(14, 4, 2, 15, 16, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BlowMoldBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (level.isClientSide()) super.use(state, level, pos, player, hand, rayTraceResult);
        ItemStack heldItem = player.getItemInHand(hand);
        BlowMoldBlockEntity blowMold = ModEntities.BLOW_MOLD.getBlockEntity(level, pos);

        if (blowMold != null) {
            int slot = blowMold.getInventory().getStackInSlot(1).isEmpty() ? 0 : 1;
            ItemStack invStack = blowMold.getInventory().getStackInSlot(slot);
            if (heldItem.sameItem(invStack) || (heldItem.isEmpty() && !invStack.isEmpty())) {
                invStack.grow(heldItem.getCount());
                player.setItemInHand(hand, invStack);
                blowMold.getInventory().setStackInSlot(slot, ItemStack.EMPTY);
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                return InteractionResult.SUCCESS;
            }

            LazyOptional<IFluidHandlerItem> optional = FluidUtil.getFluidHandler(heldItem);
            if (optional.isPresent()) {
                FluidUtil.interactWithFluidHandler(player, hand, blowMold.getTank());
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                return InteractionResult.SUCCESS;
            }

            if (!heldItem.isEmpty() && blowMold.getInventory().getStackInSlot(0).isEmpty()) {
                blowMold.getInventory().setStackInSlot(0, new ItemStack(heldItem.getItem(), 1));
                player.getItemInHand(hand).shrink(1);
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(state, level, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlowMoldBlockEntity blowMold = ModEntities.BLOW_MOLD.getBlockEntity(level, pos);

            if (blowMold != null) {
                InventoryUtils.dropInventoryItems(level, pos, blowMold.getInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlowMoldBlockEntity(pos, state);
    }
}
