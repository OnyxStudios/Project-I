package nerdhub.projecti.entity;

import nerdhub.projecti.registry.ModEntities;
import nerdhub.projecti.registry.ModItems;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySoulBase extends AnimalEntity {

    public static final DataParameter<String> ENTITY_TYPE = EntityDataManager.createKey(EntitySoulBase.class, DataSerializers.STRING);

    public EntitySoulBase(EntityType<EntitySoulBase> type, World world) {
        super(type, world);
    }

    public EntitySoulBase(World world, EntityType<?> targetEntity) {
        this(ModEntities.ENTITY_SOUL, world);
        this.setEntityType(targetEntity.getRegistryName().toString());
        this.setInvisible(false);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(ENTITY_TYPE, EntityType.PIG.getRegistryName().toString());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    protected boolean canBeRidden(Entity p_184228_1_) {
        return false;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        this.remove();
        return super.processInteract(player, hand);
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        if(nbt.contains("entityType")) {
            setEntityType(nbt.getString("entityType"));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putString("entityType", getEntityType());
    }

    @Override
    public void tick() {
        super.tick();

        if(collided) {
            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, this.getBoundingBox());

            for (ItemEntity item : items) {
                if(item.getItem().isItemEqual(new ItemStack(Items.BONE))) {
                    if(!world.isRemote) {
                        world.addEntity(new ItemEntity(world, item.posX, item.posY, item.posZ, new ItemStack(ModItems.SOUL_BONE, item.getItem().getCount())));
                    }
                    item.remove();
                }
            }
        }
    }

    public void setEntityType(String entityType) {
        this.getDataManager().set(ENTITY_TYPE, entityType);
    }

    public String getEntityType() {
        return this.getDataManager().get(ENTITY_TYPE);
    }
}
