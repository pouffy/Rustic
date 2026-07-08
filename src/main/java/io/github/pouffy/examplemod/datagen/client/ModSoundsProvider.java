package io.github.pouffy.examplemod.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import io.github.pouffy.examplemod.Example;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModSoundsProvider extends KrystalSoundsProvider {

    public ModSoundsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Example.MODID, helper);
    }

    @Override
    public void registerSounds() {

    }
}
