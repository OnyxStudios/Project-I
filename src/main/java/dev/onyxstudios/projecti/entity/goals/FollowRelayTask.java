package dev.onyxstudios.projecti.entity.goals;

import com.google.common.collect.ImmutableMap;
import dev.onyxstudios.projecti.blockentity.SoulRelayBlockEntity;
import dev.onyxstudios.projecti.entity.SoulEntity;
import dev.onyxstudios.projecti.registry.ModBlocks;
import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FollowRelayTask extends Behavior<SoulEntity> {

    private BlockPos targetPos;
    private int cooldown;

    public FollowRelayTask() {
        super(ImmutableMap.of(ModEntities.RELAY_WALK_TARGET.get(), MemoryStatus.VALUE_ABSENT));
    }

    @Override
    public boolean checkExtraStartConditions(ServerLevel level, SoulEntity entity) {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        level.getPoiManager().findAllClosestFirst(
                ModEntities.SOUL_RELAY_INTEREST.getPredicate(),
                pos -> {
                    if (entity.isBlacklisted(pos)) return false;

                    BlockEntity tile = level.getBlockEntity(pos);
                    if (tile instanceof SoulRelayBlockEntity) {
                        if (level.getBlockState(pos.below()).is(ModBlocks.SOUL_RELAY)) return false;

                        SoulRelayBlockEntity soulRelay = (SoulRelayBlockEntity) tile;
                        return soulRelay.isPowered() && soulRelay.canVisit();
                    }

                    return false;
                },
                entity.blockPosition(),
                16,
                PoiManager.Occupancy.ANY
        ).limit(5).findFirst().ifPresent(pos -> targetPos = pos.immutable());

        cooldown = 20 * 2;
        return targetPos != null;
    }

    @Override
    public void start(ServerLevel level, SoulEntity entity, long time) {
        entity.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5, 0.35);
        entity.getBrain().setMemory(ModEntities.RELAY_WALK_TARGET.get(), GlobalPos.of(level.dimension(), targetPos));
    }

    @Override
    public boolean canStillUse(ServerLevel level, SoulEntity entity, long time) {
        boolean isClose = entity.distanceToSqr(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5) <= 2;
        if (isClose) {
            entity.blacklistPos(targetPos);
        }

        return targetPos != null && !entity.getNavigation().isDone() && !isClose;
    }

    @Override
    public void stop(ServerLevel level, SoulEntity entity, long time) {
        if (targetPos != null) {
            entity.getNavigation().stop();
            targetPos = null;
        }

        cooldown = 20 * 2;
        entity.getBrain().eraseMemory(ModEntities.RELAY_WALK_TARGET.get());
    }
}
