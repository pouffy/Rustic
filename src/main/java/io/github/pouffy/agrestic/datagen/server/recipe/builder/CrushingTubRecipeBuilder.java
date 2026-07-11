package io.github.pouffy.agrestic.datagen.server.recipe.builder;

import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class CrushingTubRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final FluidStack output;
    private ItemStack byproduct = ItemStack.EMPTY;

    public CrushingTubRecipeBuilder(Ingredient ingredient, FluidStack output) {
        this.ingredient = ingredient;
        this.output = output;
    }

    public CrushingTubRecipeBuilder byproduct(@Nonnull ItemStack byproduct) {
        this.byproduct = byproduct;
        return this;
    }

    @Override
    public CrushingTubRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return this;
    }

    @Override
    public CrushingTubRecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        CrushingTubRecipe recipe = new CrushingTubRecipe(output, ingredient, byproduct);
        pRecipeOutput.accept(pId, recipe, advancement$builder.build(pId.withPrefix("recipes/crushing_tub/")));
    }
}
