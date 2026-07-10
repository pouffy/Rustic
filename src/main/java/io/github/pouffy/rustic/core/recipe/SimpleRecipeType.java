package io.github.pouffy.rustic.core.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SimpleRecipeType<T extends Recipe<?>> implements RecipeType<T> {

    public final ResourceLocation id;

    public SimpleRecipeType(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
