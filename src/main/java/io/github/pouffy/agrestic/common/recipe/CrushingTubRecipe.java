package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.krystal_core.foundation.data.recipe.result.ChanceResult;
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
    protected ChanceResult byproduct;

    public static final MapCodec<CrushingTubRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            FluidStack.CODEC.fieldOf("output").forGetter((recipe) -> recipe.output),
            Ingredient.CODEC.fieldOf("input").forGetter((recipe) -> recipe.input),
            ChanceResult.CODEC.optionalFieldOf("byproduct", ChanceResult.EMPTY).forGetter((recipe) -> recipe.byproduct)
    ).apply(obj, CrushingTubRecipe::new));

    public CrushingTubRecipe(FluidStack out, Ingredient in) {
        this(out, in, ChanceResult.EMPTY);
    }

    public CrushingTubRecipe(FluidStack out, Ingredient in, ChanceResult by) {
        super(AgresticRecipeTypes.Serializers.CRUSHING_TUB.get(), AgresticRecipeTypes.CRUSHING_TUB.get(), out);
        this.input = in;
        this.byproduct = by;
    }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return this.input.test(recipeInput.item());
    }

    public ChanceResult getByproduct() {
        return this.byproduct;
    }
}
