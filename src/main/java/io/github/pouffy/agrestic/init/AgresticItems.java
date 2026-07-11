package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemRegistryHelper;
import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;

public class AgresticItems {
    public static final ItemRegistryHelper HELPER = Agrestic.INSTANCE.itemRegistryHelper;

    public static final ItemDefinition<SignItem> OLIVE_SIGN = HELPER.register("olive_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.OLIVE.sign().get(), AgresticBlocks.OLIVE.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> OLIVE_HANGING_SIGN = HELPER.register("olive_hanging_sign", () -> new HangingSignItem(AgresticBlocks.OLIVE.hangingSign().get(), AgresticBlocks.OLIVE.hangingWallSign().get(), new Item.Properties().stacksTo(16)));

    public static final ItemDefinition<SignItem> IRONWOOD_SIGN = HELPER.register("ironwood_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.IRONWOOD.sign().get(), AgresticBlocks.IRONWOOD.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> IRONWOOD_HANGING_SIGN = HELPER.register("ironwood_hanging_sign", () -> new HangingSignItem(AgresticBlocks.IRONWOOD.hangingSign().get(), AgresticBlocks.IRONWOOD.hangingWallSign().get(), new Item.Properties().stacksTo(16)));


    public static void staticInit() {}
}
