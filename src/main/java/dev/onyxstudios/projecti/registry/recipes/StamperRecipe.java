package dev.onyxstudios.projecti.registry.recipes;

import dev.onyxstudios.projecti.registry.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class StamperRecipe implements IRecipe<IInventory> {

    protected IRecipeType<?> recipeType;
    protected ResourceLocation ID;
    protected String group;
    protected Ingredient[] ingredients;
    protected ItemStack result;

    public StamperRecipe(ResourceLocation id, String group, ItemStack result, Ingredient... ingredients) {
        this.recipeType = ModRecipes.STAMPER;
        this.ID = id;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        for (int i = 0; i < 4; i++) {
            ItemStack slotStack = inv.getItem(i);

            if (slotStack.isEmpty() || !ingredients[i].test(slotStack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 2 && height == 2;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, ingredients);
    }

    @Override
    public ResourceLocation getId() {
        return this.ID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.STAMPER_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return this.recipeType;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}