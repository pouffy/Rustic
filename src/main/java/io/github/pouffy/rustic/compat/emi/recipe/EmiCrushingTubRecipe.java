package io.github.pouffy.rustic.compat.emi.recipe;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.pouffy.rustic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.rustic.compat.emi.FluidSlotWidget;
import io.github.pouffy.rustic.compat.emi.RusticEmiPlugin;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class EmiCrushingTubRecipe extends BasicEmiRecipe {
    private final CrushingTubRecipe recipe;
    private final HolderLookup.Provider registries;

    public EmiCrushingTubRecipe(RecipeHolder<CrushingTubRecipe> holder, HolderLookup.Provider registries) {
        super(RusticEmiPlugin.TUB_CRUSHING, holder.id(), 108, 64);
        this.recipe = holder.value();
        this.registries = registries;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(recipe.getInput()));
    }

    @Override
    public List<EmiStack> getOutputs() {
        FluidStack result = recipe.getResultFluid(this.registries);
        List<EmiStack> outputs = new ArrayList<>();
        if (!recipe.getByproduct().isEmpty()) outputs.add(EmiStack.of(recipe.getByproduct()));
        outputs.add(EmiStack.of(result.getFluid(), result.getComponentsPatch(), result.getAmount()));
        return outputs;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(EmiIngredient.of(recipe.getInput()), 20, 23).recipeContext(this);
        widgets.addTexture(RusticEmiPlugin.ARROW, 42, 23);
        widgets.add(new FluidSlotWidget(recipe.getResultFluid(this.registries), 70, 4, 8000)).recipeContext(this);
        widgets.addSlot(EmiStack.of(recipe.getByproduct()), 70, 42).recipeContext(this);
    }
}
