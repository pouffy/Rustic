package io.github.pouffy.agrestic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalItemModelProvider;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends KrystalItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Agrestic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            String name = woodset.planks().getId().getPath().replace("_planks", "");
            this.basicItem(woodset.door()::asItem, "wooden/%s/door".formatted(name));
        }
        this.basicItem(AgresticItems.OLIVE_SIGN, "wooden/olive/sign");
        this.basicItem(AgresticItems.OLIVE_HANGING_SIGN, "wooden/olive/hanging_sign");
        this.basicItem(AgresticItems.IRONWOOD_SIGN, "wooden/ironwood/sign");
        this.basicItem(AgresticItems.IRONWOOD_HANGING_SIGN, "wooden/ironwood/hanging_sign");
    }
}
