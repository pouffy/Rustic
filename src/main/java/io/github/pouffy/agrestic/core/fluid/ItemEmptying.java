package io.github.pouffy.agrestic.core.fluid;

import com.mojang.datafixers.util.Pair;
import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.common.recipe.EmptyingRecipe;
import io.github.pouffy.agrestic.core.recipe.RecipeSearch;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.Optional;

import static net.neoforged.neoforge.fluids.capability.IFluidHandler.*;

public class ItemEmptying {

    public static boolean canItemBeEmptied(Level world, ItemStack stack) {
        if (RecipeSearch.search(world, AgresticRecipeTypes.EMPTYING).findRecipe((r) -> r.matches(new SingleRecipeInput(stack), world)) != null)
            return true;

        IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return false;
        for (int i = 0; i < capability.getTanks(); i++) {
            if (capability.getFluidInTank(i)
                    .getAmount() > 0)
                return true;
        }
        return false;
    }

    public static Pair<FluidStack, ItemStack> emptyItem(Level level, ItemStack stack, boolean simulate) {
        FluidStack resultingFluid = FluidStack.EMPTY;
        ItemStack resultingItem = ItemStack.EMPTY;

        Optional<EmptyingRecipe> recipe = Optional.ofNullable(RecipeSearch.search(level, AgresticRecipeTypes.EMPTYING).findRecipe((r) -> r.matches(new SingleRecipeInput(stack), level)));
        if (recipe.isPresent()) {
            EmptyingRecipe emptyingRecipe = recipe.get();
            ItemStack resultItem = emptyingRecipe.getResultItem(level.registryAccess());
            resultingItem = resultItem.isEmpty() ? ItemStack.EMPTY : resultItem;
            resultingFluid = emptyingRecipe.getResultingFluid(stack);
            if (!simulate)
                stack.shrink(1);
            return Pair.of(resultingFluid, resultingItem);
        }

        ItemStack split = stack.copy();
        split.setCount(1);
        IFluidHandlerItem capability = split.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return Pair.of(resultingFluid, resultingItem);
        resultingFluid = capability.drain(1000, simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE);
        resultingItem = capability.getContainer()
                .copy();
        if (!simulate)
            stack.shrink(1);

        return Pair.of(resultingFluid, resultingItem);
    }
}
