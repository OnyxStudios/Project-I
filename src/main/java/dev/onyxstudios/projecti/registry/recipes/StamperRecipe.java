package dev.onyxstudios.projecti.registry.recipes;

import dev.onyxstudios.projecti.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class StamperRecipe implements Recipe<Container> {

    protected RecipeType<?> recipeType;
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
    public boolean matches(Container inv, Level level) {
        for (int i = 0; i < 4; i++) {
            ItemStack slotStack = inv.getItem(i);

            if (slotStack.isEmpty() || !ingredients[i].test(slotStack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(Container inv) {
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STAMPER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return this.recipeType;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}