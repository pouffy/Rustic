package io.github.pouffy.agrestic.datagen.server.tags;

import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Agrestic.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(BlockDefinition<?> definition : Agrestic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
            if (definition.hasItem()) {
                if (definition.get() instanceof FlowerBlock) {
                    this.tag(ItemTags.SMALL_FLOWERS).add(definition.item());
                }

                if (definition.get() instanceof WallBlock) {
                    this.tag(ItemTags.WALLS).add(definition.item());
                }

                if (definition.get() instanceof SaplingBlock) {
                    this.tag(ItemTags.SAPLINGS).add(definition.item());
                }

                if (definition.get() instanceof LeavesBlock) {
                    this.tag(ItemTags.LEAVES).add(definition.item());
                }

                if (definition.get() instanceof SlabBlock) {
                    this.tag(ItemTags.SLABS).add(definition.item());
                }
            }
        }

        for(ItemDefinition<?> definition : Agrestic.INSTANCE.itemRegistryHelper.ITEM_DEFINITIONS) {
            if (definition.hasItem() && definition.get() instanceof BoatItem) {
                this.tag(ItemTags.BOATS).add(definition.item());
            }
        }

        for(AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            this.tag(ItemTags.SIGNS).add(woodset.sign().item());
            this.tag(ItemTags.HANGING_SIGNS).add(woodset.hangingSign().item());
            this.tag(ItemTags.PLANKS).add(woodset.planks().item());
            this.tag(Tags.Items.FENCE_GATES_WOODEN).add(woodset.fenceGate().item());
            this.tag(Tags.Items.FENCES_WOODEN).add(woodset.fence().item());
            this.tag(ItemTags.WOODEN_FENCES).add(woodset.fence().item());
            this.tag(ItemTags.WOODEN_STAIRS).add(woodset.stairs().item());
            this.tag(ItemTags.WOODEN_BUTTONS).add(woodset.button().item());
            this.tag(ItemTags.WOODEN_DOORS).add(woodset.door().item());
            this.tag(ItemTags.WOODEN_SLABS).add(woodset.slab().item());
            this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(woodset.pressurePlate().item());
            this.tag(ItemTags.WOODEN_TRAPDOORS).add(woodset.trapdoor().item());
            this.tag(ItemTags.LOGS).add(woodset.log().item());
            this.tag(Tags.Items.STRIPPED_LOGS).add(woodset.strippedLog().item());
            this.tag(Tags.Items.STRIPPED_WOODS).add(woodset.strippedWood().item());
        }
        this.tag(AgresticTags.OLIVE_LOGS.itemTag()).add(AgresticBlocks.OLIVE.log().asItem(), AgresticBlocks.OLIVE.wood().asItem(), AgresticBlocks.OLIVE.strippedLog().asItem(), AgresticBlocks.OLIVE.strippedWood().asItem());
        this.tag(AgresticTags.IRONWOOD_LOGS.itemTag()).add(AgresticBlocks.IRONWOOD.log().asItem(), AgresticBlocks.IRONWOOD.wood().asItem(), AgresticBlocks.IRONWOOD.strippedLog().asItem(), AgresticBlocks.IRONWOOD.strippedWood().asItem());
        this.tag(ItemTags.LOGS_THAT_BURN).addTags(AgresticTags.OLIVE_LOGS.itemTag(), AgresticTags.IRONWOOD_LOGS.itemTag());
    }

    @SafeVarargs
    protected final void addToTags(Item item, TagKey<Item>... itemTags) {
        List.of(itemTags).forEach((itemTag) -> this.tag(itemTag).add(item));
    }

    @SafeVarargs
    protected final void addToTags(TagKey<Item> item, TagKey<Item>... itemTags) {
        List.of(itemTags).forEach((itemTag) -> this.tag(itemTag).addTag(item));
    }
}
