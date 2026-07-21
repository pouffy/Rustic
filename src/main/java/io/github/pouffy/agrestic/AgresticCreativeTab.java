package io.github.pouffy.agrestic;

import com.pouffydev.krystal_core.foundation.registry.CreativeTabRegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import com.pouffydev.krystal_core.foundation.utility.CreativeTabManager;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticFluids;
import io.github.pouffy.agrestic.init.AgresticItems;
import lombok.Setter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@EventBusSubscriber
public class AgresticCreativeTab {
    private static final CreativeTabRegistryHelper HELPER = Agrestic.getRegistryHelper().getCreativeTabHelper();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AGRICULTURE = registerTab("agriculture", () -> AgresticItems.OLIVES, (params, output) -> {}, RegistryHelper.noAction());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DECORATION = registerTab("decoration", AgresticBlocks.OLIVE::planks, (params, output) -> {}, RegistryHelper.noAction());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ALCHEMY = registerTab("alchemy", () -> AgresticBlocks.ALOE_VERA, (params, output) -> {}, RegistryHelper.noAction());

    @Setter
    private static ResourceKey<CreativeModeTab> currentTab = AGRICULTURE.getKey();

    public static void staticInit() {}

    public static void populate() {
        setCurrentTab(AGRICULTURE.getKey());
        ///add(AgresticItems.CATALOG);
        ///add(AgresticBlocks.FERTILE_SOIL);
        for (AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            add(woodset.log());
            add(woodset.wood());
            add(woodset.strippedLog());
            add(woodset.strippedWood());
        }
        add(AgresticBlocks.OLIVE.leaves());
        add(AgresticBlocks.IRONWOOD.leaves());
        add(AgresticBlocks.APPLE_LEAVES);
        add(AgresticBlocks.APPLE_SAPLING);
        add(AgresticBlocks.OLIVE_SAPLING);
        add(AgresticBlocks.IRONWOOD_SAPLING);
        add(AgresticBlocks.CRUSHING_TUB);
        add(AgresticBlocks.EVAPORATING_BASIN);
        add(AgresticBlocks.FLUID_BARREL);
        ///add(AgresticBlocks.UNFIRED_DRYING_BASIN);
        ///add(AgresticBlocks.STAKE);
        ///add(AgresticBlocks.ROPE);
        //Seeds
        add(AgresticItems.CHILLI_PEPPER_SEEDS);
        ///add(AgresticItems.TOMATO_SEEDS);
        ///add(AgresticItems.GRAPE_SEEDS);
        add(AgresticBlocks.APPLE_SEEDS);
        //Crops
        add(AgresticItems.OLIVES);
        add(AgresticItems.IRONBERRIES);
        add(AgresticItems.GRAPES);
        add(AgresticItems.TOMATO);
        add(AgresticItems.CHILLI_PEPPER);
        add(AgresticItems.GHOST_PEPPER);
        //Ingredients
        add(AgresticItems.TALLOW);
        add(AgresticItems.GOLD_DUST);
        add(AgresticItems.TINY_GOLD_DUST);
        add(AgresticItems.IRON_DUST);
        add(AgresticItems.TINY_IRON_DUST);
        ///add(AgresticItems.TINY_GLOWSTONE_DUST);
        //Drinks
        add(AgresticItems.IRONBERRY_JUICE_BOTTLE);
        add(AgresticItems.VANTA_OIL_BOTTLE);
        add(AgresticItems.ALE_WORT_BOTTLE);
        add(AgresticItems.OLIVE_OIL_BOTTLE);
        add(AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE);
        add(AgresticItems.APPLE_JUICE_BOTTLE);
        add(AgresticItems.SWEET_BERRY_JUICE_BOTTLE);
        add(AgresticItems.GRAPE_JUICE_BOTTLE);
        //Buckets
        add(AgresticFluids.IRONBERRY_JUICE_BUCKET);
        add(AgresticFluids.VANTA_OIL_BUCKET);
        add(AgresticFluids.ALE_WORT_BUCKET);
        add(AgresticFluids.OLIVE_OIL_BUCKET);
        add(AgresticFluids.GOLDEN_APPLE_JUICE_BUCKET);
        add(AgresticFluids.APPLE_JUICE_BUCKET);
        add(AgresticFluids.SWEET_BERRY_JUICE_BUCKET);
        add(AgresticFluids.GRAPE_JUICE_BUCKET);
        add(AgresticBlocks.BREWING_BARREL);
        setCurrentTab(DECORATION.getKey());
        for (AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            add(woodset.planks());
            add(woodset.stairs());
            add(woodset.slab());
            add(woodset.fence());
            add(woodset.fenceGate());
            add(woodset.door());
            add(woodset.trapdoor());
            add(woodset.pressurePlate());
            add(woodset.button());
        }
        startChain(AgresticBlocks.OLIVE.button());
        toChain(AgresticItems.OLIVE_SIGN);
        toChain(AgresticItems.OLIVE_HANGING_SIGN);
        toChain(AgresticItems.OLIVE_BOAT);
        toChain(AgresticItems.OLIVE_CHEST_BOAT);
        endChain();
        startChain(AgresticBlocks.IRONWOOD.button());
        toChain(AgresticItems.IRONWOOD_SIGN);
        toChain(AgresticItems.IRONWOOD_HANGING_SIGN);
        toChain(AgresticItems.IRONWOOD_BOAT);
        toChain(AgresticItems.IRONWOOD_CHEST_BOAT);
        endChain();
        setCurrentTab(ALCHEMY.getKey());
        add(AgresticBlocks.ALOE_VERA);
        add(AgresticBlocks.BLOOD_ORCHID);
        add(AgresticBlocks.CHAMOMILE);
        add(AgresticItems.CLOUDSBLUFF);
        add(AgresticBlocks.COHOSH);
        add(AgresticBlocks.HORSETAIL);
        add(AgresticBlocks.VANTA_LILY);
        add(AgresticBlocks.WIND_THISTLE);
        add(AgresticItems.CORE_ROOT);
        add(AgresticItems.GINSENG);
        add(AgresticItems.MARSH_MALLOW);
        add(AgresticBlocks.MOONCAP);
        add(AgresticBlocks.DEATHSTALK);
        ///add(AgresticBlocks.BASIC_CONDENSER);
        ///add(AgresticBlocks.BASIC_RETORT);
        ///add(AgresticBlocks.ADVANCED_CONDENSER);
        ///add(AgresticBlocks.ADVANCED_RETORT);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void populateExtra(BuildCreativeModeTabContentsEvent event) {
        generateBooze(event, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }

    private static void generateBooze(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
        Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
        var ironWine = new ItemStack(AgresticItems.IRON_WINE_BOTTLE.asItem());
        ironWine.set(AgresticDataComponents.QUALITY, 0.75f);
        var ale = new ItemStack(AgresticItems.ALE_BOTTLE.asItem());
        ale.set(AgresticDataComponents.QUALITY, 0.75f);
        var cider = new ItemStack(AgresticItems.CIDER_BOTTLE.asItem());
        cider.set(AgresticDataComponents.QUALITY, 0.75f);
        var mead = new ItemStack(AgresticItems.MEAD_BOTTLE.asItem());
        mead.set(AgresticDataComponents.QUALITY, 0.75f);
        var ambrosia = new ItemStack(AgresticItems.AMBROSIA_BOTTLE.asItem());
        ambrosia.set(AgresticDataComponents.QUALITY, 0.75f);
        var sweetBerryWine = new ItemStack(AgresticItems.SWEET_BERRY_WINE_BOTTLE.asItem());
        sweetBerryWine.set(AgresticDataComponents.QUALITY, 0.75f);
        var wine = new ItemStack(AgresticItems.WINE_BOTTLE.asItem());
        wine.set(AgresticDataComponents.QUALITY, 0.75f);
        set.add(ironWine);
        set.add(ale);
        set.add(cider);
        set.add(mead);
        set.add(ambrosia);
        set.add(sweetBerryWine);
        set.add(wine);
        output.acceptAll(set, tabVisibility);
    }

    private static void add(ItemLike itemLike) {
        CreativeTabManager.addToTab(currentTab, itemLike);
    }

    private static void before(ItemLike itemLike, ItemLike target) {
        CreativeTabManager.addNextToItem(currentTab, itemLike, target, true);
    }

    private static void after(ItemLike itemLike, ItemLike target) {
        CreativeTabManager.addNextToItem(currentTab, itemLike, target, false);
    }

    private static void startChain(ItemLike parent) {
        CreativeTabManager.startChain(currentTab, false, false, parent);
    }

    private static void toChain(ItemLike itemLike) {
        CreativeTabManager.queryChain().addToChain(itemLike);
    }

    private static void endChain() {
        CreativeTabManager.endChain();
    }

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, Supplier<ItemLike> icon, BiConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output> displayItems, Consumer<CreativeModeTab.Builder> additionalProperties) {
        return HELPER.TABS.register(name, (id) -> {
            CreativeModeTab.Builder builder = CreativeModeTab.builder();
            CreativeModeTab.Builder var10000 = builder.title(Component.translatable(id.toLanguageKey("itemGroup"))).icon(() -> new ItemStack(icon.get()));
            Objects.requireNonNull(displayItems);
            var10000.displayItems(displayItems::accept);
            additionalProperties.accept(builder);
            return builder.build();
        });
    }
}
