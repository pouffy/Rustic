package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.SharedTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class AgresticTags {
    public static final SharedTag OLIVE_LOGS = createSharedTag("olive_logs");
    public static final SharedTag IRONWOOD_LOGS = createSharedTag("ironwood_logs");
    public static final TagKey<Item> RENDER_UPRIGHT = createItemTag("render_upright");

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

        public FeatureAddition() {
        }
    }

    public static class FeatureRemoval {

        public FeatureRemoval() {
        }
    }
}
