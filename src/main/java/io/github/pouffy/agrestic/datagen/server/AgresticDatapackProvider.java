package io.github.pouffy.agrestic.datagen.server;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticBiomeModifiers;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticConfiguredFeatures;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticDamageTypes;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AgresticDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, AgresticDamageTypes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, AgresticConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, AgresticPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, AgresticBiomeModifiers::new)
            ;

    public AgresticDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookProvider) {
        super(output, lookProvider, BUILDER, Set.of(Agrestic.MODID));
    }
}
