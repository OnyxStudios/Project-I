package nerdhub.projecti.tiles;

import nerdhub.projecti.registry.ModEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityBlowMold extends TileEntityBase {

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

    public FluidTank tank = new FluidTank(1000);

    public final LazyOptional<?> capabilityInventory = LazyOptional.of(() -> inventory);
    public final LazyOptional<?> capabilityFluid = LazyOptional.of(() -> tank);

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

        if (this.tank != null) {
            this.tank.setTileEntity(this);
        }
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
}
