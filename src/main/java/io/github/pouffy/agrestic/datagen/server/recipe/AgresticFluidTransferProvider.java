package io.github.pouffy.agrestic.datagen.server.recipe;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.core.KCTags;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.EmptyingRecipe;
import io.github.pouffy.agrestic.common.recipe.FillingRecipe;
import io.github.pouffy.agrestic.core.recipe.ComponentIngredient;
import io.github.pouffy.agrestic.datagen.server.recipe.builder.AgresticRecipeProvider;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

public class AgresticFluidTransferProvider extends AgresticRecipeProvider {

    public AgresticFluidTransferProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected String type() {
        return "Filling & Emptying";
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        //Bottles
        fillEmpty(output, "honey_bottle", Items.HONEY_BOTTLE, Items.GLASS_BOTTLE, KrystalCore.HONEY.get(), KCTags.Fluids.HONEY.tag(), 250);

        fillEmpty(output, "apple_juice_bottle", AgresticItems.APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.APPLE_JUICE, AgresticTags.APPLE_JUICE, 250);
        fillEmpty(output, "golden_apple_juice_bottle", AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GOLDEN_APPLE_JUICE, AgresticTags.GOLDEN_APPLE_JUICE, 250);
        fillEmpty(output, "grape_juice_bottle", AgresticItems.GRAPE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GRAPE_JUICE, AgresticTags.GRAPE_JUICE, 250);
        fillEmpty(output, "sweet_berry_juice_bottle", AgresticItems.SWEET_BERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.SWEET_BERRY_JUICE, AgresticTags.SWEET_BERRY_JUICE, 250);
        fillEmpty(output, "ironberry_juice_bottle", AgresticItems.IRONBERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.IRONBERRY_JUICE, AgresticTags.IRONBERRY_JUICE, 250);
        fillEmpty(output, "ale_wort_bottle", AgresticItems.ALE_WORT_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.ALE_WORT, AgresticTags.ALE_WORT, 250);
        fillEmpty(output, "olive_oil_bottle", AgresticItems.OLIVE_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.OLIVE_OIL, AgresticTags.OLIVE_OIL, 250);
        fillEmpty(output, "vanta_oil_bottle", AgresticItems.VANTA_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.VANTA_OIL, AgresticTags.VANTA_OIL, 250);

        //Buckets
        fillEmpty(output, "honey_bucket", KrystalCore.HONEY_BUCKET.get(), Items.BUCKET, KrystalCore.HONEY.get(), KCTags.Fluids.HONEY.tag(), 1000);
        //fillEmpty(output, "milk_bucket", Items.MILK_BUCKET, Items.BUCKET, NeoForgeMod.MILK.get(), Tags.Fluids.MILK, 1000);

        fillEmpty(output, "apple_juice_bucket", AgresticItems.APPLE_JUICE_BUCKET, Items.BUCKET, AgresticFluids.APPLE_JUICE, AgresticTags.APPLE_JUICE, 1000);
        fillEmpty(output, "golden_apple_juice_bucket", AgresticItems.GOLDEN_APPLE_JUICE_BUCKET, Items.BUCKET, AgresticFluids.GOLDEN_APPLE_JUICE, AgresticTags.GOLDEN_APPLE_JUICE, 1000);
        fillEmpty(output, "grape_juice_bucket", AgresticItems.GRAPE_JUICE_BUCKET, Items.BUCKET, AgresticFluids.GRAPE_JUICE, AgresticTags.GRAPE_JUICE, 1000);
        fillEmpty(output, "sweet_berry_juice_bucket", AgresticItems.SWEET_BERRY_JUICE_BUCKET, Items.BUCKET, AgresticFluids.SWEET_BERRY_JUICE, AgresticTags.SWEET_BERRY_JUICE, 1000);
        fillEmpty(output, "ironberry_juice_bucket", AgresticItems.IRONBERRY_JUICE_BUCKET, Items.BUCKET, AgresticFluids.IRONBERRY_JUICE, AgresticTags.IRONBERRY_JUICE, 1000);
        fillEmpty(output, "ale_wort_bucket", AgresticItems.ALE_WORT_BUCKET, Items.BUCKET, AgresticFluids.ALE_WORT, AgresticTags.ALE_WORT, 1000);
        fillEmpty(output, "olive_oil_bucket", AgresticItems.OLIVE_OIL_BUCKET, Items.BUCKET, AgresticFluids.OLIVE_OIL, AgresticTags.OLIVE_OIL, 1000);
        fillEmpty(output, "vanta_oil_bucket", AgresticItems.VANTA_OIL_BUCKET, Items.BUCKET, AgresticFluids.VANTA_OIL, AgresticTags.VANTA_OIL, 1000);

        empty(new FluidStack(Fluids.WATER, 250), ComponentIngredient.of(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)).build(), Ingredient.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)), new ItemStack(Items.GLASS_BOTTLE), output, "water_bottle");
        fill(SizedFluidIngredient.of(Fluids.WATER, 250), Ingredient.of(Items.GLASS_BOTTLE), PotionContents.createItemStack(Items.POTION, Potions.WATER), output, "water_bottle");
    }


    public void fillEmpty(RecipeOutput recipeOutput, String path, ItemLike filled, ItemLike empty, Fluid fluid, TagKey<Fluid> tag, int amount) {
        empty(new FluidStack(fluid, amount), Ingredient.of(filled), new ItemStack(empty), recipeOutput, path);
        fill(SizedFluidIngredient.of(fluid, amount), Ingredient.of(empty), new ItemStack(filled), recipeOutput, path);
    }

    public void empty(FluidStack fluid, Ingredient empty, ItemStack filled, RecipeOutput recipeOutput, String path) {
        recipeOutput.accept(Agrestic.location("emptying/").withSuffix(path), new EmptyingRecipe(empty, filled, fluid), null);
    }

    public void fill(SizedFluidIngredient fluid, Ingredient filled, ItemStack empty, RecipeOutput recipeOutput, String path) {
        recipeOutput.accept(Agrestic.location("filling/").withSuffix(path), new FillingRecipe(filled, fluid, empty), null);
    }
}
