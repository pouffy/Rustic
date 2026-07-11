package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.recipe.CrushingTubRecipe;
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

    public static void staticInit() {
        Serializers.staticInit();
    }

    public static <T extends Recipe<?>> SimpleRecipeType<T> registerRecipeType(final String identifier) {
        return new SimpleRecipeType<>(Agrestic.location(identifier));
    }

    public static class Serializers {
        public static final DeferredRegister<RecipeSerializer<?>> HELPER = Agrestic.getRegistryHelper().createRegister(Registries.RECIPE_SERIALIZER);

        public static final DeferredHolder<RecipeSerializer<?>, SimpleRecipeSerializer<CrushingTubRecipe>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> new SimpleRecipeSerializer<>(CrushingTubRecipe.CODEC));

        public static void staticInit() {}
    }
}
