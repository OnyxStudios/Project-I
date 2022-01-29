package dev.onyxstudios.projecti.entity;

import dev.onyxstudios.projecti.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class KnowledgeEntity extends LivingEntity {

    private PlayerEntity owner;
    private int blocksMoved;
    private double moveX, moveY, moveZ;

    public KnowledgeEntity(World world) {
        super(ModEntities.KNOWLEDGE_ENTITY.get(), world);
    }

    public KnowledgeEntity(EntityType<?> type, World world) {
        super(ModEntities.KNOWLEDGE_ENTITY.get(), world);
    }

    public KnowledgeEntity(World world, PlayerEntity player) {
        super(ModEntities.KNOWLEDGE_ENTITY.get(), world);
        this.owner = player;
        this.blocksMoved = 0;
        this.setPos(player.getX(), player.getY() + 1, player.getZ());
        this.yHeadRot = player.yHeadRot;
        this.yHeadRotO = player.yHeadRotO;
        this.setNoGravity(true);
        double accelX = 1;
        double accelY = 1;
        double accelZ = 1;
        accelX = accelX + world.random.nextGaussian() * 0.4;
        accelY = accelY + world.random.nextGaussian() * 0.4;
        accelZ = accelZ + world.random.nextGaussian() * 0.4;
        double sqrt = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

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
            this.remove();
        }

        if (owner != null && distanceToSqr(owner) > 80) {
            this.remove();
        }
    }

    @Override
    public HandSide getMainArm() {
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
    public ItemStack getItemBySlot(EquipmentSlotType p_184582_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType p_184201_1_, ItemStack p_184201_2_) {
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
