package io.github.pouffy.examplemod.datagen;

import com.pouffydev.krystal_core.datagen.client.KCSoundsProvider;
import io.github.pouffy.examplemod.Example;
import io.github.pouffy.examplemod.datagen.client.ModBlockStateProvider;
import io.github.pouffy.examplemod.datagen.client.ModItemModelProvider;
import io.github.pouffy.examplemod.datagen.client.ModLanguageProvider;
import io.github.pouffy.examplemod.datagen.client.ModSoundsProvider;
import io.github.pouffy.examplemod.datagen.server.ModAdvancementProvider;
import io.github.pouffy.examplemod.datagen.server.loot.ModLootGenerator;
import io.github.pouffy.examplemod.datagen.server.tags.ModBlockTagsProvider;
import io.github.pouffy.examplemod.datagen.server.tags.ModItemTagsProvider;
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

@EventBusSubscriber(modid = Example.MODID)
public class ExampleDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        boolean server = event.includeServer();
        boolean client = event.includeClient();

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        ModItemTagsProvider itemTags = new ModItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper);

        AdvancementProvider advancements = new AdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of(new ModAdvancementProvider()));
        ModLootGenerator lootGenerator = new ModLootGenerator(packOutput, lookupProvider);

        ModSoundsProvider sounds = new ModSoundsProvider(packOutput, existingFileHelper);
        ModLanguageProvider language = new ModLanguageProvider(packOutput, sounds);
        ModItemModelProvider itemModels = new ModItemModelProvider(packOutput, existingFileHelper);
        ModBlockStateProvider blockStates = new ModBlockStateProvider(packOutput, existingFileHelper);

        generator.addProvider(server, blockTags);
        generator.addProvider(server, itemTags);

        generator.addProvider(server, advancements);
        generator.addProvider(server, lootGenerator);

        generator.addProvider(client, sounds);
        generator.addProvider(client, itemModels);
        generator.addProvider(client, blockStates);
        generator.addProvider(client, language);
    }
}
