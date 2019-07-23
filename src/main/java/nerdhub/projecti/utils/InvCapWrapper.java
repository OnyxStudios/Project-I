package nerdhub.projecti.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class InvCapWrapper implements IInventory {

    public NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);

    public InvCapWrapper(ItemStackHandler cap) {
        for (int i = 0; i < cap.getSlots(); i++) {
            inventory.set(i, cap.getStackInSlot(i));
        }
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        return ItemStackHelper.getAndSplit(inventory, i, i1);
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(inventory, i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
