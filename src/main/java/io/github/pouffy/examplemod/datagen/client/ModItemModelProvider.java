package io.github.pouffy.examplemod.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalItemModelProvider;
import io.github.pouffy.examplemod.Example;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends KrystalItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Example.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
