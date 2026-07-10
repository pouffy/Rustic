package io.github.pouffy.rustic.core.fluid.transfer;

import com.mojang.serialization.Codec;
import io.github.pouffy.rustic.init.RusticRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public interface IFluidContainerTransfer {
    Codec<IFluidContainerTransfer> CODEC = Codec.lazyInitialized(RusticRegistries.FLUID_TRANSFER_TYPE_REGISTRY::byNameCodec).dispatch("type", IFluidContainerTransfer::getType, FluidTransferType::codec);
    Codec<Optional<WithConditions<IFluidContainerTransfer>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(CODEC);

    FluidTransferType<?> getType();

    void addRepresentativeItems(Consumer<Item> consumer);

    boolean matches(ItemStack stack, FluidStack fluid);

    @Nullable
    TransferResult transfer(ItemStack stack, FluidStack fluid, IFluidHandler handler);

    @Nullable
    default TransferResult transfer(ItemStack stack, FluidStack fluid, IFluidHandler handler, TransferDirection direction) {
        return transfer(stack, fluid, handler);
    }

    record TransferResult(ItemStack stack, FluidStack fluid, boolean didFill) {
        public SoundEvent getSound() {
            return didFill ? FluidTransferHelper.getFillSound(fluid) : FluidTransferHelper.getEmptySound(fluid);
        }
    }

    enum TransferDirection {
        /** Attempts to empty the item. If that fails, attempts to fill the item. */
        AUTO,
        /** Empties the item into the tank */
        EMPTY_ITEM,
        /** Fills the item from the tank */
        FILL_ITEM,
        /** Attempts to fill the item. If that fails, attempts to empty the item. */
        REVERSE;

        /** If true, may fill the item */
        public boolean canEmpty() {
            return this != FILL_ITEM;
        }

        /** If true, may empty the item */
        public boolean canFill() {
            return this != EMPTY_ITEM;
        }
    }

    interface WithDirection extends IFluidContainerTransfer {
        @Nullable
        @Override
        TransferResult transfer(ItemStack stack, FluidStack fluid, IFluidHandler handler, TransferDirection direction);

        @Nullable
        @Override
        @Deprecated(forRemoval = true)
        default TransferResult transfer(ItemStack stack, FluidStack fluid, IFluidHandler handler) {
            return transfer(stack, fluid, handler, TransferDirection.AUTO);
        }
    }
}
