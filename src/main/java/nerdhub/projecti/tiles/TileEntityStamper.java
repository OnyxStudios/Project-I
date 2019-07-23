package nerdhub.projecti.tiles;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.blocks.BlockCircuitStamper;
import nerdhub.projecti.registry.ModEntities;
import nerdhub.projecti.utils.InvCapWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TileEntityStamper extends TileEntityBase {

    public ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            TileEntityStamper.this.markDirty();
        }
    };

    public final LazyOptional<?> capabilityInventory = LazyOptional.of(() -> inventory);

    public TileEntityStamper() {
        super(ModEntities.STAMPER_TYPE);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        inventory.deserializeNBT(nbt.getCompound("items"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("items", inventory.serializeNBT());
        return nbt;
    }

    public void run() {
        if(!world.isRemote) {
            boolean open = getBlockState().get(BlockCircuitStamper.STATUS) == BlockCircuitStamper.StamperStatus.OPEN;
            if(world.isBlockPowered(pos) && open) {
                Optional<IRecipe<InvCapWrapper>> recipeObj = world.getRecipeManager().getRecipe(ProjectI.STAMPER, new InvCapWrapper(inventory), world);

                if (recipeObj.isPresent()) {
                    IRecipe recipe = recipeObj.get();
                    ItemStack result = recipe.getRecipeOutput();

                    if (!inventory.getStackInSlot(4).isEmpty() && (!inventory.getStackInSlot(4).isItemEqual(result) || inventory.getStackInSlot(4).getCount() >= 64))
                        return;

                    for (int i = 0; i < inventory.getSlots() - 1; i++) {
                        if (!inventory.getStackInSlot(i).isEmpty()) {
                            inventory.getStackInSlot(i).shrink(1);
                        }
                    }

                    if (inventory.getStackInSlot(4).isEmpty()) {
                        result.setCount(1);
                        inventory.setStackInSlot(4, result);
                    } else {
                        inventory.getStackInSlot(4).grow(1);
                    }

                    world.setBlockState(pos, this.getBlockState().with(BlockCircuitStamper.STATUS, BlockCircuitStamper.StamperStatus.CLOSED));
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                }
            }else if(!world.isBlockPowered(pos) && !open) {
                world.setBlockState(pos, this.getBlockState().with(BlockCircuitStamper.STATUS, BlockCircuitStamper.StamperStatus.OPEN));
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (LazyOptional<T>) capabilityInventory : null;
    }
}
