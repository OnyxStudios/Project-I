package dev.onyxstudios.projecti.blocks;

import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModItems;
import dev.onyxstudios.projecti.blockentity.CrystalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlueCrystalBlock extends BaseEntityBlock {

    public BlueCrystalBlock() {
        super(Properties
                .of(Material.GLASS)
                .sound(SoundType.GLASS)
                .strength(2, 2)
                .noOcclusion()
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof CrystalBlockEntity && ((CrystalBlockEntity) tile).fullyGrown) {
            if (!level.isClientSide)
                level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.MASTER, 0.4f, 0.1f);

            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, level, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean isMoving) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (!level.isClientSide && tile instanceof CrystalBlockEntity && !((CrystalBlockEntity) tile).propsSet)
            ((CrystalBlockEntity) tile).setProps(false);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (!level.isClientSide && tile instanceof CrystalBlockEntity crystal)
            crystal.setProps(true);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean p_220069_6_) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (!level.isClientSide() && !canSurvive(state, level, pos) && tile instanceof CrystalBlockEntity crystalEntity) {
            //Make it so only fully grown drop 1, so that players don't get around it and just break bottom  block
            //Ex. Make crystal with water+lapis+bone and break bottom block instantly to drop
            if (crystalEntity.fullyGrown || crystalEntity.returnItem)
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.BLUE_CRYSTAL_ITEM)));

            level.removeBlock(pos, false);
        }

        super.neighborChanged(state, level, pos, block, neighbor, p_220069_6_);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (!level.isClientSide() && tile instanceof CrystalBlockEntity && !player.isCreative()) {
            state.spawnAfterBreak((ServerLevel) level, pos, new ItemStack(ModBlocks.BLUE_CRYSTAL_ITEM));
            dropResources(state, level, pos);
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
        List<ItemStack> drops = new ArrayList<>();
        Random random = lootBuilder.getLevel().random;

        BlockEntity tile = lootBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile instanceof CrystalBlockEntity crystal && crystal.fullyGrown) {
            int returnAmt = crystal.returnItem ? 2 : 1;

            drops.add(new ItemStack(this, returnAmt + (random.nextDouble() <= 0.15 ? 1 : 0)));
            if (crystal.rare) {
                drops.add(new ItemStack(ModItems.YELLOW_CRYSTAL));
            }
        }

        return drops;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(Tags.Blocks.STONE) || below.is(Tags.Blocks.GLASS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext selectionContext) {
        return Block.box(4, 0, 4, 12, 12, 12);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModEntities.CRYSTAL, CrystalBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalBlockEntity(pos, state);
    }
}
