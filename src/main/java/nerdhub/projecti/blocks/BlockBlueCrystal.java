package nerdhub.projecti.blocks;

import nerdhub.projecti.registry.ModItems;
import nerdhub.projecti.tiles.TileEntityCrystal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBlueCrystal extends ContainerBlock implements IGrowable, IPlantable {

    public BlockBlueCrystal() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).tickRandomly());
        this.setRegistryName("blue_crystal");
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntityCrystal crystal = (TileEntityCrystal) world.getTileEntity(pos);
        if(crystal != null && (crystal.fullyGrown || crystal.stage >= 4)) {
            world.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.MASTER, 0.4F, 0.1f);
            return true;
        }

        return false;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState state2, boolean p_220082_5_) {
        TileEntityCrystal crystal = (TileEntityCrystal) world.getTileEntity(pos);
        if(crystal != null && crystal.stage == 0) {
            crystal.setProperties(false);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntityCrystal crystal = (TileEntityCrystal) world.getTileEntity(pos);
        if(crystal != null && crystal.stage == 0) {
            crystal.setProperties(true);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityCrystal();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean p_220069_6_) {
        if(!world.getBlockState(pos.down()).getMaterial().isSolid()) {
            spawnDrops(state, world, pos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
        super.neighborChanged(state, world, pos, block, neighbor, p_220069_6_);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntityCrystal crystal = (TileEntityCrystal) world.getTileEntity(pos);
        if(crystal != null && !player.isCreative()) {
            spawnDrops(state, world, pos, crystal, player, player.getActiveItemStack());
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder lootBuilder) {
        List<ItemStack> drops = new ArrayList<>();
        Random random = lootBuilder.getWorld().rand;
        TileEntityCrystal crystal = (TileEntityCrystal) lootBuilder.getWorld().getTileEntity(lootBuilder.get(LootParameters.POSITION));

        if(crystal != null) {
            if(crystal.fullyGrown) {
                drops.add(new ItemStack(this, random.nextDouble() <= 0.15 ? 2 : 1));
                if(crystal.rare) {
                    drops.add(new ItemStack(ModItems.YELLOW_CRYSTAL));
                }
            }else if(crystal.returnItem) {
                drops.add(new ItemStack(this));
            }
        }

        return drops;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return Block.makeCuboidShape(4, 0, 4, 12, 12, 12);
    }

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canGrow(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState, boolean b) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState blockState) {
        TileEntityCrystal crystal = (TileEntityCrystal) world.getTileEntity(blockPos);

        return crystal != null && !crystal.fullyGrown;
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, BlockState blockState) {
        TileEntityCrystal crystal = (TileEntityCrystal) world.getTileEntity(blockPos);

        if(crystal != null) {
            crystal.grow();
        }
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.Cave;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos);
    }
}
