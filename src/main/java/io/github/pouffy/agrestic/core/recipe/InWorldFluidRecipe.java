package io.github.pouffy.agrestic.core.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class InWorldFluidRecipe<T extends RecipeInput> extends InWorldRecipe<T> {

    public final FluidStack output;

    protected InWorldFluidRecipe(RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType, FluidStack output) {
        super(recipeSerializer, recipeType);
        this.output = output;
    }

    public FluidStack finish(T input, HolderLookup.Provider registries) {
        return getResultFluid(registries).copy();
    }

    public FluidStack getResultFluid(HolderLookup.Provider registries) {
        return output;
    }
}
