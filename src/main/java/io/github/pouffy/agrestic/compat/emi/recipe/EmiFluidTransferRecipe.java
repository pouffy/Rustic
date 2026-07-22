package io.github.pouffy.agrestic.compat.emi.recipe;

import com.mojang.datafixers.util.Either;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.EmptyingRecipe;
import io.github.pouffy.agrestic.common.recipe.FillingRecipe;
import io.github.pouffy.agrestic.compat.emi.AgresticEmiPlugin;
import io.github.pouffy.agrestic.compat.emi.FluidTankWidget;
import io.github.pouffy.agrestic.core.fluid.ItemFilling;
import io.github.pouffy.agrestic.mixin.RecipeManagerAccessor;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.crafting.DataComponentFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmiFluidTransferRecipe extends BasicEmiRecipe {
    private final Either<FillingRecipe, EmptyingRecipe> recipe;
    private final HolderLookup.Provider registries;

    private EmiFluidTransferRecipe(Either<RecipeHolder<FillingRecipe>, RecipeHolder<EmptyingRecipe>> recipe, HolderLookup.Provider registries) {
        super(AgresticEmiPlugin.FLUID_TRANSFER, recipe.map(RecipeHolder::id, RecipeHolder::id), 108, 64);
        this.registries = registries;
        this.recipe = recipe.mapBoth(RecipeHolder::value, RecipeHolder::value);
    }

    public static EmiFluidTransferRecipe filling(RecipeHolder<FillingRecipe> recipe, HolderLookup.Provider registries) {
        return new EmiFluidTransferRecipe(Either.left(recipe), registries);
    }

    public static EmiFluidTransferRecipe emptying(RecipeHolder<EmptyingRecipe> recipe, HolderLookup.Provider registries) {
        return new EmiFluidTransferRecipe(Either.right(recipe), registries);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(
                recipe.map(filling -> NeoForgeEmiIngredient.of(filling.getRequiredFluid()), emptying -> EmiIngredient.of(emptying.getInput())),
                recipe.map(filling -> EmiIngredient.of(filling.getInput()), emptying -> EmiStack.EMPTY)
        );
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(
                recipe.map(filling -> EmiStack.of(filling.getResultItem(registries)), emptying -> EmiStack.EMPTY),
                recipe.map(filling -> EmiStack.EMPTY, emptying -> EmiStack.of(emptying.getResultingFluid().getFluid(), emptying.getResultingFluid().getAmount()))
        );
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        recipe.ifLeft((filling) -> addFilling(widgets, filling)).ifRight((emptying) -> addEmptying(widgets, emptying));
    }

    private void addFilling(WidgetHolder widgets, FillingRecipe filling) {
        widgets.add(FluidTankWidget.input(NeoForgeEmiIngredient.of(filling.getRequiredFluid()), 10, 15, filling.getRequiredFluid().amount()));
        widgets.addSlot(EmiIngredient.of(filling.getInput()), 30, 23);
        widgets.addTexture(AgresticEmiPlugin.ARROW_BG, 53, 23);
        widgets.addSlot(EmiStack.of(filling.getResultItem(registries)), 80, 23).recipeContext(this);
    }

    private void addEmptying(WidgetHolder widgets, EmptyingRecipe emptying) {
        widgets.addSlot(EmiIngredient.of(emptying.getInput()), 10, 23);
        widgets.addTexture(AgresticEmiPlugin.ARROW_BG, 33, 23);
        widgets.add(FluidTankWidget.result(emptying.getResultingFluid(), 60, 15, emptying.getResultingFluid().getAmount())).recipeContext(this);
        widgets.addSlot(EmiStack.of(emptying.getResultItem(registries)), 80, 23);
    }

    public static void gatherFillings(EmiRegistry registry) {
        Collection<FluidStack> fluidStacks = indexedFluids();
        HolderLookup.Provider registries = ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries();
        for (ItemStack stack : indexedItems()) {
            IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (capability == null)
                continue;

            int numTanks = capability.getTanks();
            FluidStack existingFluid = numTanks == 1 ? capability.getFluidInTank(0) : FluidStack.EMPTY;

            for (FluidStack fluidStack : fluidStacks) {
                if (numTanks == 1 && (!existingFluid.isEmpty() && !FluidStack.isSameFluidSameComponents(existingFluid, fluidStack)))
                    continue;

                ItemStack copy = stack.copy();
                IFluidHandlerItem fhi = copy.getCapability(Capabilities.FluidHandler.ITEM);
                if (fhi != null) {
                    if (!ItemFilling.isFluidHandlerValid(copy, fhi))
                        continue;
                    FluidStack fluidCopy = fluidStack.copy();
                    fluidCopy.setAmount(1000);
                    fhi.fill(fluidCopy, IFluidHandler.FluidAction.EXECUTE);
                    ItemStack container = fhi.getContainer();
                    if (ItemStack.isSameItemSameComponents(container, copy))
                        continue;
                    if (container.isEmpty())
                        continue;
                    Ingredient bucket = Ingredient.of(stack);
                    ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(stack.getItem());
                    ResourceLocation fluidName = BuiltInRegistries.FLUID.getKey(fluidCopy.getFluid());
                    ResourceLocation id = Agrestic.location("/filling/" + itemName.getNamespace() + "_" + itemName.getPath()
                            + "_with_" + fluidName.getNamespace() + "_" + fluidName.getPath());
                    SizedFluidIngredient fluidIngredient = new SizedFluidIngredient(
                            DataComponentFluidIngredient.of(false, fluidCopy), fluidCopy.getAmount());
                    FillingRecipe fillingRecipe = new FillingRecipe(bucket, fluidIngredient, container);
                    registry.addRecipe(filling(new RecipeHolder<>(id, fillingRecipe), registries));
                }
            }
        }
    }

    public static void gatherEmptying(EmiRegistry registry) {
        ObjectOpenCustomHashSet<ItemStack> emptiedItems = new ObjectOpenCustomHashSet<>(ItemStackLinkedSet.TYPE_AND_TAG);
        HolderLookup.Provider registries = ((RecipeManagerAccessor) registry.getRecipeManager()).getRegistries();
        for (ItemStack stack : indexedItems()) {
            IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (capability == null)
                continue;

            ItemStack copy = stack.copy();
            capability = copy.getCapability(Capabilities.FluidHandler.ITEM);
            FluidStack extracted = capability.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            ItemStack result = capability.getContainer();
            if (extracted.isEmpty())
                continue;
            if (result.isEmpty())
                continue;

            result = ItemStack.isSameItemSameComponents(stack, result) ? stack : emptiedItems.addOrGet(result);

            Ingredient ingredient = Ingredient.of(stack);
            ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(stack.getItem());
            ResourceLocation fluidName = BuiltInRegistries.FLUID.getKey(extracted.getFluid());
            ResourceLocation id = Agrestic.location("/emptying/" + itemName.getNamespace() + "_" + itemName.getPath() + "_of_"
                    + fluidName.getNamespace() + "_" + fluidName.getPath());
            EmptyingRecipe emptyingRecipe = new EmptyingRecipe(ingredient, result, extracted);
            registry.addRecipe(emptying(new RecipeHolder<>(id, emptyingRecipe), registries));
        }
    }

    private static Collection<ItemStack> indexedItems() {
        Collection<ItemStack> indexed = new ArrayList<>();
        try {
            for (Item item : BuiltInRegistries.ITEM) {
                indexed.add(new ItemStack(item, 1));
            }
        } catch (Exception e) {
            Agrestic.LOGGER.error("Error occurred while indexing items", e);
        }
        return indexed;
    }

    private static Collection<FluidStack> indexedFluids() {
        Collection<FluidStack> indexed = new ArrayList<>();
        try {
            for (Fluid fluid : BuiltInRegistries.FLUID) {
                indexed.add(new FluidStack(fluid, 1000));
            }
        } catch (Exception e) {
            Agrestic.LOGGER.error("Error occurred while indexing fluids", e);
        }
        return indexed;
    }
}
