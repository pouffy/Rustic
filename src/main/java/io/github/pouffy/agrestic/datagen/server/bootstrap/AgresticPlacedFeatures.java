package io.github.pouffy.agrestic.datagen.server.bootstrap;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class AgresticPlacedFeatures {
    public static final ResourceKey<PlacedFeature> IRONWOOD_TREE = makeKey("ironwood_tree");
    public static final ResourceKey<PlacedFeature> OLIVE_TREE = makeKey("olive_tree");

    private static ResourceKey<PlacedFeature> makeKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Agrestic.location(name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> featureLookup = context.lookup(Registries.CONFIGURED_FEATURE);

        // Ironwood
        register(context, IRONWOOD_TREE, featureLookup.getOrThrow(AgresticConfiguredFeatures.IRONWOOD_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), AgresticBlocks.IRONWOOD_SAPLING.block()));

        // Olive
        register(context, OLIVE_TREE, featureLookup.getOrThrow(AgresticConfiguredFeatures.OLIVE_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), AgresticBlocks.OLIVE_SAPLING.block()));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
