package io.github.pouffy.agrestic.core.fluid.transfer.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferType;
import io.github.pouffy.agrestic.init.AgresticFluidTransferTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class FillFluidWithComponentTransfer extends FillFluidContainerTransfer {

    public static final MapCodec<FillFluidWithComponentTransfer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter((transfer) -> transfer.input),
                    ItemStack.CODEC.fieldOf("result").forGetter((transfer) -> transfer.result),
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter((transfer) -> transfer.fluid)
            ).apply(instance, FillFluidWithComponentTransfer::new));

    public FillFluidWithComponentTransfer(Ingredient input, ItemStack result, SizedFluidIngredient fluid) {
        super(input, result, fluid);
    }

    @Override
    public FluidTransferType<?> getType() {
        return AgresticFluidTransferTypes.FILL_ITEM_WITH_COMPONENT.get();
    }

    @Override
    protected ItemStack getFilled(FluidStack drained) {
        ItemStack filled = super.getFilled(drained);
        if (!drained.getComponentsPatch().isEmpty()) {
            filled.applyComponents(drained.getComponentsPatch());
        }
        return filled;
    }
}
