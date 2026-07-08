package io.github.pouffy.examplemod.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalLanguageProvider;
import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import io.github.pouffy.examplemod.Example;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends KrystalLanguageProvider {

    public ModLanguageProvider(PackOutput output, KrystalSoundsProvider soundsProvider) {
        super(output, Example.MODID, "en_us", soundsProvider);
    }

    @Override
    protected void extraTranslations() {
        add("itemGroup.example", "Example Tab");
    }
}
