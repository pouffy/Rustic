package io.github.pouffy.rustic.datagen.server.recipe;

import com.pouffydev.krystal_core.KrystalCore;
import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.datagen.server.recipe.builder.CrushingTubRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public class ModCrushingRecipeProvider extends RecipeProvider {
    public ModCrushingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected void buildRecipes(RecipeOutput output) {
        //recipe(new FluidStack(RusticFluids.OLIVE_OIL, 250), Ingredient.of(RusticItems.OLIVES), output, "olives");
        //recipe(new FluidStack(RusticFluids.IRONBERRY_JUICE, 250), Ingredient.of(RusticItems.IRONBERRIES), output, "ironberries");
        //recipe(new FluidStack(Fluids.WATER, 250), Ingredient.of(Items.REEDS), new ItemStack(Items.SUGAR, 2), output, "reeds");
        //recipe(new FluidStack(RusticFluids.WILDBERRY_JUICE, 250), Ingredient.of(RusticItems.WILDBERRIES), output, "wildberries");
        //recipe(new FluidStack(RusticFluids.GRAPE_JUICE, 250), Ingredient.of(RusticItems.GRAPES), output, "grapes");
        //recipe(new FluidStack(RusticFluids.APPLE_JUICE, 250), Ingredient.of(Items.APPLE), new ItemStack(RusticBlocks.APPLE_SEEDS), output, "apple");
        recipe(new FluidStack(KrystalCore.HONEY, 250), Ingredient.of(Items.HONEYCOMB), output, "honeycomb");
        //recipe(new FluidStack(RusticFluids.GOLDEN_APPLE_JUICE, 100), Ingredient.of(Items.GOLDEN_APPLE), new ItemStack(RusticBlocks.APPLE_SEEDS), output, "golden_apple");
        //recipe(new FluidStack(RusticFluids.VANTA_OIL, 250), Ingredient.of(RusticItems.VANTA_LILY), output, "vanta_lily");
    }

    public void recipe(FluidStack output, Ingredient ingredient, RecipeOutput recipeOutput, String path) {
        recipe(output, ingredient, ItemStack.EMPTY, recipeOutput, path);
    }

    public void recipe(FluidStack output, Ingredient ingredient, ItemStack byproduct, RecipeOutput recipeOutput, String path) {
        var builder = new CrushingTubRecipeBuilder(ingredient, output);
        if (!byproduct.isEmpty()) builder.byproduct(byproduct);
        builder.save(recipeOutput, Rustic.location("crushing_tub/").withSuffix(path));
    }
}
