package io.github.pouffy.agrestic.core.fluid.transfer.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferType;
import io.github.pouffy.agrestic.core.fluid.transfer.IFluidContainerTransfer;
import io.github.pouffy.agrestic.init.AgresticFluidTransferTypes;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class FillFluidContainerTransfer implements IFluidContainerTransfer.WithDirection {

    protected final Ingredient input;
    protected final ItemStack result;
    protected final SizedFluidIngredient fluid;

    public static final MapCodec<FillFluidContainerTransfer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter((transfer) -> transfer.input),
                    ItemStack.CODEC.fieldOf("result").forGetter((transfer) -> transfer.result),
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter((transfer) -> transfer.fluid)
            ).apply(instance, FillFluidContainerTransfer::new));

    @Override
    public FluidTransferType<?> getType() {
        return AgresticFluidTransferTypes.FILL_ITEM.get();
    }

    @Override
    public void addRepresentativeItems(Consumer<Item> consumer) {
        for (ItemStack stack : input.getItems()) {
            consumer.accept(stack.getItem());
        }
    }

    @Override
    public boolean matches(ItemStack stack, FluidStack fluid) {
        return input.test(stack) && this.fluid.test(fluid);
    }

    protected ItemStack getFilled(FluidStack drained) {
        return this.result.copy();
    }

    @Nullable
    @Override
    public TransferResult transfer(ItemStack stack, FluidStack fluid, IFluidHandler handler, TransferDirection direction) {
        if (!direction.canFill()) {
            return null;
        }
        if (handler instanceof AgresticFluidTank agresticTank) {
            if (!agresticTank.canExtract()) return null;
        }
        int amount = this.fluid.amount();
        FluidStack toDrain = new FluidStack(fluid.getFluid(), amount);
        FluidStack simulated = handler.drain(toDrain.copy(), IFluidHandler.FluidAction.SIMULATE);
        if (simulated.getAmount() == amount) {
            FluidStack actual = handler.drain(toDrain.copy(), IFluidHandler.FluidAction.EXECUTE);
            if (actual.getAmount() != amount) {
                Agrestic.LOGGER.error("Wrong amount drained from {}, expected {}, filled {}", BuiltInRegistries.ITEM.getKey(stack.getItem()), fluid.getAmount(), actual.getAmount());
            }
            return new TransferResult(getFilled(toDrain), toDrain, true);
        }
        return null;
    }
}
