package dev.onyxstudios.projecti.registry.recipes;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class BlowMoldRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlowMoldRecipe> {

    @Override
    public BlowMoldRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getAsString(json, "group", "");
        Ingredient mold = Ingredient.fromJson(json.get("mold"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "fluid")));
        ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
        return new BlowMoldRecipe(recipeId, group, mold, result, fluid);
    }

    @Nullable
    @Override
    public BlowMoldRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        return new BlowMoldRecipe(recipeId, buffer.readUtf(), Ingredient.fromNetwork(buffer), buffer.readItem(), FluidStack.loadFluidStackFromNBT(buffer.readNbt()).getFluid());
    }

    @Override
    public void toNetwork(PacketBuffer buffer, BlowMoldRecipe recipe) {
        buffer.writeUtf(recipe.group);
        recipe.mold.toNetwork(buffer);
        buffer.writeItem(recipe.result);
        buffer.writeNbt(new FluidStack(recipe.fluid, 0).writeToNBT(new CompoundNBT()));
    }
}