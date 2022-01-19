package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.registry.recipes.BlowMoldRecipe;
import dev.onyxstudios.projecti.registry.recipes.BlowMoldRecipeSerializer;
import dev.onyxstudios.projecti.registry.recipes.StamperRecipe;
import dev.onyxstudios.projecti.registry.recipes.StamperRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> recipeRegistry = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectI.MODID);

    public static RegistryObject<IRecipeSerializer<?>> STAMPER_SERIALIZER = recipeRegistry.register("stamper", StamperRecipeSerializer::new);
    public static RegistryObject<IRecipeSerializer<?>> BLOW_MOLD_SERIALIZER = recipeRegistry.register("blow_mold", BlowMoldRecipeSerializer::new);

    public static IRecipeType<StamperRecipe> STAMPER = IRecipeType.register(ProjectI.MODID + ":stamper");
    public static IRecipeType<BlowMoldRecipe> BLOW_MOLD = IRecipeType.register(ProjectI.MODID + ":blow_mold");
}
