package nerdhub.projecti.registry.recipe;

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

public class BlowMoldRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlowMoldRecipe> {

    @Override
    public BlowMoldRecipe read(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getString(json, "group", "");
        Ingredient mold = Ingredient.deserialize(json.get("mold"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(JSONUtils.getString(json, "fluid")));
        ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        return new BlowMoldRecipe(recipeId, group, mold, result, fluid);
    }

    @Override
    public BlowMoldRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        return new BlowMoldRecipe(recipeId, buffer.readString(), Ingredient.read(buffer), buffer.readItemStack(), FluidStack.loadFluidStackFromNBT(buffer.readCompoundTag()).getFluid());
    }

    @Override
    public void write(PacketBuffer buffer, BlowMoldRecipe recipe) {
        buffer.writeString(recipe.group);
        recipe.mold.write(buffer);
        buffer.writeItemStack(recipe.result);
        buffer.writeCompoundTag(new FluidStack(recipe.fluid, 0).writeToNBT(new CompoundNBT()));
    }
}
