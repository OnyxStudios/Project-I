package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.tileentity.BlowMoldTileEntity;
import dev.onyxstudios.projecti.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class BlowMoldBlock extends ContainerBlock {

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
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public BlowMoldBlock() {
        super(Properties.of(Material.STONE)
                .strength(1.5f, 1.5f)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion());
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (level.isClientSide()) super.use(state, level, pos, player, hand, rayTraceResult);
        ItemStack heldItem = player.getItemInHand(hand);
        BlowMoldTileEntity blowMold = ModEntities.BLOW_MOLD_TYPE.get().getBlockEntity(level, pos);

        if (blowMold != null) {
            int slot = blowMold.getInventory().getStackInSlot(1).isEmpty() ? 0 : 1;
            ItemStack invStack = blowMold.getInventory().getStackInSlot(slot);
            if (heldItem.sameItem(invStack) || (heldItem.isEmpty() && !invStack.isEmpty())) {
                invStack.grow(heldItem.getCount());
                player.setItemInHand(hand, invStack);
                blowMold.getInventory().setStackInSlot(slot, ItemStack.EMPTY);
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Constants.BlockFlags.DEFAULT);
                return ActionResultType.SUCCESS;
            }

            LazyOptional<IFluidHandlerItem> optional = FluidUtil.getFluidHandler(heldItem);
            if (optional.isPresent()) {
                FluidUtil.interactWithFluidHandler(player, hand, blowMold.getTank());
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Constants.BlockFlags.DEFAULT);
                return ActionResultType.SUCCESS;
            }

            if (!heldItem.isEmpty() && blowMold.getInventory().getStackInSlot(0).isEmpty()) {
                blowMold.getInventory().setStackInSlot(0, new ItemStack(heldItem.getItem(), 1));
                player.getItemInHand(hand).shrink(1);
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Constants.BlockFlags.DEFAULT);
                return ActionResultType.SUCCESS;
            }
        }

        return super.use(state, level, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlowMoldTileEntity blowMold = ModEntities.BLOW_MOLD_TYPE.get().getBlockEntity(level, pos);

            if (blowMold != null)
                InventoryUtils.dropInventoryItems(level, pos, blowMold.getInventory());
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new BlowMoldTileEntity();
    }
}
