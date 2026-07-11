package io.github.pouffy.agrestic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalLanguageProvider;
import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends KrystalLanguageProvider {

    public ModLanguageProvider(PackOutput output, KrystalSoundsProvider soundsProvider) {
        super(output, Agrestic.MODID, "en_us", soundsProvider);
    }

    @Override
    protected void extraTranslations() {
        add("itemGroup.agrestic.main", "Agrestic");
        for(BlockDefinition<?> definition : Agrestic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
            String customLang = definition.customLang();
            if (customLang == null) continue;
            if (!customLang.isEmpty()) {
                this.add(definition.langKey(), customLang);
            } else {
                this.add(definition.langKey(), definition.langName());
            }
        }

        for(ItemDefinition<?> definition : Agrestic.INSTANCE.itemRegistryHelper.ITEM_DEFINITIONS) {
            if (!definition.isBlockItem()) {
                String customLang = definition.customLang();
                if (customLang == null) continue;
                if (!customLang.isEmpty()) {
                    this.add(definition.langKey(), customLang);
                } else {
                    this.add(definition.langKey(), definition.langName());
                }
            }
        }

        add("emi.category.agrestic.tub_crushing", "Tub Crushing");
        add("block.agrestic.tank.drained", "Drained %s mb %s");
        add("block.agrestic.tank.filled", "Filled %s mb %s");
    }
}
