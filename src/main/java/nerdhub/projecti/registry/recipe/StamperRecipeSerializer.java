package nerdhub.projecti.registry.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class StamperRecipeSerializer<T extends StamperRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final StamperRecipeSerializer.IFactory<T> factory;

    public StamperRecipeSerializer(StamperRecipeSerializer.IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getString(json, "group", "");
        ResourceLocation resultLoc = new ResourceLocation(JSONUtils.getString(json, "result", ""));
        ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(resultLoc));
        JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");

        return this.factory.create(recipeId, group, result, getItem(0, ingredients), getItem(1, ingredients), getItem(2, ingredients), getItem(3, ingredients));
    }

    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        return this.factory.create(recipeId, buffer.readString(), buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack(), buffer.readItemStack());
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        buffer.writeString(recipe.group);
        buffer.writeItemStack(recipe.result);
        buffer.writeItemStack(recipe.ingredients[0]);
        buffer.writeItemStack(recipe.ingredients[1]);
        buffer.writeItemStack(recipe.ingredients[2]);
        buffer.writeItemStack(recipe.ingredients[3]);
    }

    public ItemStack getItem(int index, JsonArray array) {
        String name = array.size() >= index ? array.get(index).getAsString() : null;
        if(name == null) return ItemStack.EMPTY;

        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(name)));
    }

    public interface IFactory<T extends StamperRecipe> {
        T create(ResourceLocation id, String group, ItemStack result, ItemStack... ingredients);
    }
}
