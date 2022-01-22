package dev.onyxstudios.projecti.entity.goals;

import com.google.common.collect.ImmutableMap;
import dev.onyxstudios.projecti.entity.SoulEntity;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.tileentity.SoulRelayTileEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.server.ServerWorld;

public class FollowRelayTask extends Task<SoulEntity> {

    private BlockPos targetPos;
    private int cooldown;

    public FollowRelayTask() {
        super(ImmutableMap.of(ModEntities.RELAY_WALK_TARGET.get(), MemoryModuleStatus.VALUE_ABSENT));
    }

    @Override
    public boolean checkExtraStartConditions(ServerWorld world, SoulEntity entity) {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        world.getPoiManager().findAllClosestFirst(
                ModEntities.SOUL_RELAY_INTEREST.get().getPredicate(),
                pos -> {
                    if (entity.isBlacklisted(pos)) return false;

                    TileEntity tile = world.getBlockEntity(pos);
                    if (tile instanceof SoulRelayTileEntity) {
                        if (world.getBlockState(pos.below()).is(ModBlocks.SOUL_RELAY.get())) return false;

                        SoulRelayTileEntity soulRelay = (SoulRelayTileEntity) tile;
                        return soulRelay.isPowered() && soulRelay.canVisit();
                    }

                    return false;
                },
                entity.blockPosition(),
                16,
                PointOfInterestManager.Status.ANY
        ).limit(5).findFirst().ifPresent(pos -> targetPos = pos.immutable());

        cooldown = 20 * 2;
        return targetPos != null;
    }

    @Override
    public void start(ServerWorld world, SoulEntity entity, long time) {
        entity.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5, 0.35);
        entity.getBrain().setMemory(ModEntities.RELAY_WALK_TARGET.get(), GlobalPos.of(world.dimension(), targetPos));
    }

    @Override
    public boolean canStillUse(ServerWorld world, SoulEntity entity, long time) {
        boolean isClose = entity.distanceToSqr(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5) <= 2;
        if (isClose) {
            entity.blacklistPos(targetPos);
        }

        return targetPos != null && !entity.getNavigation().isDone() && !isClose;
    }

    @Override
    public void stop(ServerWorld world, SoulEntity entity, long time) {
        if (targetPos != null) {
            entity.getNavigation().stop();
            targetPos = null;
        }

        cooldown = 20 * 2;
        entity.getBrain().eraseMemory(ModEntities.RELAY_WALK_TARGET.get());
    }
}
