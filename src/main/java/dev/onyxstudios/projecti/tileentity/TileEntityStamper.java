package dev.onyxstudios.projecti.tileentity;

import dev.onyxstudios.projecti.api.block.StamperStatus;
import dev.onyxstudios.projecti.blocks.CircuitStamperBlock;
import dev.onyxstudios.projecti.registry.ModEntities;
import dev.onyxstudios.projecti.registry.ModRecipes;
import dev.onyxstudios.projecti.utils.InvCapWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TileEntityStamper extends TileEntityBase {

    private ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            TileEntityStamper.this.setChanged();
        }
    };

    private final LazyOptional<IItemHandler> capabilityInventory = LazyOptional.of(() -> inventory);

    public TileEntityStamper() {
        super(ModEntities.STAMPER_TYPE.get());
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        inventory.deserializeNBT(nbt.getCompound("items"));
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.put("items", inventory.serializeNBT());
        return nbt;
    }

    public void run() {
        if (level != null && !level.isClientSide()) {
            boolean open = getBlockState().getValue(CircuitStamperBlock.STATUS) == StamperStatus.OPEN;
            boolean powered = level.hasNeighborSignal(getBlockPos());
            if (powered && open) {
                Optional<IRecipe<InvCapWrapper>> recipeObj = level.getRecipeManager().getRecipeFor(ModRecipes.STAMPER, new InvCapWrapper(inventory), level);

                if (recipeObj.isPresent()) {
                    IRecipe recipe = recipeObj.get();
                    ItemStack result = recipe.getResultItem();

                    if (!inventory.getStackInSlot(4).isEmpty() && (!inventory.getStackInSlot(4).sameItem(result) || inventory.getStackInSlot(4).getCount() >= 64))
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

                    level.setBlockAndUpdate(getBlockPos(), this.getBlockState().setValue(CircuitStamperBlock.STATUS, StamperStatus.CLOSED));
                }
            } else if (!powered && !open) {
                level.setBlockAndUpdate(getBlockPos(), this.getBlockState().setValue(CircuitStamperBlock.STATUS, StamperStatus.OPEN));
            }
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? capabilityInventory.cast() : super.getCapability(cap, side);
    }
}
