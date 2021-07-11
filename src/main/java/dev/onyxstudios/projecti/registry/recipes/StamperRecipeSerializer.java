package dev.onyxstudios.projecti.registry.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class StamperRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<StamperRecipe> {

    @Override
    public StamperRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getAsString(json, "group", "");
        ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
        JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
        if (ingredients.size() < 4) {
            ProjectI.LOGGER.fatal("Unable to read Stamper Recipe " + recipeId.toString() + ", less than 4 ingredients provided!");
            return null;
        }

        return new StamperRecipe(recipeId, group, result, Ingredient.fromJson(ingredients.get(0)), Ingredient.fromJson(ingredients.get(1)), Ingredient.fromJson(ingredients.get(2)), Ingredient.fromJson(ingredients.get(3)));
    }

    @Nullable
    @Override
    public StamperRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        return new StamperRecipe(recipeId, buffer.readUtf(), buffer.readItem(), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(PacketBuffer buffer, StamperRecipe recipe) {
        buffer.writeUtf(recipe.group);
        buffer.writeItem(recipe.result);
        recipe.ingredients[0].toNetwork(buffer);
        recipe.ingredients[1].toNetwork(buffer);
        recipe.ingredients[2].toNetwork(buffer);
        recipe.ingredients[3].toNetwork(buffer);
    }
}
