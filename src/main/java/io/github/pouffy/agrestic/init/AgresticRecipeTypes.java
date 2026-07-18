package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.agrestic.common.recipe.EmptyingRecipe;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.common.recipe.FillingRecipe;
import io.github.pouffy.agrestic.core.recipe.ShapelessNoReturnRecipe;
import io.github.pouffy.agrestic.core.recipe.SimpleRecipeSerializer;
import io.github.pouffy.agrestic.core.recipe.SimpleRecipeType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AgresticRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> HELPER = Agrestic.getRegistryHelper().createRegister(Registries.RECIPE_TYPE);

    public static final DeferredHolder<RecipeType<?>, SimpleRecipeType<CrushingTubRecipe>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> registerRecipeType("crushing_tub"));
    public static final DeferredHolder<RecipeType<?>, SimpleRecipeType<EvaporatingBasinRecipe>> EVAPORATING_BASIN = HELPER.register("evaporating_basin", () -> registerRecipeType("evaporating_basin"));
    public static final DeferredHolder<RecipeType<?>, SimpleRecipeType<EmptyingRecipe>> EMPTYING = HELPER.register("emptying", () -> registerRecipeType("emptying"));
    public static final DeferredHolder<RecipeType<?>, SimpleRecipeType<FillingRecipe>> FILLING = HELPER.register("filling", () -> registerRecipeType("filling"));

    public static void staticInit() {
        Serializers.staticInit();
    }

    public static <T extends Recipe<?>> SimpleRecipeType<T> registerRecipeType(final String identifier) {
        return new SimpleRecipeType<>(Agrestic.location(identifier));
    }

    public static class Serializers {
        public static final DeferredRegister<RecipeSerializer<?>> HELPER = Agrestic.getRegistryHelper().createRegister(Registries.RECIPE_SERIALIZER);

        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<CrushingTubRecipe>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> new SimpleRecipeSerializer<>(CrushingTubRecipe.CODEC));
        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<EvaporatingBasinRecipe>> EVAPORATING_BASIN = HELPER.register("evaporating_basin", () -> new SimpleRecipeSerializer<>(EvaporatingBasinRecipe.CODEC));

        // Fluid Transfer
        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<EmptyingRecipe>> EMPTYING = HELPER.register("emptying", () -> new SimpleRecipeSerializer<>(EmptyingRecipe.CODEC));
        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<FillingRecipe>> FILLING = HELPER.register("filling", () -> new SimpleRecipeSerializer<>(FillingRecipe.CODEC));

        // Crafting sub-types
        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<ShapelessNoReturnRecipe>> SHAPELESS_NO_RETURN = HELPER.register("shapeless_no_return", () -> new SimpleRecipeSerializer<>(ShapelessNoReturnRecipe.CODEC));

        public static void staticInit() {}
    }
}
