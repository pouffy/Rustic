package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.recipe.InWorldRecipe;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class EmptyingRecipe extends InWorldRecipe<SingleRecipeInput> {
    @Getter
    protected Ingredient input;
    protected FluidStack fluidOutput;

    public static final MapCodec<EmptyingRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            Ingredient.CODEC.fieldOf("full").forGetter((recipe) -> recipe.input),
            ItemStack.CODEC.fieldOf("empty").forGetter((recipe) -> recipe.output),
            FluidStack.CODEC.fieldOf("fluid").forGetter((recipe) -> recipe.fluidOutput)
    ).apply(obj, EmptyingRecipe::new));

    public EmptyingRecipe(Ingredient input, ItemStack output, FluidStack fluidOutput) {
        super(AgresticRecipeTypes.Serializers.EMPTYING.get(), AgresticRecipeTypes.EMPTYING.get(), output);
        this.input = input;
        this.fluidOutput = fluidOutput;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.input.test(input.getItem(0));
    }

    public FluidStack getResultingFluid() {
        if (fluidOutput.isEmpty())
            throw new IllegalStateException("Emptying Recipe has no fluid output!");
        return fluidOutput;
    }
}
