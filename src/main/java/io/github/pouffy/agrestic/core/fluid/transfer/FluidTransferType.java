package io.github.pouffy.agrestic.core.fluid.transfer;

import com.mojang.serialization.MapCodec;

public record FluidTransferType<T extends IFluidContainerTransfer>(MapCodec<T> codec) {
}
