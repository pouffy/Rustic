package io.github.pouffy.agrestic.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiCrushingTubRecipe;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import io.github.pouffy.agrestic.mixin.RecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

@EmiEntrypoint
public class AgresticEmiPlugin implements EmiPlugin {
    public static final EmiTexture TANK = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 0, 0, 18, 34);
    public static final EmiTexture ARROW = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 18, 0, 22, 17);

    public static final EmiStack CRUSHING_TUB = EmiStack.of(AgresticBlocks.CRUSHING_TUB.get());

    public static final EmiRecipeCategory TUB_CRUSHING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "tub_crushing"), CRUSHING_TUB);

    public AgresticEmiPlugin() {
    }

    @Override
    public void register(EmiRegistry registry) {
        Level level = Minecraft.getInstance().level;

        // Tell EMI to add a tab for your category
        registry.addCategory(TUB_CRUSHING);

        // Add all the workstations your category uses
        registry.addWorkstation(TUB_CRUSHING, CRUSHING_TUB);
        RecipeManager manager = registry.getRecipeManager();

        for (RecipeHolder<CrushingTubRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.CRUSHING_TUB.get())) {
            registry.addRecipe(new EmiCrushingTubRecipe(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
    }
}
