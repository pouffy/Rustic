package io.github.pouffy.agrestic.init;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferType;
import io.github.pouffy.agrestic.core.fluid.transfer.IFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.EmptyFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.EmptyFluidWithComponentTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.FillFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.FillFluidWithComponentTransfer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AgresticFluidTransferTypes {
    public static final DeferredRegister<FluidTransferType<?>> HELPER = Agrestic.getRegistryHelper().createRegister(AgresticRegistries.FLUID_TRANSFER_TYPE_KEY);

    public static final DeferredHolder<FluidTransferType<? extends IFluidContainerTransfer>, FluidTransferType<EmptyFluidContainerTransfer>> EMPTY_ITEM = register("empty_item", EmptyFluidContainerTransfer.CODEC);
    public static final DeferredHolder<FluidTransferType<? extends IFluidContainerTransfer>, FluidTransferType<EmptyFluidWithComponentTransfer>> EMPTY_ITEM_WITH_COMPONENT = register("empty_item_with_components", EmptyFluidWithComponentTransfer.CODEC);
    public static final DeferredHolder<FluidTransferType<? extends IFluidContainerTransfer>, FluidTransferType<FillFluidContainerTransfer>> FILL_ITEM = register("fill_item", FillFluidContainerTransfer.CODEC);
    public static final DeferredHolder<FluidTransferType<? extends IFluidContainerTransfer>, FluidTransferType<FillFluidWithComponentTransfer>> FILL_ITEM_WITH_COMPONENT = register("fill_item_with_components", FillFluidWithComponentTransfer.CODEC);

    private static <P extends IFluidContainerTransfer> DeferredHolder<FluidTransferType<? extends IFluidContainerTransfer>, FluidTransferType<P>> register(String name, MapCodec<P> codec) {
        return HELPER.register(name, () -> new FluidTransferType<>(codec));
    }

    public static void staticInit() {}
}
