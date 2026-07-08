package io.github.pouffy.examplemod;

import com.pouffydev.krystal_core.foundation.registry.CreativeTabRegistryHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ExampleCreativeTab {
    private static final CreativeTabRegistryHelper HELPER = Example.getRegistryHelper().getCreativeTabHelper();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = HELPER.registerTabSearchBar("main", Items.DIRT.builtInRegistryHolder(), (params, out) -> {},
            builder -> builder.withTabsBefore(CreativeModeTabs.COMBAT));

    public static void staticInit() {

    }
}
