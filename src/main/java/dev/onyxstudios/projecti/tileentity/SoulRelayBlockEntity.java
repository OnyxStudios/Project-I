package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SoulRelayBlockEntity extends BaseBlockEntity implements IAnimatable {

    private final AnimationFactory animationFactory = new AnimationFactory(this);
    private boolean powered = false;
    private boolean canVisit = true;

    public SoulRelayBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.SOUL_RELAY_TYPE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("powered", powered);
        tag.putBoolean("canVisit", canVisit);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        powered = tag.getBoolean("powered");
        canVisit = tag.getBoolean("canVisit");
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void setCanVisit(boolean canVisit) {
        this.canVisit = canVisit;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean canVisit() {
        return canVisit;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    private PlayState predicate(AnimationEvent<SoulRelayBlockEntity> event) {
        AnimationController<SoulRelayBlockEntity> controller = event.getController();
        controller.transitionLengthTicks = 0;
        if (event.getAnimatable().isPowered()) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.soul_relay.spin", true));
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }
}
