package io.github.pouffy.agrestic.core.fluid.transfer;

import com.google.common.collect.Sets;
import com.pouffydev.krystal_core.foundation.data.provider.server.KrysOutput;
import io.github.pouffy.agrestic.core.fluid.transfer.type.EmptyFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.EmptyFluidWithComponentTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.FillFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.FillFluidWithComponentTransfer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractFluidContainerTransferProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final String modid;

    public AbstractFluidContainerTransferProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modid) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "agrestic/fluid_transfer");
        this.registries = registries;
        this.modid = modid;
    }

    protected abstract void addTransfers(KrysOutput<IFluidContainerTransfer> output, HolderLookup.Provider holderLookup);

    public final CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose((provider) -> this.run(output, provider));
    }

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.addTransfers((location, transfer, conditions) -> {
            if (!set.add(location)) {
                throw new IllegalStateException("Duplicate cauldron interaction " + location);
            } else {
                list.add(DataProvider.saveStable(output, registries, IFluidContainerTransfer.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(transfer, conditions)), pathProvider.json(location)));
            }
        }, registries);

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected void addTransfer(KrysOutput<IFluidContainerTransfer> output, String name, IFluidContainerTransfer transfer, ICondition... conditions) {
        save(output, transfer, ResourceLocation.fromNamespaceAndPath(this.modid, name), conditions);
    }

    protected void addFillEmpty(KrysOutput<IFluidContainerTransfer> output, String prefix, ItemLike item, ItemLike container, FluidStack fill, SizedFluidIngredient drain, boolean nbt, ICondition... conditions) {
        if (nbt) {
            addTransfer(output, prefix + "_empty", new EmptyFluidWithComponentTransfer(Ingredient.of(item), new ItemStack(container), fill), conditions);
            addTransfer(output, prefix + "_fill", new FillFluidWithComponentTransfer(Ingredient.of(container), new ItemStack(item), drain), conditions);
        } else {
            addTransfer(output, prefix + "_empty", new EmptyFluidContainerTransfer(Ingredient.of(item), new ItemStack(container), fill), conditions);
            addTransfer(output, prefix + "_fill", new FillFluidContainerTransfer(Ingredient.of(container), new ItemStack(item), drain), conditions);
        }
    }

    protected void addFillEmpty(KrysOutput<IFluidContainerTransfer> output, String prefix, ItemLike item, ItemLike container, Fluid fluid, TagKey<Fluid> tag, int amount, boolean nbt, ICondition... conditions) {
        addFillEmpty(output, prefix, item, container, new FluidStack(fluid, amount), SizedFluidIngredient.of(tag, amount), nbt, conditions);
    }

    public void save(KrysOutput<IFluidContainerTransfer> output, IFluidContainerTransfer transfer, ResourceLocation location) {
        output.accept(location, transfer);
    }

    public void save(KrysOutput<IFluidContainerTransfer> output, IFluidContainerTransfer transfer, ResourceLocation location, ICondition... conditions) {
        output.accept(location, transfer, conditions);
    }

    @Override
    public String getName() {
        return "Fluid Container Transfers for %s".formatted(this.modid);
    }
}
