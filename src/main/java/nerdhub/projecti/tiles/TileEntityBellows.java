package nerdhub.projecti.tiles;

import nerdhub.projecti.api.IBellowsTickable;
import nerdhub.projecti.blocks.BlockBellows;
import nerdhub.projecti.registry.ModEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntityBellows extends TileEntityBase implements ITickableTileEntity {

    public int age = 0;
    public int stage = 0;
    public static final int TICKS_PER_STAGE = 60;

    public TileEntityBellows() {
        super(ModEntities.BELLOWS_TYPE);
    }

    @Override
    public void tick() {
        if (world.isBlockPowered(pos)) {
            age++;

            Direction facing = getBlockState().get(BlockBellows.FACING);
            BlockPos offsetPos = pos.offset(facing);
            TileEntity tile = world.getTileEntity(offsetPos);

            if (tile instanceof IBellowsTickable) {
                ((IBellowsTickable) tile).addProgress(1);
            }
        }else if(age > 0) {
            age = 0;
        }
    }
}
