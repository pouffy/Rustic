package io.github.pouffy.agrestic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalLanguageProvider;
import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalSoundsProvider;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticEffects;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemNameBlockItem;
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
            if (!definition.isBlockItem() || definition.get() instanceof ItemNameBlockItem) {
                String customLang = definition.customLang();
                if (customLang == null) continue;
                if (!customLang.isEmpty()) {
                    this.add(definition.langKey(), customLang);
                } else {
                    this.add(definition.langKey(), definition.langName());
                }
            }
        }

        AgresticEffects.HELPER.getEntries().forEach((effect) -> this.add(effect.get().getDescriptionId(), transform(effect.getId())));

        add("emi.category.agrestic.tub_crushing", "Tub Crushing");
        add("emi.category.agrestic.basin_evaporating", "Basin Evaporating");
        add("emi.category.agrestic.brewing", "Booze Brewing");
        add("emi.category.agrestic.fluid_transfer", "Fluid Transfer");
        add("recipe.agrestic.chance", "Chance: %s");
        add("recipe.agrestic.time", "Time: %s");
        add("recipe.agrestic.brewing.optional", "Starter culture fluid is optional");
        add("recipe.agrestic.brewing.quality_change", "Quality can change between %s & %s with starter culture");
        add("block.agrestic.tank.drained", "Drained %s mb %s");
        add("block.agrestic.tank.filled", "Filled %s mb %s");

        add("ui.agrestic.tooltip.fluid", "%s %smb / %smb");
        add("ui.agrestic.tooltip.item", "%s x%s");

        add("death.attack.bad_ambrosia", "%1$s tasted tainted divinity");

        NeoForgeRegistries.FLUID_TYPES.registryKeySet().stream().filter((key) -> key.location().getNamespace().equals(Agrestic.MODID)).forEach((key) -> add("fluid_type.agrestic.%s".formatted(key.location().getPath()), transform(key.location())));

        config("title", "Agrestic");
        config("all.general", "General");
        config("all.general.button", "General");
        config("all.general.tooltip", "General settings");

        config("section.agrestic.client.toml", "Agrestic Client Configuration");
        config("foodEffectTooltips", "Food Effect Tooltips");
        config("foodEffectTooltips.tooltip", "whether to show tooltips for food effects on Agrestic foods");

        config("section.agrestic.server.toml", "Agrestic Server Configuration");
        config("minQualityChange", "Minimum Change To Brew Quality");
        config("minQualityChange.tooltip", "the minimum amount of increase that booze culture will provide to the new brew, in percent");
        config("maxQualityChange", "Maximum Change To Brew Quality");
        config("maxQualityChange.tooltip", "the maximum amount of increase that booze culture will provide to the new brew, in percent");

        config("section.agrestic.common.toml", "Agrestic Common Configuration");

        quality("highest.0", "Heavenly");
        quality("highest.1", "Exceptional");
        quality("highest.2", "Improbably Delicious");
        quality("highest.3", "Divine");
        quality("highest.4", "Ambrosial");
        quality("highest.5", "Tantalizing");
        quality("highest.6", "Sensual");
        quality("highest.7", "God-Tier");

        quality("high.0", "Tasty");
        quality("high.1", "Quite Good");
        quality("high.2", "Enjoyable");
        quality("high.3", "Agreeable");
        quality("high.4", "Splendid");
        quality("high.5", "Delightful");
        quality("high.6", "Delectable");
        quality("high.7", "Yummy");

        quality("highish.0", "Acceptable");
        quality("highish.1", "Fair");
        quality("highish.2", "Drinkable");
        quality("highish.3", "Moderate");
        quality("highish.4", "Inoffensive");
        quality("highish.5", "Tolerable");
        quality("highish.6", "Palatable");
        quality("highish.7", "Passable");

        quality("lowish.0", "Dubious");
        quality("lowish.1", "Bad");
        quality("lowish.2", "Not Quite Drinkable");
        quality("lowish.3", "Questionable");
        quality("lowish.4", "Unpleasant");
        quality("lowish.5", "Dodgy");
        quality("lowish.6", "Disagreeable");
        quality("lowish.7", "Suspicious");

        quality("low.0", "Disgusting");
        quality("low.1", "Really Quite Bad");
        quality("low.2", "Repugnant");
        quality("low.3", "Nauseating");
        quality("low.4", "Sickening");
        quality("low.5", "Foul");
        quality("low.6", "Repellent");
        quality("low.7", "Gross");

        quality("lowest.0", "Atrocious");
        quality("lowest.1", "Offensively Bad");
        quality("lowest.2", "Undrinkable");
        quality("lowest.3", "Vile");
        quality("lowest.4", "Revolting");
        quality("lowest.5", "Noxious");
        quality("lowest.6", "Rancid");
        quality("lowest.7", "Wants You Gone");
    }

    public void config(String key, String value) {
        add("agrestic.configuration." + key, value);
    }

    public void quality(String key, String value) {
        add("ui.agrestic.tooltip.quality." + key, value);
    }
}
