package dev.onyxstudios.projecti.blockentity;

import dev.onyxstudios.projecti.api.block.IBellowsTickable;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BellowsBlockEntity extends BaseBlockEntity implements IAnimatable {

    private final AnimationFactory animationFactory = new AnimationFactory(this);

    public BellowsBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.BELLOWS, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BellowsBlockEntity blockEntity) {
        if (level.hasNeighborSignal(pos)) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos offsetPos = pos.relative(facing);
            BlockEntity tile = level.getBlockEntity(offsetPos);

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

    private PlayState predicate(AnimationEvent<BellowsBlockEntity> event) {
        AnimationController<BellowsBlockEntity> controller = event.getController();
        controller.transitionLengthTicks = 0;
        if (event.getAnimatable().getLevel().hasNeighborSignal(event.getAnimatable().getBlockPos())) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.bellows.deploy", true));
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }
}
