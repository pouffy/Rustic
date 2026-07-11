package io.github.pouffy.agrestic;

import com.pouffydev.krystal_core.foundation.registry.CreativeTabRegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AgresticCreativeTab {
    private static final CreativeTabRegistryHelper HELPER = Agrestic.getRegistryHelper().getCreativeTabHelper();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = HELPER.registerTabSearchBar("main", Items.DIRT.builtInRegistryHolder(), (params, output) -> {
                for (var definition : Agrestic.INSTANCE.itemRegistryHelper.ITEM_DEFINITIONS) {
                    if (definition.get() instanceof BlockItem) continue;
                    output.accept(definition.get());
                }
                for (var definition : Agrestic.INSTANCE.blockRegistryHelper.BLOCK_DEFINITIONS) {
                    if (!definition.hasItem()) continue;
                    output.accept(definition.get());
                }
            },
            builder -> RegistryHelper.noAction());

    public static void staticInit() {

    }
}
