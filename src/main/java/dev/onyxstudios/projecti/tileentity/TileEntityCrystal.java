package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TileEntityCrystal extends TileEntityBase implements ITickableTileEntity {

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
    //This return item is so that people cant cheat and spawn in a crystal and insta break it
    public boolean returnItem;
    public Direction randomDir;

    public TileEntityCrystal() {
        super(ModEntities.CRYSTAL_TYPE.get());
        randomDir = Direction.getRandom(random);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        this.age = nbt.getInt("age");
        this.stage = nbt.getInt("stage");
        this.rare = nbt.getBoolean("rare");
        this.rarePillar = nbt.getInt("rarePillar");
        this.fullyGrown = nbt.getBoolean("fullyGrown");
        this.returnItem = nbt.getBoolean("returnItem");
        this.propsSet = nbt.getBoolean("propsSet");
        this.randomDir = Direction.byName(nbt.getString("randomDir"));
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("age", this.age);
        nbt.putInt("stage", this.stage);
        nbt.putBoolean("rare", this.rare);
        nbt.putInt("rarePillar", this.rarePillar);
        nbt.putBoolean("fullyGrown", this.fullyGrown);
        nbt.putBoolean("returnItem", this.returnItem);
        nbt.putBoolean("propsSet", this.propsSet);
        nbt.putString("randomDir", randomDir != null ? this.randomDir.getName() : Direction.NORTH.getName());
        return nbt;
    }

    @Override
    public void tick() {
        if (!hasLevel() || fullyGrown || level.isClientSide()) return;

        if (stage < 4 && age < MAX_TICKS_PER_STAGE) {
            age += level.getBlockState(getBlockPos().below()).is(Tags.Blocks.STONE) ? 2 : 1;
        } else if (age >= MAX_TICKS_PER_STAGE)
            grow();

        level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Constants.BlockFlags.DEFAULT);
    }

    public void grow() {
        if (!level.isClientSide && stage < 4) {
            stage++;
            if (stage >= 4)
                this.fullyGrown = true;

            level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }

        age = 0;
    }

    public void setProps(boolean placedByPlayer) {
        this.returnItem = placedByPlayer;
        this.rare = level.random.nextDouble() < 0.2;
        this.rarePillar = level.random.nextInt(4);
        this.propsSet = true;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), level.getBlockState(getBlockPos()), Constants.BlockFlags.DEFAULT);
    }
}
