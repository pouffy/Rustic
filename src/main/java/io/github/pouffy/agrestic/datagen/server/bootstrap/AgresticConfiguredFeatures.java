package io.github.pouffy.agrestic.datagen.server.bootstrap;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class AgresticConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> IRONWOOD_TREE = makeKey("ironwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OLIVE_TREE = makeKey("olive_tree");

    private static ResourceKey<ConfiguredFeature<?, ?>> makeKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Agrestic.location(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Ironwood Tree
        register(context, IRONWOOD_TREE, Feature.TREE, createStraightBlobTree(AgresticBlocks.IRONWOOD.log().block(), AgresticBlocks.IRONWOOD.leaves().block(), 6, 7, 0, 2).ignoreVines().build());

        // Olive Tree
        register(context, OLIVE_TREE, Feature.TREE, createStraightBlobTree(AgresticBlocks.OLIVE.log().block(), AgresticBlocks.OLIVE.leaves().block(), 4, 3, 0, 2).ignoreVines().build());

    }

    private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(
            Block logBlock, Block leavesBlock, int baseHeight, int heightRandA, int heightRandB, int radius
    ) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(logBlock),
                new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
                BlockStateProvider.simple(leavesBlock),
                new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }


}
