package io.github.pouffy.agrestic.datagen.server.tags;

import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Agrestic.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider provider) {
        for(BlockDefinition<?> definition : Agrestic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
            if (definition.get() instanceof FlowerBlock) {
                this.tag(BlockTags.SMALL_FLOWERS).add(definition.block());
            }

            if (definition.get() instanceof WallBlock) {
                this.tag(BlockTags.WALLS).add(definition.block());
            }

            if (definition.get() instanceof SaplingBlock) {
                this.tag(BlockTags.SAPLINGS).add(definition.block());
            }

            if (definition.get() instanceof LeavesBlock) {
                this.tag(BlockTags.LEAVES).add(definition.block());
            }

            if (definition.get() instanceof SlabBlock) {
                this.tag(BlockTags.SLABS).add(definition.block());
            }

            if (definition.get() instanceof StairBlock) {
                this.tag(BlockTags.STAIRS).add(definition.block());
            }
        }

        for(AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            this.tag(BlockTags.STANDING_SIGNS).add(woodset.sign().block());
            this.tag(BlockTags.WALL_SIGNS).add(woodset.wallSign().block());
            this.tag(BlockTags.CEILING_HANGING_SIGNS).add(woodset.hangingSign().block());
            this.tag(BlockTags.WALL_HANGING_SIGNS).add(woodset.hangingWallSign().block());
            this.tag(BlockTags.PLANKS).add(woodset.planks().block());
            this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(woodset.fenceGate().block());
            this.tag(BlockTags.FENCE_GATES).add(woodset.fenceGate().block());
            this.tag(Tags.Blocks.FENCES_WOODEN).add(woodset.fence().block());
            this.tag(BlockTags.WOODEN_FENCES).add(woodset.fence().block());
            this.tag(BlockTags.WOODEN_STAIRS).add(woodset.stairs().block());
            this.tag(BlockTags.WOODEN_BUTTONS).add(woodset.button().block());
            this.tag(BlockTags.WOODEN_DOORS).add(woodset.door().block());
            this.tag(BlockTags.WOODEN_SLABS).add(woodset.slab().block());
            this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(woodset.pressurePlate().block());
            this.tag(BlockTags.WOODEN_TRAPDOORS).add(woodset.trapdoor().block());
            this.tag(BlockTags.LOGS).add(woodset.log().block());
            this.tag(Tags.Blocks.STRIPPED_LOGS).add(woodset.strippedLog().block());
            this.tag(Tags.Blocks.STRIPPED_WOODS).add(woodset.strippedWood().block());
        }

        this.tag(AgresticTags.OLIVE_LOGS.blockTag()).add(AgresticBlocks.OLIVE.log().get(), AgresticBlocks.OLIVE.wood().get(), AgresticBlocks.OLIVE.strippedLog().get(), AgresticBlocks.OLIVE.strippedWood().get());
        this.tag(AgresticTags.IRONWOOD_LOGS.blockTag()).add(AgresticBlocks.IRONWOOD.log().get(), AgresticBlocks.IRONWOOD.wood().get(), AgresticBlocks.IRONWOOD.strippedLog().get(), AgresticBlocks.IRONWOOD.strippedWood().get());
        this.tag(BlockTags.LOGS_THAT_BURN).addTags(AgresticTags.OLIVE_LOGS.blockTag(), AgresticTags.IRONWOOD_LOGS.blockTag());
        this.tag(BlockTags.OVERWORLD_NATURAL_LOGS).addTags(AgresticTags.OLIVE_LOGS.blockTag(), AgresticTags.IRONWOOD_LOGS.blockTag());
    }

    @SafeVarargs
    protected final void addToTags(Block block, TagKey<Block>... blockTags) {
        List.of(blockTags).forEach((blockTag) -> this.tag(blockTag).add(block));
    }

    @SafeVarargs
    protected final void addToTags(TagKey<Block> block, TagKey<Block>... blockTags) {
        List.of(blockTags).forEach((blockTag) -> this.tag(blockTag).addTag(block));
    }
}
