package dev.onyxstudios.projecti.registry.recipes;

import dev.onyxstudios.projecti.registry.ModRecipes;
import dev.onyxstudios.projecti.utils.InvFluidWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BlowMoldRecipe implements Recipe<InvFluidWrapper> {

    protected RecipeType<?> recipeType;
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
    public boolean matches(InvFluidWrapper inv, Level level) {
        return inv.getTank() != null && inv.getTank().getFluid().isFluidEqual(new FluidStack(fluid, 1000)) && mold.test(inv.getItem(0));
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BLOW_MOLD_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
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