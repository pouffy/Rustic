package io.github.pouffy.rustic.core.fluid.transfer.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.core.fluid.transfer.FluidTransferType;
import io.github.pouffy.rustic.core.fluid.transfer.IFluidContainerTransfer;
import io.github.pouffy.rustic.init.RusticFluidTransferTypes;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class EmptyFluidContainerTransfer implements IFluidContainerTransfer.WithDirection {

    protected final Ingredient input;
    protected final ItemStack result;
    protected final FluidStack fluid;

    public static final MapCodec<EmptyFluidContainerTransfer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter((transfer) -> transfer.input),
                    ItemStack.CODEC.fieldOf("result").forGetter((transfer) -> transfer.result),
                    FluidStack.CODEC.fieldOf("fluid").forGetter((transfer) -> transfer.fluid)
            ).apply(instance, EmptyFluidContainerTransfer::new));

    @Override
    public FluidTransferType<?> getType() {
        return RusticFluidTransferTypes.EMPTY_ITEM.get();
    }

    @Override
    public void addRepresentativeItems(Consumer<Item> consumer) {
        for (ItemStack stack : input.getItems()) {
            consumer.accept(stack.getItem());
        }
    }

    @Override
    public boolean matches(ItemStack stack, FluidStack fluid) {
        return input.test(stack);
    }

    protected FluidStack getFluid(ItemStack stack) {
        return fluid;
    }

    @Nullable
    @Override
    public TransferResult transfer(ItemStack stack, FluidStack fluid, IFluidHandler handler, TransferDirection direction) {
        if (!direction.canEmpty()) {
            return null;
        }
        FluidStack contained = getFluid(stack);
        int simulated = handler.fill(contained.copy(), IFluidHandler.FluidAction.SIMULATE);
        if (simulated == contained.getAmount()) {
            int actual = handler.fill(contained.copy(), IFluidHandler.FluidAction.EXECUTE);
            if (actual > 0) {
                if (actual != this.fluid.getAmount()) {
                    Rustic.LOGGER.error("Wrong amount filled from {}, expected {}, filled {}", BuiltInRegistries.ITEM.getKey(stack.getItem()), this.fluid.getAmount(), actual);
                }
                return new TransferResult(result.copy(), contained, false);
            }
        }
        return null;
    }
}
