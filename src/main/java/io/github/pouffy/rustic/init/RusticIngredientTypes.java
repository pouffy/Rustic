package io.github.pouffy.rustic.init;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.core.fluid.transfer.FluidTransferType;
import io.github.pouffy.rustic.core.fluid.transfer.IFluidContainerTransfer;
import io.github.pouffy.rustic.core.fluid.transfer.type.EmptyFluidContainerTransfer;
import io.github.pouffy.rustic.core.recipe.ComponentIngredient;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RusticIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> HELPER = Rustic.getRegistryHelper().createRegister(NeoForgeRegistries.Keys.INGREDIENT_TYPES);

    public static final DeferredHolder<IngredientType<? extends ICustomIngredient>, IngredientType<ComponentIngredient>> COMPONENTS = register("components", ComponentIngredient.CODEC);

    private static <P extends ICustomIngredient> DeferredHolder<IngredientType<? extends ICustomIngredient>, IngredientType<P>> register(String name, MapCodec<P> codec) {
        return HELPER.register(name, () -> new IngredientType<>(codec));
    }

    public static void staticInit() {}
}
