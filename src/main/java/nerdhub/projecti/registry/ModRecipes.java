package nerdhub.projecti.registry;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.registry.recipe.BlowMoldRecipeSerializer;
import nerdhub.projecti.registry.recipe.StamperRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipes {

    public static final IRecipeType STAMPER = IRecipeType.register(ProjectI.MODID + ":stamper");
    public static final StamperRecipeSerializer STAMPER_SERIALIZER = new StamperRecipeSerializer();

    public static final IRecipeType BLOW_MOLD = IRecipeType.register(ProjectI.MODID + ":blow_mold");
    public static final BlowMoldRecipeSerializer BLOW_MOLD_SERIALIZER = new BlowMoldRecipeSerializer();
}
