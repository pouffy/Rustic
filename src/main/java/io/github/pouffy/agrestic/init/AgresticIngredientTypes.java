package io.github.pouffy.agrestic.init;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.recipe.ComponentIngredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AgresticIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> HELPER = Agrestic.getRegistryHelper().createRegister(NeoForgeRegistries.Keys.INGREDIENT_TYPES);

    public static final DeferredHolder<IngredientType<? extends ICustomIngredient>, IngredientType<ComponentIngredient>> COMPONENTS = register("components", ComponentIngredient.CODEC);

    private static <P extends ICustomIngredient> DeferredHolder<IngredientType<? extends ICustomIngredient>, IngredientType<P>> register(String name, MapCodec<P> codec) {
        return HELPER.register(name, () -> new IngredientType<>(codec));
    }

    public static void staticInit() {}
}
