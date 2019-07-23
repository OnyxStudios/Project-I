package nerdhub.projecti.entity;

import nerdhub.projecti.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityKnowledge extends LivingEntity {

    private PlayerEntity owner;
    private int blocksMoved;
    private double moveX, moveY, moveZ;

    public EntityKnowledge(EntityType<EntityKnowledge> entityType, World world) {
        super(entityType, world);
    }

    public EntityKnowledge(World world, PlayerEntity player) {
        super(ModEntities.ENTITY_KNOWLEDGE, world);
        this.owner = player;
        this.blocksMoved = 0;
        this.setLocationAndAngles(player.posX, player.posY + 1, player.posZ, player.rotationYaw, player.rotationPitch);
        this.noClip = false;
        this.setNoGravity(true);
        double accelX = 1;
        double accelY = 1;
        double accelZ = 1;
        accelX = accelX + world.rand.nextGaussian() * 0.4;
        accelY = accelY + world.rand.nextGaussian() * 0.4;
        accelZ = accelZ + world.rand.nextGaussian() * 0.4;
        double sqrt = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        this.moveX = accelX / sqrt * 0.1;
        this.moveY = accelY / sqrt * 0.1;
        this.moveZ = accelZ / sqrt * 0.1;
    }

    @Override
    public void tick() {
        super.tick();

        if(blocksMoved < 16) {
            blocksMoved++;

            this.getMotion().add(moveX, 0, moveZ);
            this.getMotion().mul(1.01, 0, 1.01);
        }

        if(ticksExisted >= (20 * 15)) {
            //ticksExisted = 0;
            //this.remove();
        }

        if(owner != null && getDistanceSq(owner) > 80) {
            this.remove();
        }
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public HandSide getPrimaryHand() {
        return null;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean canBeRidden(Entity entity) {
        return false;
    }

    @Override
    public boolean isNonBoss() {
        return true;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double distance1 = (this.getBoundingBox().getAverageEdgeLength() * 4) * 64;
        return distance < distance1 * distance1;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return null;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
    }
}
