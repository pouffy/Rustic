package io.github.pouffy.agrestic.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.common.recipe.EmptyingRecipe;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.common.recipe.FillingRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiCrushingTubRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiEvaporatingBasinRecipe;
import io.github.pouffy.agrestic.compat.emi.recipe.EmiFluidTransferRecipe;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import io.github.pouffy.agrestic.mixin.RecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

@EmiEntrypoint
public class AgresticEmiPlugin implements EmiPlugin {
    public static final EmiTexture TANK = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 0, 0, 18, 34);
    public static final EmiTexture ARROW = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 18, 0, 22, 17);
    public static final EmiTexture ALT_ARROW = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 18, 17, 22, 16);
    public static final EmiTexture RESULT = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 40, 0, 18, 18);
    public static final EmiTexture RESULT_CHANCE = new EmiTexture(Agrestic.location("textures/gui/emi/widgets.png"), 40, 18, 18, 18);

    public static final EmiStack CRUSHING_TUB = EmiStack.of(AgresticBlocks.CRUSHING_TUB.get());
    public static final EmiStack EVAPORATING_BASIN = EmiStack.of(AgresticBlocks.EVAPORATING_BASIN.get());

    public static final EmiRecipeCategory TUB_CRUSHING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "tub_crushing"), CRUSHING_TUB);
    public static final EmiRecipeCategory BASIN_EVAPORATING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "basin_evaporating"), EVAPORATING_BASIN);
    public static final EmiRecipeCategory FLUID_TRANSFER = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Agrestic.MODID, "fluid_transfer"), EmiStack.of(Items.BUCKET));

    public AgresticEmiPlugin() {
    }

    @Override
    public void register(EmiRegistry registry) {
        Level level = Minecraft.getInstance().level;

        // Tell EMI to add a tab for your category
        registry.addCategory(TUB_CRUSHING);
        registry.addCategory(BASIN_EVAPORATING);
        registry.addCategory(FLUID_TRANSFER);

        // Add all the workstations your category uses
        registry.addWorkstation(TUB_CRUSHING, CRUSHING_TUB);
        registry.addWorkstation(BASIN_EVAPORATING, EVAPORATING_BASIN);
        RecipeManager manager = registry.getRecipeManager();

        for (RecipeHolder<CrushingTubRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.CRUSHING_TUB.get())) {
            registry.addRecipe(new EmiCrushingTubRecipe(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
        for (RecipeHolder<EvaporatingBasinRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.EVAPORATING_BASIN.get())) {
            registry.addRecipe(new EmiEvaporatingBasinRecipe(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
        for (RecipeHolder<EmptyingRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.EMPTYING.get())) {
            registry.addRecipe(EmiFluidTransferRecipe.emptying(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
        for (RecipeHolder<FillingRecipe> recipe : manager.getAllRecipesFor(AgresticRecipeTypes.FILLING.get())) {
            registry.addRecipe(EmiFluidTransferRecipe.filling(recipe, ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries()));
        }
    }
}
