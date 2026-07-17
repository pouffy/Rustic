package io.github.pouffy.agrestic.datagen.server.recipe;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.foundation.data.recipe.result.ChanceResult;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.CrushingTubRecipeBuilder;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public class AgresticCrushingProvider extends AgresticRecipeProvider {
    public AgresticCrushingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected String type() {
        return "Tub Crushing";
    }

    protected void buildRecipes(RecipeOutput output) {
        recipe(new FluidStack(AgresticFluids.OLIVE_OIL, 250), Ingredient.of(AgresticItems.OLIVES), output, "olives");
        recipe(new FluidStack(AgresticFluids.IRONBERRY_JUICE, 250), Ingredient.of(AgresticItems.IRONBERRIES), output, "ironberries");
        recipe(new FluidStack(Fluids.WATER, 250), Ingredient.of(Items.SUGAR_CANE), new ChanceResult(new ItemStack(Items.SUGAR, 2), 1), output, "sugar_cane");
        recipe(new FluidStack(AgresticFluids.SWEET_BERRY_JUICE, 250), Ingredient.of(Items.SWEET_BERRIES), output, "sweet_berries");
        recipe(new FluidStack(AgresticFluids.GRAPE_JUICE, 250), Ingredient.of(AgresticItems.GRAPES), output, "grapes");
        //recipe(new FluidStack(AgresticFluids.APPLE_JUICE, 250), Ingredient.of(Items.APPLE), new ItemStack(AgresticBlocks.APPLE_SEEDS), output, "apple");
        recipe(new FluidStack(KrystalCore.HONEY, 250), Ingredient.of(Items.HONEYCOMB), output, "honeycomb");
        //recipe(new FluidStack(AgresticFluids.GOLDEN_APPLE_JUICE, 100), Ingredient.of(Items.GOLDEN_APPLE), new ItemStack(AgresticBlocks.APPLE_SEEDS), output, "golden_apple");
        recipe(new FluidStack(AgresticFluids.VANTA_OIL, 250), Ingredient.of(AgresticBlocks.VANTA_LILY), output, "vanta_lily");
    }

    public void recipe(FluidStack output, Ingredient ingredient, RecipeOutput recipeOutput, String path) {
        recipe(output, ingredient, ChanceResult.EMPTY, recipeOutput, path);
    }

    public void recipe(FluidStack output, Ingredient ingredient, ChanceResult byproduct, RecipeOutput recipeOutput, String path) {
        var builder = new CrushingTubRecipeBuilder(ingredient, output);
        if (byproduct != ChanceResult.EMPTY) builder.byproduct(byproduct);
        builder.save(recipeOutput, Agrestic.location("crushing_tub/").withSuffix(path));
    }
}
