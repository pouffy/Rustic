package io.github.pouffy.agrestic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalLanguageProvider;
import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLanguageProvider extends KrystalLanguageProvider {

    public ModLanguageProvider(PackOutput output, KrystalSoundsProvider soundsProvider) {
        super(output, Agrestic.MODID, "en_us", soundsProvider);
    }

    @Override
    protected void extraTranslations() {
        add("itemGroup.agrestic.agriculture", "Agrestic Agriculture");
        add("itemGroup.agrestic.decoration", "Agrestic Decorations");
        add("itemGroup.agrestic.alchemy", "Agrestic Alchemy");
        for(BlockDefinition<?> definition : AgresticBlocks.BLOCK_DEFINITIONS) {
            String customLang = definition.customLang();
            if (customLang == null) continue;
            if (!customLang.isEmpty()) {
                this.add(definition.langKey(), customLang);
            } else {
                this.add(definition.langKey(), definition.langName());
            }
        }

        for(ItemDefinition<?> definition : AgresticItems.ITEM_DEFINITIONS) {
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
        add("recipe.agrestic.chance", "Chance: %s");
        add("recipe.agrestic.time", "Time: %s");
        add("block.agrestic.tank.drained", "Drained %s mb %s");
        add("block.agrestic.tank.filled", "Filled %s mb %s");

        add("ui.agrestic.tooltip.fluid", "%s %smb / %smb");
        add("ui.agrestic.tooltip.item", "%s x%s");

        NeoForgeRegistries.FLUID_TYPES.registryKeySet().stream().filter((key) -> key.location().getNamespace().equals(Agrestic.MODID)).forEach((key) -> add("fluid_type.agrestic.%s".formatted(key.location().getPath()), transform(key.location())));
    }
}
