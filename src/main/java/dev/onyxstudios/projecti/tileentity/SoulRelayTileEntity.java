package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SoulRelayTileEntity extends BaseTileEntity implements IAnimatable {

    private final AnimationFactory animationFactory = new AnimationFactory(this);
    private boolean powered = false;

    public SoulRelayTileEntity() {
        super(ModEntities.SOUL_RELAY_TYPE.get());
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        tag.putBoolean("powered", powered);
        return tag;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        powered = tag.getBoolean("powered");
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT);
    }

    public boolean isPowered() {
        return powered;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    private PlayState predicate(AnimationEvent<SoulRelayTileEntity> event) {
        AnimationController<SoulRelayTileEntity> controller = event.getController();
        controller.transitionLengthTicks = 0;
        if (event.getAnimatable().isPowered()) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.soul_relay.spin", true));
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }
}
