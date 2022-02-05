package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.api.block.IBellowsTickable;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModRecipes;
import dev.onyxstudios.projecti.registry.recipes.BlowMoldRecipe;
import dev.onyxstudios.projecti.utils.InvFluidWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlowMoldBlockEntity extends BaseBlockEntity implements IBellowsTickable {

    private final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME);
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            BlowMoldBlockEntity.this.setChanged();
        }
    };

    private final LazyOptional<?> inventoryOptional = LazyOptional.of(() -> inventory);
    private final LazyOptional<?> fluidOptional = LazyOptional.of(() -> tank);
    private int progress;

    public BlowMoldBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.BLOW_MOLD_TYPE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("progress", progress);
        tag.put("inventory", inventory.serializeNBT());
        tag.put("fluid", tank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
        inventory.deserializeNBT(tag.getCompound("inventory"));
        tank.readFromNBT(tag.getCompound("fluid"));
    }

    @Override
    public void addProgress(int amount) {
        progress += amount;

        if (level != null && !level.isClientSide() && progress >= getTotalProgress()) {
            InvFluidWrapper fluidInv = new InvFluidWrapper(inventory, tank);
            Optional<BlowMoldRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.BLOW_MOLD, fluidInv, level);
            recipe.ifPresent(blowMoldRecipe -> {
                if (!canRun(blowMoldRecipe)) return;

                tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                inventory.setStackInSlot(1, blowMoldRecipe.assemble(fluidInv));
            });

            progress = 0;
            this.setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    private boolean canRun(BlowMoldRecipe recipe) {
        return !tank.isEmpty() && tank.getFluidAmount() >= 1000
                && !inventory.getStackInSlot(0).isEmpty()
                && inventory.getStackInSlot(1).isEmpty()
                && tank.getFluid().getFluid().isSame(recipe.getFluid());
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public int getTotalProgress() {
        return 20 * 8;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getTank() {
        return tank;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY)
            return inventoryOptional.cast();
        else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidOptional.cast();

        return super.getCapability(cap, side);
    }
}
