package io.github.pouffy.agrestic.datagen.server.recipe;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.core.KCTags;
import com.pouffydev.krystal_core.foundation.data.provider.server.KrysOutput;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.transfer.AbstractFluidContainerTransferProvider;
import io.github.pouffy.agrestic.core.fluid.transfer.IFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.EmptyFluidContainerTransfer;
import io.github.pouffy.agrestic.core.fluid.transfer.type.FillFluidContainerTransfer;
import io.github.pouffy.agrestic.core.recipe.ComponentIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

public class AgresticFluidTransferProvider extends AbstractFluidContainerTransferProvider {

    public AgresticFluidTransferProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, Agrestic.MODID);
    }

    @Override
    protected void addTransfers(KrysOutput<IFluidContainerTransfer> output, HolderLookup.Provider holderLookup) {
        addFillEmpty(output, "honey_bottle", Items.HONEY_BOTTLE, Items.GLASS_BOTTLE, KrystalCore.HONEY.get(), KCTags.Fluids.HONEY.tag(), 250, false);
        addTransfer(output, "water_bottle_empty", new EmptyFluidContainerTransfer(
                ComponentIngredient.of(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)).build(), Ingredient.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)),
                new ItemStack(Items.GLASS_BOTTLE), new FluidStack(Fluids.WATER, 250)));
        addTransfer(output, "water_bottle_fill", new FillFluidContainerTransfer(Ingredient.of(Items.GLASS_BOTTLE), PotionContents.createItemStack(Items.POTION, Potions.WATER), SizedFluidIngredient.of(Fluids.WATER, 250)));
    }
}
