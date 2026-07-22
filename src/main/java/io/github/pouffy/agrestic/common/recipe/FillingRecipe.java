package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.recipe.InWorldRecipe;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.List;

public class FillingRecipe extends InWorldRecipe<SingleRecipeInput> {
    @Getter
    protected Ingredient input;
    protected SizedFluidIngredient fluidInput;

    public static final MapCodec<FillingRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            Ingredient.CODEC.fieldOf("empty").forGetter((recipe) -> recipe.input),
            SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter((recipe) -> recipe.fluidInput),
            ItemStack.CODEC.fieldOf("full").forGetter((recipe) -> recipe.output)
    ).apply(obj, FillingRecipe::new));

    public FillingRecipe(Ingredient input, SizedFluidIngredient fluidInput, ItemStack output) {
        super(AgresticRecipeTypes.Serializers.FILLING.get(), AgresticRecipeTypes.FILLING.get(), output);
        this.input = input;
        this.fluidInput = fluidInput;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.input.test(input.getItem(0));
    }

    public SizedFluidIngredient getRequiredFluid() {
        return fluidInput;
    }
}
