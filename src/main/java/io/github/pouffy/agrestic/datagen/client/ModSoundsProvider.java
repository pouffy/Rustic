package io.github.pouffy.agrestic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModSoundsProvider extends KrystalSoundsProvider {

    public ModSoundsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Agrestic.MODID, helper);
    }

    @Override
    public void registerSounds() {

    }
}
