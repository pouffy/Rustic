package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.item.DrinkableItem;
import io.github.pouffy.agrestic.core.SharedTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public class AgresticTags {
    public static final SharedTag OLIVE_LOGS = createSharedTag("olive_logs");
    public static final SharedTag IRONWOOD_LOGS = createSharedTag("ironwood_logs");
    public static final TagKey<Item> RENDER_UPRIGHT = createItemTag("render_upright");
    public static final SharedTag FERTILE_SOILS = createSharedTag("fertile_soils");

    public static final TagKey<Block> HERBS_CAN_SURVIVE_ON = createBlockTag("herbs_can_survive_on");
    public static final TagKey<Block> EVAPORATION_BOOSTERS = createBlockTag("evaporation_boosters");

    public static final TagKey<Fluid> APPLE_JUICE = createFluidTag("apple_juice");
    public static final TagKey<Fluid> GOLDEN_APPLE_JUICE = createFluidTag("golden_apple_juice");
    public static final TagKey<Fluid> GRAPE_JUICE = createFluidTag("grape_juice");
    public static final TagKey<Fluid> SWEET_BERRY_JUICE = createFluidTag("sweet_berry_juice");
    public static final TagKey<Fluid> IRONBERRY_JUICE = createFluidTag("ironberry_juice");
    public static final TagKey<Fluid> ALE_WORT = createFluidTag("ale_wort");
    public static final TagKey<Fluid> OLIVE_OIL = createFluidTag("olive_oil");
    public static final TagKey<Fluid> VANTA_OIL = createFluidTag("vanta_oil");

    public static final TagKey<EntityType<?>> DENSE_HERB_UNAFFECTED = createEntityTag("dense_herb_unaffected");
    public static final TagKey<EntityType<?>> PRICKLY_HERB_UNAFFECTED = createEntityTag("prickly_herb_unaffected");

    public AgresticTags() {
    }

    private static SharedTag createSharedTag(String name) {
        return new SharedTag(name);
    }

    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, Agrestic.location(name));
    }

    private static TagKey<Block> createBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, Agrestic.location(name));
    }

    private static TagKey<Fluid> createFluidTag(String name) {
        return TagKey.create(Registries.FLUID, Agrestic.location(name));
    }

    private static TagKey<Biome> createBiomeTag(String name) {
        return TagKey.create(Registries.BIOME, Agrestic.location(name));
    }

    private static TagKey<EntityType<?>> createEntityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Agrestic.location(name));
    }

    private static TagKey<DamageType> createDamageTypeTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Agrestic.location(name));
    }

    private static TagKey<Structure> createStructureTag(String name) {
        return TagKey.create(Registries.STRUCTURE, Agrestic.location(name));
    }

    public static class FeatureAddition {
        public static final TagKey<Biome> HAS_IRONWOOD_TREES = createBiomeTag("feature_addition/has_ironwood_trees");
        public static final TagKey<Biome> HAS_OLIVE_TREES = createBiomeTag("feature_addition/has_olive_trees");

        public FeatureAddition() {
        }
    }

    public static class FeatureRemoval {

        public FeatureRemoval() {
        }
    }
}
