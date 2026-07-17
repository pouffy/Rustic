package io.github.pouffy.agrestic.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record EvaporatingRecipeInput(FluidStack stack, ItemStack contained) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    public FluidStack getFluid() {
        return stack;
    }

    @Override
    public int size() {
        return 1;
    }
}
