package dev.onyxstudios.projecti.entity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class KnowledgeEntity extends LivingEntity {

    private Player owner;
    private int blocksMoved;
    private double moveX, moveY, moveZ;

    public KnowledgeEntity(Level level) {
        super(ModEntities.KNOWLEDGE_ENTITY, level);
    }

    public KnowledgeEntity(EntityType<?> type, Level level) {
        super(ModEntities.KNOWLEDGE_ENTITY, level);
    }

    public KnowledgeEntity(Level level, Player player) {
        super(ModEntities.KNOWLEDGE_ENTITY, level);
        this.owner = player;
        this.blocksMoved = 0;
        this.setPos(player.getX(), player.getY() + 1, player.getZ());
        this.yHeadRot = player.yHeadRot;
        this.yHeadRotO = player.yHeadRotO;
        this.setNoGravity(true);
        double accelX = 1;
        double accelY = 1;
        double accelZ = 1;
        accelX = accelX + level.random.nextGaussian() * 0.4;
        accelY = accelY + level.random.nextGaussian() * 0.4;
        accelZ = accelZ + level.random.nextGaussian() * 0.4;
        double sqrt = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        this.moveX = accelX / sqrt * 0.1;
        this.moveY = accelY / sqrt * 0.1;
        this.moveZ = accelZ / sqrt * 0.1;
    }

    @Override
    public void tick() {
        super.tick();

        if (blocksMoved < 16) {
            blocksMoved++;

            this.getDeltaMovement().add(moveX, 0, moveZ);
            this.getDeltaMovement().multiply(1.01, 0, 1.01);
        }

        if (tickCount >= (20 * 15)) {
            tickCount = 0;
            this.discard();
        }

        if (owner != null && distanceToSqr(owner) > 80) {
            this.discard();
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double distance1 = (this.getBoundingBox().getCenter().length() * 4) * 64;
        return distance < distance1 * distance1;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
