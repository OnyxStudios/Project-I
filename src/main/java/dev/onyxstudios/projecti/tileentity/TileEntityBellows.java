package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.api.block.IBellowsTickable;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TileEntityBellows extends TileEntityBase implements ITickableTileEntity, IAnimatable {

    private final AnimationFactory animationFactory = new AnimationFactory(this);

    public TileEntityBellows() {
        super(ModEntities.BELLOWS_TYPE.get());
    }

    @Override
    public void tick() {
        if (level.hasNeighborSignal(getBlockPos())) {
            Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise();
            BlockPos offsetPos = getBlockPos().relative(facing);
            TileEntity tile = level.getBlockEntity(offsetPos);

            if (tile instanceof IBellowsTickable)
                ((IBellowsTickable) tile).addProgress(1);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    private PlayState predicate(AnimationEvent<TileEntityBellows> event) {
        AnimationController<TileEntityBellows> controller = event.getController();
        controller.transitionLengthTicks = 0;
        if (event.getAnimatable().getLevel().hasNeighborSignal(event.getAnimatable().getBlockPos())) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.bellows.deploy", true));
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }
}
