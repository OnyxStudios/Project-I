package nerdhub.projecti.blocks;

import nerdhub.projecti.tiles.TileEntityBlowMold;
import nerdhub.projecti.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockBlowMold extends ContainerBlock {

    public BlockBlowMold() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.5f));
        this.setRegistryName("blow_mold");
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        ItemStack targetStack;
        TileEntityBlowMold blowMold = (TileEntityBlowMold) world.getTileEntity(pos);

        int slot = blowMold.inventory.getStackInSlot(1).isEmpty() ? 0 : 1;
        boolean extract = heldItem.isItemEqual(blowMold.inventory.getStackInSlot(slot)) || (heldItem.isEmpty() && !blowMold.inventory.getStackInSlot(slot).isEmpty());

        if (extract) {
            if (heldItem.isEmpty()) player.setHeldItem(hand, blowMold.inventory.getStackInSlot(slot)); else player.getHeldItem(hand).grow(1);
            blowMold.inventory.setStackInSlot(slot, ItemStack.EMPTY);

            return true;
        } else if (FluidUtil.getFluidHandler(heldItem).isPresent() || heldItem.getItem() == Items.LAVA_BUCKET) {
            return FluidUtil.interactWithFluidHandler(player, hand, blowMold.tank);
        } else if (blowMold.inventory.getStackInSlot(0).isEmpty()) {// && RECIPES.containsKey(blowMold.inventory.getStackInSlot(0).getItem())){
            targetStack = heldItem.copy();
            targetStack.setCount(1);
            blowMold.inventory.setStackInSlot(0, targetStack);
            heldItem.shrink(1);

            return true;
        }

        return false;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntityBlowMold blowMold = (TileEntityBlowMold) world.getTileEntity(pos);
            InventoryUtils.dropInventoryItems(world, pos, blowMold.inventory);

            super.onReplaced(state, world, pos, newState, isMoving);
        }
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
    public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        VoxelShape baseShape = Block.makeCuboidShape(1, 4, 1, 15, 16, 15);
        VoxelShape bottomShaft = Block.makeCuboidShape(6, 0, 6, 10, 4, 10);
        VoxelShape bottomShape = Block.makeCuboidShape(5, 0, 5, 11, 1, 11);
        return VoxelShapes.or(bottomShaft, VoxelShapes.or(baseShape, bottomShape));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityBlowMold();
    }
}
