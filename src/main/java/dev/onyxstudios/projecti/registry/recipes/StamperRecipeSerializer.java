package dev.onyxstudios.projecti.registry.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class StamperRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<StamperRecipe> {

    @Override
    public StamperRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        if (ingredients.size() < 4) {
            ProjectI.LOGGER.fatal("Unable to read Stamper Recipe " + recipeId.toString() + ", less than 4 ingredients provided!");
            return null;
        }

        return new StamperRecipe(recipeId, group, result, Ingredient.fromJson(ingredients.get(0)), Ingredient.fromJson(ingredients.get(1)), Ingredient.fromJson(ingredients.get(2)), Ingredient.fromJson(ingredients.get(3)));
    }

    @Nullable
    @Override
    public StamperRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return new StamperRecipe(recipeId, buffer.readUtf(), buffer.readItem(), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, StamperRecipe recipe) {
        buffer.writeUtf(recipe.group);
        buffer.writeItem(recipe.result);
        recipe.ingredients[0].toNetwork(buffer);
        recipe.ingredients[1].toNetwork(buffer);
        recipe.ingredients[2].toNetwork(buffer);
        recipe.ingredients[3].toNetwork(buffer);
    }
}
