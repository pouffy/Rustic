package io.github.pouffy.agrestic.core.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class InWorldRecipe<T extends RecipeInput> implements Recipe<T> {

    private final RecipeSerializer<?> recipeSerializer;
    private final RecipeType<?> recipeType;
    public final ItemStack output;

    public InWorldRecipe(RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType, ItemStack output) {
        this.recipeSerializer = recipeSerializer;
        this.recipeType = recipeType;
        this.output = output;
    }

    public InWorldRecipe(RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType) {
        this(recipeSerializer, recipeType, ItemStack.EMPTY);
    }

    @Override
    public ItemStack assemble(T input, HolderLookup.Provider registries) {
        return getResultItem(registries).copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return recipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return recipeType;
    }
}
