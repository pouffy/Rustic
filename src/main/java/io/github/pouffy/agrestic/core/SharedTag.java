package io.github.pouffy.agrestic.core;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SharedTag {
    private final TagKey<Block> blockTag;
    private final TagKey<Item> itemTag;

    public SharedTag(String name) {
        this(Agrestic.MODID, name);
    }

    public SharedTag(String modId, String name) {
        this.blockTag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, name));
        this.itemTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, name));
    }

    public SharedTag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
        this.blockTag = blockTag;
        this.itemTag = itemTag;
    }

    public TagKey<Item> itemTag() {
        return this.itemTag;
    }

    public TagKey<Block> blockTag() {
        return this.blockTag;
    }
}
