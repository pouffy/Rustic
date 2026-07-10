package io.github.pouffy.rustic.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.rustic.compat.emi.recipe.EmiCrushingTubRecipe;
import io.github.pouffy.rustic.init.RusticBlocks;
import io.github.pouffy.rustic.init.RusticRecipeTypes;
import io.github.pouffy.rustic.mixin.RecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

@EmiEntrypoint
public class RusticEmiPlugin implements EmiPlugin {
    public static final EmiTexture TANK = new EmiTexture(Rustic.location("textures/gui/emi/widgets.png"), 0, 0, 18, 34);
    public static final EmiTexture ARROW = new EmiTexture(Rustic.location("textures/gui/emi/widgets.png"), 18, 0, 22, 17);

    public static final EmiStack CRUSHING_TUB = EmiStack.of(RusticBlocks.CRUSHING_TUB.get());

    public static final EmiRecipeCategory TUB_CRUSHING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Rustic.MODID, "tub_crushing"), CRUSHING_TUB);

    public RusticEmiPlugin() {
    }

    @Override
    public void register(EmiRegistry registry) {
        Level level = Minecraft.getInstance().level;

        // Tell EMI to add a tab for your category
        registry.addCategory(TUB_CRUSHING);

        // Add all the workstations your category uses
        registry.addWorkstation(TUB_CRUSHING, CRUSHING_TUB);
        RecipeManager manager = registry.getRecipeManager();

        for (RecipeHolder<CrushingTubRecipe> recipe : manager.getAllRecipesFor(RusticRecipeTypes.CRUSHING_TUB.get())) {
            registry.addRecipe(new EmiCrushingTubRecipe(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
    }
}
