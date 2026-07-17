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
        addToTag(AgresticTags.APPLE_JUICE, AgresticFluids.APPLE_JUICE, AgresticFluids.FLOWING_APPLE_JUICE);
        addToTag(AgresticTags.GOLDEN_APPLE_JUICE, AgresticFluids.GOLDEN_APPLE_JUICE, AgresticFluids.FLOWING_GOLDEN_APPLE_JUICE);
        addToTag(AgresticTags.GRAPE_JUICE, AgresticFluids.GRAPE_JUICE, AgresticFluids.FLOWING_GRAPE_JUICE);
        addToTag(AgresticTags.SWEET_BERRY_JUICE, AgresticFluids.SWEET_BERRY_JUICE, AgresticFluids.FLOWING_SWEET_BERRY_JUICE);
        addToTag(AgresticTags.IRONBERRY_JUICE, AgresticFluids.IRONBERRY_JUICE, AgresticFluids.FLOWING_IRONBERRY_JUICE);
        addToTag(AgresticTags.ALE_WORT, AgresticFluids.ALE_WORT, AgresticFluids.FLOWING_ALE_WORT);
        addToTag(AgresticTags.OLIVE_OIL, AgresticFluids.OLIVE_OIL, AgresticFluids.FLOWING_OLIVE_OIL);
        addToTag(AgresticTags.VANTA_OIL, AgresticFluids.VANTA_OIL, AgresticFluids.FLOWING_VANTA_OIL);
    }

    protected final void addToTag(TagKey<Fluid> tagKey, Fluid... fluids) {
        var tag = this.tag(tagKey);
        List.of(fluids).forEach(tag::add);
    }
}
