package io.github.pouffy.agrestic.datagen.server.recipe;

import com.pouffydev.krystal_core.foundation.data.provider.server.KrysOutput;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.data.AbstractEvaporationBoosterProvider;
import io.github.pouffy.agrestic.common.data.EvaporationBooster;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class AgresticEvaporationBoosters extends AbstractEvaporationBoosterProvider {
    public AgresticEvaporationBoosters(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, Agrestic.MODID);
    }

    @Override
    protected void addBoosters(KrysOutput<EvaporationBooster> output, HolderLookup.Provider holderLookup) {
        addBooster(output, "campfire", EvaporationBooster.builder(2.0f, BlockTags.CAMPFIRES).conditions(b -> b._true("lit")).build());
        addBooster(output, "fire", EvaporationBooster.builder(2.0f, BlockTags.FIRE).conditions(b -> b._false("east")._false("north")._false("south")._false("up")._false("west")).build());
        addBooster(output, "magma_block", EvaporationBooster.builder(1.2f, Blocks.MAGMA_BLOCK).build());
        addBooster(output, "lava", EvaporationBooster.builder(2.5f, Blocks.LAVA, Blocks.LAVA_CAULDRON).build());
    }
}
