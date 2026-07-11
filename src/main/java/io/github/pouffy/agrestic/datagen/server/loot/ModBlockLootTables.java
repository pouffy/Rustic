package io.github.pouffy.agrestic.datagen.server.loot;

import com.pouffydev.krystal_core.foundation.data.loot.*;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.block.DoorBlockLootType;
import io.github.pouffy.agrestic.core.block.SlabBlockLootType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModBlockLootTables extends BlockLootSubProvider {
    private final Set<Block> generatedLootTables = new HashSet<>();

    public ModBlockLootTables(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        for(BlockDefinition<?> definition : Agrestic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
            Block block = (Block)definition.get();
            BlockLootType lootType = definition.lootType();
            if (lootType instanceof SelfBlockLootType) {
                this.dropSelf(block);
            } else if (lootType instanceof OtherBlockLootType otherBlockLootType) {
                this.add(block, this.createSingleItemTable(otherBlockLootType.getBlock()));
            } else if (lootType instanceof ShearsBlockLootType) {
                this.add(block, createShearsOnlyDrop(block));
            } else if (lootType instanceof OtherShearsBlockLootType otherShearsBlockLootType) {
                this.add(block, createShearsOnlyDrop(otherShearsBlockLootType.getBlock()));
            } else if (lootType instanceof SlabBlockLootType) {
                this.add(block, this.createSlabItemTable(block));
            } else if (lootType instanceof DoorBlockLootType) {
                this.add(block, this.createDoorTable(block));
            }
        }
    }

    protected void dropNamedContainer(Block block) {
        add(block, this::createNameableBlockEntityTable);
    }

    protected void dropSelf(Supplier<Block>... block) {
        for (Supplier<Block> b : block) {
            dropSelf(b.get());
        }
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    protected void otherWhenSilkTouch(Block pBlock, Block pOther) {
        this.add(pBlock, createSilkTouchOnlyTable(pOther));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }
}
