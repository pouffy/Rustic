package io.github.pouffy.rustic.init;

import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.common.recipe.CrushingTubRecipe;
import io.github.pouffy.rustic.core.recipe.SimpleRecipeSerializer;
import io.github.pouffy.rustic.core.recipe.SimpleRecipeType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RusticRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> HELPER = Rustic.getRegistryHelper().createRegister(Registries.RECIPE_TYPE);

    public static final DeferredHolder<RecipeType<?>, SimpleRecipeType<CrushingTubRecipe>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> registerRecipeType("crushing_tub"));

    public static void staticInit() {
        Serializers.staticInit();
    }

    public static <T extends Recipe<?>> SimpleRecipeType<T> registerRecipeType(final String identifier) {
        return new SimpleRecipeType<>(Rustic.location(identifier));
    }

    public static class Serializers {
        public static final DeferredRegister<RecipeSerializer<?>> HELPER = Rustic.getRegistryHelper().createRegister(Registries.RECIPE_SERIALIZER);

        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<CrushingTubRecipe>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> new SimpleRecipeSerializer<>(CrushingTubRecipe.CODEC));

        public static void staticInit() {}
    }
}
