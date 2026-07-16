package io.github.pouffy.agrestic.datagen.server.bootstrap;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.worldgen.FeatureWithStep;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgresticBiomeModifiers {
    private final BootstrapContext<BiomeModifier> bootstrap;
    private final Map<FeatureWithStep, List<Holder<Biome>>> featureToBiomes = new HashMap<>();

    public AgresticBiomeModifiers(BootstrapContext<BiomeModifier> bootstrap) {
        this.bootstrap = bootstrap;
        setup();
    }

    private void setup() {
        GenerationStep.Decoration topLayerModifications = GenerationStep.Decoration.TOP_LAYER_MODIFICATION;
        GenerationStep.Decoration vegetalDecoration = GenerationStep.Decoration.VEGETAL_DECORATION;
        GenerationStep.Decoration localModifications = GenerationStep.Decoration.LOCAL_MODIFICATIONS;

        addFeaturesToTag(AgresticTags.FeatureAddition.HAS_IRONWOOD_TREES, vegetalDecoration, AgresticPlacedFeatures.IRONWOOD_TREE);
        addFeaturesToTag(AgresticTags.FeatureAddition.HAS_OLIVE_TREES, vegetalDecoration, AgresticPlacedFeatures.OLIVE_TREE);

        processFeatures();
    }

    @SafeVarargs
    private void addFeaturesToTag(TagKey<Biome> biomeTag, GenerationStep.Decoration step, ResourceKey<PlacedFeature>... features) {
        List<Holder<PlacedFeature>> featureHolders = new ArrayList<>();
        List.of(features).forEach(featureKey ->
                featureHolders.add(bootstrap.lookup(Registries.PLACED_FEATURE).getOrThrow(featureKey)));
        String name = biomeTag.location().getPath() + "_tag_features";

        bootstrap.register(
                ResourceKey.create(
                        NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                        Agrestic.location("add_" + name)
                ),
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        bootstrap.lookup(Registries.BIOME).getOrThrow(biomeTag),
                        HolderSet.direct(featureHolders),
                        step
                )
        );
    }

    private void processFeatures() {
        featureToBiomes.forEach((feature, biomeSet) -> {
            HolderSet<Biome> mergedBiomes = HolderSet.direct(biomeSet);
            addFeatureToSet(feature, mergedBiomes);
        });
    }

    private void addFeatureToSet(FeatureWithStep feature, HolderSet<Biome> biomes) {
        Holder<PlacedFeature> placedFeature = bootstrap.lookup(Registries.PLACED_FEATURE).getOrThrow(feature.key());
        bootstrap.register(
                ResourceKey.create(
                        NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                        Agrestic.location("add_" + placedFeature.unwrapKey().orElseThrow().location().getPath())
                ),
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes,
                        HolderSet.direct(placedFeature),
                        feature.step()
                )
        );
    }
}
