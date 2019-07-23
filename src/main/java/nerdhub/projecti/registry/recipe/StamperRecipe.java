package nerdhub.projecti.registry.recipe;

import nerdhub.projecti.ProjectI;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Arrays;

public class StamperRecipe implements IRecipe<IInventory> {

    protected IRecipeType<?> recipeType;
    protected ResourceLocation ID;
    protected String group;
    protected ItemStack[] ingredients;
    protected ItemStack result;

    public StamperRecipe(ResourceLocation id, String group, ItemStack result, ItemStack... ingredients) {
        this.recipeType = ProjectI.STAMPER;
        this.ID = id;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        for (int i = 0; i < 4; i++) {
            ItemStack slotStack = inv.getStackInSlot(i);

            if(slotStack.isEmpty() || !slotStack.isItemEqual(ingredients[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width == 2 && height == 2;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        Arrays.asList(ingredients).forEach(stack -> list.add(Ingredient.fromStacks(stack)));
        return list;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.ID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ProjectI.STAMPER_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return this.recipeType;
    }
}
