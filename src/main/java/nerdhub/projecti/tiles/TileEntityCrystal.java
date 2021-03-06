package nerdhub.projecti.tiles;

import nerdhub.projecti.registry.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

import java.util.Random;

public class TileEntityCrystal extends TileEntityBase implements ITickableTileEntity {

    public int age;
    public int stage;
    public boolean rare;
    public boolean fullyGrown;
    public boolean returnItem;
    public Direction randomDir;

    public static int MAX_TICKS_PER_STAGE = 2400;

    public TileEntityCrystal() {
        super(ModEntities.CRYSTAL_TYPE);
        randomDir = Direction.random(new Random());
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        this.age = nbt.getInt("age");
        this.stage = nbt.getInt("stage");
        this.rare = nbt.getBoolean("rare");
        this.fullyGrown = nbt.getBoolean("fullyGrown");
        this.returnItem = nbt.getBoolean("returnItem");
        this.randomDir = Direction.byName(nbt.getString("randomDir"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("age", this.age);
        nbt.putInt("stage", this.stage);
        nbt.putBoolean("rare", this.rare);
        nbt.putBoolean("returnItem", this.returnItem);
        nbt.putString("randomDir", randomDir != null ? this.randomDir.getName() : Direction.NORTH.getName());
        return nbt;
    }

    @Override
    public void tick() {
        if (!fullyGrown) {
            if (stage < 4 && age < MAX_TICKS_PER_STAGE) {
                age += world.getBlockState(pos.down()).getBlock() == Blocks.STONE ? 2 : 1;

            } else if (age >= MAX_TICKS_PER_STAGE) {
                this.grow();
            }
        } else if (stage >= 4) {
            this.fullyGrown = true;
        }
    }

    public void grow() {
        if(!world.isRemote && stage < 4) {
            stage++;
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

            if(stage >= 4) {
                this.fullyGrown = true;
            }
        }

        age = 0;
    }

    public void setProperties(boolean placedByPlayer) {
        if(!world.isRemote) {
            this.returnItem = placedByPlayer;
            this.rare = world.rand.nextDouble() < 0.2;
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }
}
