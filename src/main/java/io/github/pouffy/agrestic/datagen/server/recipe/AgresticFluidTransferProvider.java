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
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import io.github.pouffy.agrestic.init.AgresticTags;
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

        addFillEmpty(output, "apple_juice_bottle", AgresticItems.APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.APPLE_JUICE, AgresticTags.APPLE_JUICE, 250, false);
        addFillEmpty(output, "golden_apple_juice_bottle", AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GOLDEN_APPLE_JUICE, AgresticTags.GOLDEN_APPLE_JUICE, 250, false);
        addFillEmpty(output, "grape_juice_bottle", AgresticItems.GRAPE_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.GRAPE_JUICE, AgresticTags.GRAPE_JUICE, 250, false);
        addFillEmpty(output, "sweet_berry_juice_bottle", AgresticItems.SWEET_BERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.SWEET_BERRY_JUICE, AgresticTags.SWEET_BERRY_JUICE, 250, false);
        addFillEmpty(output, "ironberry_juice_bottle", AgresticItems.IRONBERRY_JUICE_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.IRONBERRY_JUICE, AgresticTags.IRONBERRY_JUICE, 250, false);
        addFillEmpty(output, "ale_wort_bottle", AgresticItems.ALE_WORT_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.ALE_WORT, AgresticTags.ALE_WORT, 250, false);
        addFillEmpty(output, "olive_oil_bottle", AgresticItems.OLIVE_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.OLIVE_OIL, AgresticTags.OLIVE_OIL, 250, false);
        addFillEmpty(output, "vanta_oil_bottle", AgresticItems.VANTA_OIL_BOTTLE, Items.GLASS_BOTTLE, AgresticFluids.VANTA_OIL, AgresticTags.VANTA_OIL, 250, false);

        addTransfer(output, "water_bottle_empty", new EmptyFluidContainerTransfer(
                ComponentIngredient.of(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)).build(), Ingredient.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)),
                new ItemStack(Items.GLASS_BOTTLE), new FluidStack(Fluids.WATER, 250)));
        addTransfer(output, "water_bottle_fill", new FillFluidContainerTransfer(Ingredient.of(Items.GLASS_BOTTLE), PotionContents.createItemStack(Items.POTION, Potions.WATER), SizedFluidIngredient.of(Fluids.WATER, 250)));
    }
}
