package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.recipe.InWorldFluidRecipe;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class CrushingTubRecipe extends InWorldFluidRecipe<SingleRecipeInput> {
    @Getter
    protected Ingredient input;
    protected ItemStack byproduct;

    public static final MapCodec<CrushingTubRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            FluidStack.CODEC.fieldOf("output").forGetter((recipe) -> recipe.output),
            Ingredient.CODEC.fieldOf("input").forGetter((recipe) -> recipe.input),
            ItemStack.CODEC.optionalFieldOf("byproduct", ItemStack.EMPTY).forGetter((recipe) -> recipe.byproduct)
    ).apply(obj, CrushingTubRecipe::new));

    public CrushingTubRecipe(FluidStack out, Ingredient in) {
        this(out, in, ItemStack.EMPTY);
    }

    public CrushingTubRecipe(FluidStack out, Ingredient in, ItemStack by) {
        super(AgresticRecipeTypes.Serializers.CRUSHING_TUB.get(), AgresticRecipeTypes.CRUSHING_TUB.get(), out);
        this.input = in;
        this.byproduct = by;
    }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return this.input.test(recipeInput.item());
    }

    public ItemStack getByproduct() {
        return this.byproduct.copy();
    }
}
