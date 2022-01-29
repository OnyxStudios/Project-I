package dev.onyxstudios.projecti.registry.recipes;

import dev.onyxstudios.projecti.registry.ModRecipes;
import dev.onyxstudios.projecti.utils.InvFluidWrapper;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BlowMoldRecipe implements IRecipe<InvFluidWrapper> {

    protected IRecipeType<?> recipeType;
    protected ResourceLocation ID;
    protected String group;
    protected Ingredient mold;
    protected ItemStack result;
    protected Fluid fluid;

    public BlowMoldRecipe(ResourceLocation id, String group, Ingredient mold, ItemStack result, Fluid fluid) {
        this.recipeType = ModRecipes.BLOW_MOLD;
        this.ID = id;
        this.group = group;
        this.mold = mold;
        this.result = result;
        this.fluid = fluid;
    }

    @Override
    public boolean matches(InvFluidWrapper inv, World world) {
        if (inv.getTank() != null && inv.getTank().getFluid().isFluidEqual(new FluidStack(fluid, 1000)) && mold.test(inv.getItem(0))) {
            return true;
        }

        return false;
    }

    @Override
    public ItemStack assemble(InvFluidWrapper inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.ID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.BLOW_MOLD_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return this.recipeType;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Fluid getFluid() {
        return fluid;
    }
}