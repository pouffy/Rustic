package io.github.pouffy.agrestic.compat.emi.recipe;

import com.pouffydev.krystal_core.foundation.data.recipe.result.ChanceResult;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.compat.emi.FluidSlotWidget;
import io.github.pouffy.agrestic.compat.emi.AgresticEmiPlugin;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class EmiCrushingTubRecipe extends BasicEmiRecipe {
    private final CrushingTubRecipe recipe;
    private final HolderLookup.Provider registries;

    public EmiCrushingTubRecipe(RecipeHolder<CrushingTubRecipe> holder, HolderLookup.Provider registries) {
        super(AgresticEmiPlugin.TUB_CRUSHING, holder.id(), 108, 64);
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
        if (recipe.getByproduct() != ChanceResult.EMPTY) outputs.add(EmiStack.of(recipe.getByproduct().stack()));
        outputs.add(EmiStack.of(result.getFluid(), result.getComponentsPatch(), result.getAmount()));
        return outputs;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(EmiIngredient.of(recipe.getInput()), 20, 23).recipeContext(this);
        widgets.addTexture(AgresticEmiPlugin.ARROW, 42, 23);
        widgets.add(new FluidSlotWidget(recipe.getResultFluid(this.registries), 70, 4, 8000)).recipeContext(this);
        ChanceResult byproduct = recipe.getByproduct();
        SlotWidget byproductSlot = widgets.addSlot(EmiStack.of(byproduct.stack()), 70, 42).recipeContext(this);
        byproductSlot.customBackground(Agrestic.location("textures/gui/emi/widgets.png"), 40, byproduct.chance() < 1 ? 18 : 0, 18, 18);
        if (byproduct.chance() < 1) {
            float chance = byproduct.chance() * 100;
            String formattedChance = chance % 1 == 0 ? String.format("%d%%", (int) chance) : String.format("%.1f%%", chance);
            byproductSlot.appendTooltip(Component.translatable("recipe.agrestic.chance", formattedChance).withStyle(ChatFormatting.GOLD));
        }
    }
}
