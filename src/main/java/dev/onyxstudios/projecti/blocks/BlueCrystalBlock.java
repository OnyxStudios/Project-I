package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModItems;
import dev.onyxstudios.projecti.tileentity.TileEntityCrystal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlueCrystalBlock extends ContainerBlock {

    public BlueCrystalBlock() {
        super(AbstractBlock.Properties
                .of(Material.GLASS)
                .sound(SoundType.GLASS)
                .strength(2, 2)
                .noOcclusion()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileEntityCrystal && ((TileEntityCrystal) tile).fullyGrown) {
            if (!world.isClientSide)
                world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundCategory.MASTER, 0.4f, 0.1f);

            return ActionResultType.SUCCESS;
        }

        return super.use(blockState, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState state2, boolean isMoving) {
        TileEntity tile = world.getBlockEntity(pos);
        if (!world.isClientSide && tile instanceof TileEntityCrystal && !((TileEntityCrystal) tile).propsSet)
            ((TileEntityCrystal) tile).setProps(false);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tile = world.getBlockEntity(pos);
        if (!world.isClientSide && tile instanceof TileEntityCrystal)
            ((TileEntityCrystal) tile).setProps(true);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean p_220069_6_) {
        TileEntity tile = world.getBlockEntity(pos);
        if (!world.isClientSide() && !canSurvive(state, world, pos) && tile instanceof TileEntityCrystal) {
            //TODO When in 1.17 clean up code to java 16
            //Make it so fully grown only drop 1, so that players don't get around it and just break bottom  block
            if (((TileEntityCrystal) tile).fullyGrown || ((TileEntityCrystal) tile).returnItem)
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.BLUE_CRYSTAL_ITEM.get())));

            world.removeBlock(pos, false);
        }

        super.neighborChanged(state, world, pos, block, neighbor, p_220069_6_);
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = world.getBlockEntity(pos);
        if (!world.isClientSide() && tile instanceof TileEntityCrystal && !player.isCreative()) {
            state.spawnAfterBreak((ServerWorld) world, pos, new ItemStack(ModBlocks.BLUE_CRYSTAL_ITEM.get()));
            dropResources(state, world, pos);
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
        List<ItemStack> drops = new ArrayList<>();
        Random random = lootBuilder.getLevel().random;

        TileEntity tile = lootBuilder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        if (tile instanceof TileEntityCrystal) {
            TileEntityCrystal crystal = (TileEntityCrystal) tile;

            if (crystal.fullyGrown) {
                int returnAmt = crystal.returnItem ? 2 : 1;

                drops.add(new ItemStack(this, returnAmt + (random.nextDouble() <= 0.15 ? 1 : 0)));
                if (crystal.rare) {
                    drops.add(new ItemStack(ModItems.YELLOW_CRYSTAL.get()));
                }
            }
        }

        return drops;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.below());
        return below.is(Tags.Blocks.STONE) || below.is(Tags.Blocks.DIRT) || below.is(Tags.Blocks.GLASS);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext selectionContext) {
        return Block.box(4, 0, 4, 12, 12, 12);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new TileEntityCrystal();
    }
}
