package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.registry.recipes.BlowMoldRecipe;
import dev.onyxstudios.projecti.registry.recipes.BlowMoldRecipeSerializer;
import dev.onyxstudios.projecti.registry.recipes.StamperRecipe;
import dev.onyxstudios.projecti.registry.recipes.StamperRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectI.MODID);

    public static RegistryObject<RecipeSerializer<?>> STAMPER_SERIALIZER = RECIPES.register("stamper", StamperRecipeSerializer::new);
    public static RegistryObject<RecipeSerializer<?>> BLOW_MOLD_SERIALIZER = RECIPES.register("blow_mold", BlowMoldRecipeSerializer::new);

    public static RecipeType<StamperRecipe> STAMPER = RecipeType.register(ProjectI.MODID + ":stamper");
    public static RecipeType<BlowMoldRecipe> BLOW_MOLD = RecipeType.register(ProjectI.MODID + ":blow_mold");
}
