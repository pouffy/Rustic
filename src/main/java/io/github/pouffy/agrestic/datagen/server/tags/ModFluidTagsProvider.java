package io.github.pouffy.agrestic.datagen.server.tags;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends FluidTagsProvider {

    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Agrestic.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        addToTag(AgresticTags.APPLE_JUICE, AgresticFluids.APPLE_JUICE.get(), AgresticFluids.FLOWING_APPLE_JUICE.get());
        addToTag(AgresticTags.GOLDEN_APPLE_JUICE, AgresticFluids.GOLDEN_APPLE_JUICE.get(), AgresticFluids.FLOWING_GOLDEN_APPLE_JUICE.get());
        addToTag(AgresticTags.GRAPE_JUICE, AgresticFluids.GRAPE_JUICE.get(), AgresticFluids.FLOWING_GRAPE_JUICE.get());
        addToTag(AgresticTags.SWEET_BERRY_JUICE, AgresticFluids.SWEET_BERRY_JUICE.get(), AgresticFluids.FLOWING_SWEET_BERRY_JUICE.get());
        addToTag(AgresticTags.IRONBERRY_JUICE, AgresticFluids.IRONBERRY_JUICE.get(), AgresticFluids.FLOWING_IRONBERRY_JUICE.get());
        addToTag(AgresticTags.ALE_WORT, AgresticFluids.ALE_WORT.get(), AgresticFluids.FLOWING_ALE_WORT.get());
        addToTag(AgresticTags.OLIVE_OIL, AgresticFluids.OLIVE_OIL.get(), AgresticFluids.FLOWING_OLIVE_OIL.get());
        addToTag(AgresticTags.VANTA_OIL, AgresticFluids.VANTA_OIL.get(), AgresticFluids.FLOWING_VANTA_OIL.get());

        addToTag(AgresticTags.ALE, AgresticFluids.ALE.get(), AgresticFluids.FLOWING_ALE.get());
        addToTag(AgresticTags.CIDER, AgresticFluids.CIDER.get(), AgresticFluids.FLOWING_CIDER.get());
        addToTag(AgresticTags.IRON_WINE, AgresticFluids.IRON_WINE.get(), AgresticFluids.FLOWING_IRON_WINE.get());
        addToTag(AgresticTags.MEAD, AgresticFluids.MEAD.get(), AgresticFluids.FLOWING_MEAD.get());
        addToTag(AgresticTags.SWEET_BERRY_WINE, AgresticFluids.SWEET_BERRY_WINE.get(), AgresticFluids.FLOWING_SWEET_BERRY_WINE.get());
        addToTag(AgresticTags.WINE, AgresticFluids.WINE.get(), AgresticFluids.FLOWING_WINE.get());
        addToTag(AgresticTags.AMBROSIA, AgresticFluids.AMBROSIA.get(), AgresticFluids.FLOWING_AMBROSIA.get());

    }

    protected final void addToTag(TagKey<Fluid> tagKey, Fluid... fluids) {
        var tag = this.tag(tagKey);
        List.of(fluids).forEach(tag::add);
    }
}
