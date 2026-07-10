package io.github.pouffy.rustic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalLanguageProvider;
import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.rustic.Rustic;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends KrystalLanguageProvider {

    public ModLanguageProvider(PackOutput output, KrystalSoundsProvider soundsProvider) {
        super(output, Rustic.MODID, "en_us", soundsProvider);
    }

    @Override
    protected void extraTranslations() {
        add("itemGroup.rustic.main", "Rustic");
        for(BlockDefinition<?> definition : Rustic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
            String customLang = definition.customLang();
            if (customLang == null) continue;
            if (!customLang.isEmpty()) {
                this.add(definition.langKey(), customLang);
            } else {
                this.add(definition.langKey(), definition.langName());
            }
        }

        for(ItemDefinition<?> definition : Rustic.INSTANCE.itemRegistryHelper.ITEM_DEFINITIONS) {
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

        add("emi.category.rustic.tub_crushing", "Tub Crushing");
    }
}
