package nerdhub.projecti.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBlueCrystal extends ItemEntity {

    private int inLavaTicks;

    public EntityBlueCrystal(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EntityBlueCrystal(ItemEntity item) {
        super(null, item.getEntityWorld());

        CompoundNBT nbt = item.writeWithoutTypeId(new CompoundNBT());
        this.read(nbt);
    }

    @Override
    public void tick() {
        super.tick();

        if(isInLava()) {
            inLavaTicks++;

            if(inLavaTicks >= 60) {
                //world.setBlockState(getPosition(), ModBlocks.MOLTEN_BLUE_CRYSTAL, 3);
                this.remove();
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isFireDamage();
    }
}
