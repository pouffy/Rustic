package io.github.pouffy.agrestic.datagen.server.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class AgresticRecipeCollector {
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final ExistingFileHelper existingFileHelper;
    private final DataGenerator generator;
    private final PackOutput packOutput;

    public AgresticRecipeCollector(CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper, DataGenerator generator, PackOutput packOutput) {
        this.lookupProvider = lookupProvider;
        this.existingFileHelper = existingFileHelper;
        this.generator = generator;
        this.packOutput = packOutput;
    }

    public void add(boolean server) {
        generator.addProvider(server, new ModCrushingRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(server, new AgresticFluidTransferProvider(packOutput, lookupProvider));
    }
}
