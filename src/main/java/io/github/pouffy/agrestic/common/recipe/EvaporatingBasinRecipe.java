package io.github.pouffy.agrestic.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.recipe.FluidRecipeInput;
import io.github.pouffy.agrestic.core.recipe.InWorldRecipe;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class EvaporatingBasinRecipe extends InWorldRecipe<EvaporatingRecipeInput> {
    @Getter
    protected SizedFluidIngredient input;
    @Getter
    protected int time;

    public static final MapCodec<EvaporatingBasinRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
            SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter((recipe) -> recipe.input),
            ItemStack.CODEC.fieldOf("output").forGetter((recipe) -> recipe.output),
            ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter((recipe) -> recipe.time)
    ).apply(obj, EvaporatingBasinRecipe::new));

    public EvaporatingBasinRecipe(SizedFluidIngredient input, ItemStack output, int time) {
        super(AgresticRecipeTypes.Serializers.EVAPORATING_BASIN.get(), AgresticRecipeTypes.EVAPORATING_BASIN.get(), output);
        this.input = input;
        this.time = time;
    }

    @Override
    public boolean matches(EvaporatingRecipeInput input, Level level) {
        boolean correctFluid = this.getInput().test(input.getFluid());

        ItemStack storedStack = input.contained();

        return correctFluid && (storedStack.isEmpty() || (ItemStack.isSameItemSameComponents(storedStack, output) && storedStack.getCount() + output.getCount() <= storedStack.getMaxStackSize()));
    }
}
