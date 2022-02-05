package dev.onyxstudios.projecti.registry.recipes;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class BlowMoldRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BlowMoldRecipe> {

    @Override
    public BlowMoldRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        Ingredient mold = Ingredient.fromJson(json.get("mold"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "fluid")));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        return new BlowMoldRecipe(recipeId, group, mold, result, fluid);
    }

    @Nullable
    @Override
    public BlowMoldRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        return new BlowMoldRecipe(recipeId, buffer.readUtf(), Ingredient.fromNetwork(buffer), buffer.readItem(), FluidStack.loadFluidStackFromNBT(buffer.readNbt()).getFluid());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BlowMoldRecipe recipe) {
        buffer.writeUtf(recipe.group);
        recipe.mold.toNetwork(buffer);
        buffer.writeItem(recipe.result);
        buffer.writeNbt(new FluidStack(recipe.fluid, 0).writeToNBT(new CompoundTag()));
    }
}