package nerdhub.projecti.registry.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class StamperRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<StamperRecipe> {

    @Override
    public StamperRecipe read(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getString(json, "group", "");
        ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");

        return new StamperRecipe(recipeId, group, result, Ingredient.deserialize(ingredients.get(0)), Ingredient.deserialize(ingredients.get(1)), Ingredient.deserialize(ingredients.get(2)), Ingredient.deserialize(ingredients.get(3)));
    }

    @Override
    public StamperRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        return new StamperRecipe(recipeId, buffer.readString(), buffer.readItemStack(), Ingredient.read(buffer), Ingredient.read(buffer), Ingredient.read(buffer), Ingredient.read(buffer));
    }

    @Override
    public void write(PacketBuffer buffer, StamperRecipe recipe) {
        buffer.writeString(recipe.group);
        buffer.writeItemStack(recipe.result);
        recipe.ingredients[0].write(buffer);
        recipe.ingredients[1].write(buffer);
        recipe.ingredients[2].write(buffer);
        recipe.ingredients[3].write(buffer);
    }
}
