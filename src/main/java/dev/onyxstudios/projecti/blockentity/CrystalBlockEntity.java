package dev.onyxstudios.projecti.blockentity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import java.util.Random;

public class CrystalBlockEntity extends BaseBlockEntity {

    private static final Random random = new Random();
    public static final int MAX_TICKS_PER_STAGE = 2400;

    public int age = 0;
    public int stage;

    //If the crystal is rare and which pillar to color
    public boolean rare;
    public int rarePillar;

    //Because minecraft is stupid
    public boolean propsSet;
    public boolean fullyGrown;
    //This return item is so that people can't cheat and spawn in a crystal and insta break it
    public boolean returnItem;
    public Direction randomDir;

    public CrystalBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.CRYSTAL, pos, state);
        randomDir = Direction.getRandom(random);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("age", this.age);
        tag.putInt("stage", this.stage);
        tag.putBoolean("rare", this.rare);
        tag.putInt("rarePillar", this.rarePillar);
        tag.putBoolean("fullyGrown", this.fullyGrown);
        tag.putBoolean("returnItem", this.returnItem);
        tag.putBoolean("propsSet", this.propsSet);
        tag.putString("randomDir", randomDir != null ? this.randomDir.getName() : Direction.NORTH.getName());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.age = tag.getInt("age");
        this.stage = tag.getInt("stage");
        this.rare = tag.getBoolean("rare");
        this.rarePillar = tag.getInt("rarePillar");
        this.fullyGrown = tag.getBoolean("fullyGrown");
        this.returnItem = tag.getBoolean("returnItem");
        this.propsSet = tag.getBoolean("propsSet");
        this.randomDir = Direction.byName(tag.getString("randomDir"));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrystalBlockEntity blockEntity) {
        if (blockEntity.fullyGrown) return;

        if (blockEntity.stage < 4 && blockEntity.age < MAX_TICKS_PER_STAGE) {
            blockEntity.age += level.getBlockState(pos.below()).is(Tags.Blocks.STONE) ? 2 : 1;
        } else if (blockEntity.age >= MAX_TICKS_PER_STAGE)
            blockEntity.grow();

        level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
    }

    public void grow() {
        if (!level.isClientSide && stage < 4) {
            stage++;
            if (stage >= 4)
                this.fullyGrown = true;

            level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Block.UPDATE_ALL);
        }

        age = 0;
    }

    public void setProps(boolean placedByPlayer) {
        this.returnItem = placedByPlayer;
        this.rare = level.random.nextDouble() < 0.2;
        this.rarePillar = level.random.nextInt(4);
        this.propsSet = true;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Block.UPDATE_ALL);
    }
}
