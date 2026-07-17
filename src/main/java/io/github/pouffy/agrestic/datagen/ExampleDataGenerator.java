package io.github.pouffy.agrestic.datagen;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.datagen.client.ModBlockStateProvider;
import io.github.pouffy.agrestic.datagen.client.ModItemModelProvider;
import io.github.pouffy.agrestic.datagen.client.ModLanguageProvider;
import io.github.pouffy.agrestic.datagen.client.ModSoundsProvider;
import io.github.pouffy.agrestic.datagen.server.AgresticDatapackProvider;
import io.github.pouffy.agrestic.datagen.server.ModAdvancementProvider;
import io.github.pouffy.agrestic.datagen.server.loot.ModLootGenerator;
import io.github.pouffy.agrestic.datagen.server.recipe.AgresticRecipeCollector;
import io.github.pouffy.agrestic.datagen.server.tags.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Agrestic.MODID)
public class ExampleDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        boolean server = event.includeServer();
        boolean client = event.includeClient();

        AgresticDatapackProvider datapackProvider = new AgresticDatapackProvider(packOutput, lookupProvider);
        lookupProvider = datapackProvider.getRegistryProvider();
        generator.addProvider(server, datapackProvider);

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        ModItemTagsProvider itemTags = new ModItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper);
        ModFluidTagsProvider fluidTags = new ModFluidTagsProvider(packOutput, lookupProvider, existingFileHelper);
        ModEntityTypeTagsProvider entityTypeTags = new ModEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper);
        ModBiomeTagsProvider biomeTags = new ModBiomeTagsProvider(packOutput, lookupProvider, existingFileHelper);

        AdvancementProvider advancements = new AdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of(new ModAdvancementProvider()));
        ModLootGenerator lootGenerator = new ModLootGenerator(packOutput, lookupProvider);
        AgresticRecipeCollector recipes = new AgresticRecipeCollector(lookupProvider, existingFileHelper, generator, packOutput);

        ModSoundsProvider sounds = new ModSoundsProvider(packOutput, existingFileHelper);
        ModLanguageProvider language = new ModLanguageProvider(packOutput, sounds);
        ModItemModelProvider itemModels = new ModItemModelProvider(packOutput, existingFileHelper);
        ModBlockStateProvider blockStates = new ModBlockStateProvider(packOutput, existingFileHelper);

        generator.addProvider(server, blockTags);
        generator.addProvider(server, itemTags);
        generator.addProvider(server, fluidTags);
        generator.addProvider(server, entityTypeTags);
        generator.addProvider(server, biomeTags);

        generator.addProvider(server, advancements);
        generator.addProvider(server, lootGenerator);
        recipes.add(server);

        generator.addProvider(client, sounds);
        generator.addProvider(client, itemModels);
        generator.addProvider(client, blockStates);
        generator.addProvider(client, language);
    }
}
