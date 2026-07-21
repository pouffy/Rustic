package io.github.pouffy.agrestic.core.fluid;

import io.github.pouffy.agrestic.common.recipe.EmptyingRecipe;
import io.github.pouffy.agrestic.common.recipe.FillingRecipe;
import io.github.pouffy.agrestic.core.recipe.RecipeSearch;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

import java.util.List;
import java.util.Optional;

import static net.neoforged.neoforge.fluids.capability.IFluidHandler.*;

public class ItemFilling {

    public static boolean isFluidHandlerValid(ItemStack stack, IFluidHandlerItem fluidHandler) {
        // Not instanceof in case a correct subclass is made
        if (fluidHandler.getClass() == FluidBucketWrapper.class) {
            Item item = stack.getItem();
            // Forge does not patch the FluidBucketWrapper onto subclasses of BucketItem
            if (item.getClass() != BucketItem.class && !(item instanceof MilkBucketItem)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canItemBeFilled(Level world, ItemStack stack) {
        if (RecipeSearch.search(world, AgresticRecipeTypes.FILLING).findRecipe((r) -> r.matches(new SingleRecipeInput(stack), world)) != null)
            return true;
        if (stack.getItem() == Items.GLASS_BOTTLE)
            return true;
        if (stack.getItem() == Items.MILK_BUCKET)
            return false;

        IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return false;
        if (!isFluidHandlerValid(stack, capability))
            return false;
        for (int i = 0; i < capability.getTanks(); i++) {
            if (capability.getFluidInTank(i)
                    .getAmount() < capability.getTankCapacity(i))
                return true;
        }
        return false;
    }

    public static int getRequiredAmountForItem(Level world, ItemStack stack, FluidStack availableFluid) {
        List<FillingRecipe> possibleRecipes = RecipeSearch.search(world, AgresticRecipeTypes.FILLING).findRecipes((r) -> r.matches(new SingleRecipeInput(stack), world));
        Optional<FillingRecipe> recipe = possibleRecipes.stream().filter((r) -> r.getRequiredFluid().test(availableFluid)).findFirst();
        if (recipe.isPresent()) {
            return recipe.get().getRequiredFluid().amount();
        }

        if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(availableFluid))
            return 250;
        if (stack.getItem() == Items.BUCKET && canFillBucketInternally(availableFluid))
            return 1000;

        IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return -1;
        if (capability instanceof FluidBucketWrapper) {
            Item filledBucket = availableFluid.getFluid()
                    .getBucket();
            if (filledBucket == Items.AIR)
                return -1;
            if (!((FluidBucketWrapper) capability).getFluid()
                    .isEmpty())
                return -1;
            return 1000;
        }

        int filled = capability.fill(availableFluid, FluidAction.SIMULATE);
        return filled == 0 ? -1 : filled;
    }

    private static boolean canFillGlassBottleInternally(FluidStack availableFluid) {
        Fluid fluid = availableFluid.getFluid();
        if (fluid.isSame(Fluids.WATER))
            return true;
        return false;
    }

    private static boolean canFillBucketInternally(FluidStack availableFluid) {
        return false;
    }

    public static ItemStack fillItem(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid) {
        FluidStack toFill = availableFluid.copy();
        toFill.setAmount(requiredAmount);
        availableFluid.shrink(requiredAmount);

        List<FillingRecipe> possibleRecipes = RecipeSearch.search(world, AgresticRecipeTypes.FILLING).findRecipes((r) -> r.matches(new SingleRecipeInput(stack), world));
        Optional<FillingRecipe> recipe = possibleRecipes.stream().filter((r) -> r.getRequiredFluid().test(toFill)).findFirst();
        if (recipe.isPresent()) {
            FillingRecipe fillingRecipe = recipe.get();
            ItemStack resultItem = fillingRecipe.getResultItem(world.registryAccess(), toFill);
            if (!resultItem.isEmpty()) {
                stack.shrink(1);
                return resultItem;
            }
        }

        if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(toFill)) {
            ItemStack fillBottle = new ItemStack(Items.GLASS_BOTTLE);
            Fluid fluid = toFill.getFluid();
            if (FluidHelper.isWater(fluid))
                fillBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
            stack.shrink(1);
            return fillBottle;
        }

        ItemStack split = stack.copy();
        split.setCount(1);
        IFluidHandlerItem capability = split.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return ItemStack.EMPTY;
        capability.fill(toFill, FluidAction.EXECUTE);
        ItemStack container = capability.getContainer()
                .copy();
        stack.shrink(1);
        return container;
    }
}
