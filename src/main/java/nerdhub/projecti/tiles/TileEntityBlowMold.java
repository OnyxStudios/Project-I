package nerdhub.projecti.tiles;

import nerdhub.projecti.api.IBellowsTickable;
import nerdhub.projecti.registry.ModEntities;
import nerdhub.projecti.registry.ModRecipes;
import nerdhub.projecti.utils.InvFluidWrapper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TileEntityBlowMold extends TileEntityBase implements IBellowsTickable {

    public static final int TOTAL_PROGRESS_TIME = 200;

    public ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            TileEntityBlowMold.this.markDirty();
        }
    };

    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME);

    public final LazyOptional<?> capabilityInventory = LazyOptional.of(() -> inventory);
    public final LazyOptional<?> capabilityFluid = LazyOptional.of(() -> tank);

    public int progress;

    public TileEntityBlowMold() {
        super(ModEntities.BLOW_MOLD_TYPE);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        inventory.deserializeNBT(nbt.getCompound("items"));

        if (nbt.contains("FluidData")) {
            this.tank.setFluid(FluidStack.loadFluidStackFromNBT(nbt.getCompound("FluidData")));
        }

        if(tank != null && tank.getFluid() != null) {
            tank.readFromNBT(nbt);
        }

        this.progress = nbt.getInt("progress");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("items", inventory.serializeNBT());

        if (this.tank != null && this.tank.getFluid() != null) {
            CompoundNBT tankTag = new CompoundNBT();
            this.tank.getFluid().writeToNBT(tankTag);
            nbt.put("FluidData", tankTag);
            tank.writeToNBT(nbt);
        }

        nbt.putInt("progress", this.progress);

        return nbt;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) capabilityInventory;
        }else if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) capabilityFluid;
        }

        return null;
    }

    public boolean canRun(Optional<IRecipe<InvFluidWrapper>> recipe) {
        if(tank.isEmpty() || tank.getFluidAmount() < 1000 || inventory.getStackInSlot(0).isEmpty() || !recipe.isPresent() || !inventory.getStackInSlot(1).isEmpty()) {
            return false;
        }

        return true;
    }

    public void run(Optional<IRecipe<InvFluidWrapper>> recipe) {
        tank.setFluid(FluidStack.EMPTY);
        this.inventory.setStackInSlot(1, recipe.get().getRecipeOutput().copy());
        this.progress = 0;
    }

    @Override
    public void addProgress(int amount) {
        if(!world.isRemote) {
            Optional<IRecipe<InvFluidWrapper>> recipe = world.getRecipeManager().getRecipe(ModRecipes.BLOW_MOLD, new InvFluidWrapper(inventory, tank), world);
            if (recipe.isPresent() && canRun(recipe)) {
                this.progress += amount;

                if (progress >= TOTAL_PROGRESS_TIME) {
                    this.run(recipe);
                }

                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }
    }

    @Override
    public int getCurrentProgress() {
        return this.progress;
    }
}
