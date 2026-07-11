package io.github.pouffy.agrestic.core.fluid.transfer.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferType;
import io.github.pouffy.agrestic.init.AgresticFluidTransferTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public class EmptyFluidWithComponentTransfer extends EmptyFluidContainerTransfer {

    public static final MapCodec<EmptyFluidWithComponentTransfer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter((transfer) -> transfer.input),
                    ItemStack.CODEC.fieldOf("result").forGetter((transfer) -> transfer.result),
                    FluidStack.CODEC.fieldOf("fluid").forGetter((transfer) -> transfer.fluid)
            ).apply(instance, EmptyFluidWithComponentTransfer::new));

    public EmptyFluidWithComponentTransfer(Ingredient input, ItemStack result, FluidStack fluid) {
        super(input, result, fluid);
    }

    @Override
    public FluidTransferType<?> getType() {
        return AgresticFluidTransferTypes.EMPTY_ITEM_WITH_COMPONENT.get();
    }

    @Override
    protected FluidStack getFluid(ItemStack stack) {
        FluidStack copy = fluid.copy();
        copy.applyComponents(stack.getComponentsPatch());
        return new FluidStack(fluid.getFluidHolder(), fluid.getAmount(), copy.getComponentsPatch());
    }
}
