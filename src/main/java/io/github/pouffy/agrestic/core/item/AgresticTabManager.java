package io.github.pouffy.agrestic.core.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.foundation.utility.CreativeTabManager;
import com.pouffydev.krystal_core.foundation.utility.Pair;
import lombok.Getter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AgresticTabManager {
    private static final Object MUTEX = new Object();
    private static final Map<ResourceKey<CreativeModeTab>, CreativeTabAdditions> additions = new HashMap();
    private static DaisyChain chain = null;
    private static final boolean ignChaining = false;

    public AgresticTabManager() {
    }

    public static synchronized void startChain(ResourceKey<CreativeModeTab> tab, boolean reversed, boolean behindParent, @Nullable ItemLike appendParent) {
        if (chain != null) {
            KrystalCore.LOGGER.error("CHAIN WAS STARTED WITHOUT FINISHING IT! DO NOT DO THIS!!! Ending chain now.");
            endChain();
        }

        chain = new DaisyChain(tab, reversed, behindParent, appendParent);
    }

    public static DaisyChain queryChain() {
        return chain;
    }

    public static synchronized void endChain() {
        for(ItemStack itemFromChain : chain.chainedItems) {
            CreativeTabAdditions tabAdditions = getForTab(chain.getTab());
            if (chain.appendParent == null) {
                tabAdditions.addAtEnd(itemFromChain);
            } else if (!chain.behindParent) {
                tabAdditions.addInFront(itemFromChain, chain.appendParent, false);
            } else {
                tabAdditions.addBehind(itemFromChain, chain.getParent(), false);
            }
        }

        chain = null;
    }

    public static void addToTab(ResourceKey<CreativeModeTab> tab, ItemStack item) {
        if (chain != null && chain.getTab().equals(tab)) {
            chain.addToChain(item);
        } else {
            getForTab(tab).addAtEnd(item);
        }

    }

    public static void addNextToItem(ResourceKey<CreativeModeTab> tab, ItemStack item, ItemLike target, boolean behind) {
        if (chain != null && chain.getTab().equals(tab)) {
            chain.addToChain(item);
        } else {
            CreativeTabAdditions tabAdditions = getForTab(tab);
            if (behind) {
                tabAdditions.addBehind(item, target, false);
            } else {
                tabAdditions.addInFront(item, target, false);
            }
        }

    }

    private static CreativeTabAdditions getForTab(ResourceKey<CreativeModeTab> tab) {
        return additions.computeIfAbsent(tab, (tabRk) -> new CreativeTabAdditions());
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        synchronized(MUTEX) {
            ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
            if (additions.containsKey(tabKey)) {
                CreativeTabAdditions tabAdditions = additions.get(tabKey);

                for(ItemStack item : tabAdditions.appendToEnd) {
                    acceptItem(event, item);
                }

                AtomicInteger firstTryIdiots = new AtomicInteger(0);
                AtomicInteger inOnSecond = new AtomicInteger(0);
                AtomicInteger failedBoth = new AtomicInteger(0);
                AtomicInteger doubleJeopardy = new AtomicInteger(0);
                AtomicInteger dailyDouble = new AtomicInteger(0);
                Multimap<ItemLike, Pair<Boolean, ItemStack>> additionsAtAllItems = LinkedHashMultimap.create();
                tabAdditions.appendInFront.forEach((parent, child) -> additionsAtAllItems.put(parent, Pair.of(false, child)));
                tabAdditions.appendBehind.forEach((parent, child) -> additionsAtAllItems.put(parent, Pair.of(true, child)));
                Multimap<ItemLike, Pair<Boolean, ItemStack>> round2 = LinkedHashMultimap.create();
                additionsAtAllItems.forEach((parent, childPair) -> {
                    if (!event.getParentEntries().contains(parent.asItem().getDefaultInstance())) {
                        round2.put(parent, childPair);
                    } else if (event.getParentEntries().contains(childPair.getSecond())) {
                        KrystalCore.LOGGER.debug("DOUBLE JEOPARDY FOR {}", childPair.getSecond().getDisplayName().getString());
                        doubleJeopardy.getAndIncrement();
                    } else {
                        acceptItemAtParent(event, childPair.getSecond(), parent, (Boolean)childPair.getFirst());
                        firstTryIdiots.getAndIncrement();
                    }

                });
                round2.forEach((parent, childPair) -> {
                    if (event.getParentEntries().contains(childPair.getSecond())) {
                        KrystalCore.LOGGER.debug("HOLY SHIT DAILY DOUBLE?!?! FOR {}", childPair.getSecond().getDisplayName().getString());
                        dailyDouble.getAndIncrement();
                    } else if (event.getParentEntries().contains(parent.asItem().getDefaultInstance())) {
                        acceptItemAtParent(event, childPair.getSecond(), parent, childPair.getFirst());
                        inOnSecond.getAndIncrement();
                    } else {
                        acceptItem(event, childPair.getSecond());
                        failedBoth.getAndIncrement();
                    }

                });
                KrystalCore.LOGGER.debug("{} - {}/{}/{} - {}/{}", new Object[]{tabKey.location(), firstTryIdiots, inOnSecond, failedBoth, doubleJeopardy, dailyDouble});
            }

        }
    }

    private static void acceptItem(BuildCreativeModeTabContentsEvent event, ItemStack item) {
        event.accept(item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void acceptItemAtParent(BuildCreativeModeTabContentsEvent event, ItemStack item, ItemLike parent, boolean behind) {
        ItemStack parentStack = parent.asItem().getDefaultInstance();
        if (behind) {
            event.insertBefore(parentStack, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else {
            event.insertAfter(parentStack, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

    }

    public static class DaisyChain {
        @Getter
        private final ResourceKey<CreativeModeTab> tab;
        private final boolean reversed;
        private final boolean behindParent;
        @Nullable
        private final ItemLike appendParent;
        private final List<ItemStack> chainedItems;

        public DaisyChain(ResourceKey<CreativeModeTab> tab, boolean reversed, boolean behindParent, @Nullable ItemLike appendParent) {
            this.chainedItems = new ArrayList<>();
            this.tab = tab;
            this.behindParent = behindParent;
            this.reversed = reversed;
            this.appendParent = appendParent;
        }

        public DaisyChain(ResourceKey<CreativeModeTab> tab, boolean reversed) {
            this(tab, reversed, reversed, null);
        }

        @Nullable
        public ItemLike getParent() {
            return this.appendParent;
        }

        public void addToChain(ItemStack item) {
            if (!this.reversed) {
                this.chainedItems.add(item);
            } else {
                this.chainedItems.addFirst(item);
            }

        }
    }

    private static class CreativeTabAdditions {
        private final List<ItemStack> appendToEnd = new ArrayList<>();
        private final Multimap<ItemLike, ItemStack> appendBehind = LinkedHashMultimap.create();
        private final Multimap<ItemLike, ItemStack> appendInFront = LinkedHashMultimap.create();
        private final List<ItemStack> addedItems = new ArrayList<>();

        private CreativeTabAdditions() {
        }

        private void addBehind(ItemStack item, ItemLike parent, boolean addFirst) {
            if (this.validateItem(item)) {
                this.appendBehind.put(parent, item);
            }
        }

        private void addInFront(ItemStack item, ItemLike parent, boolean addFirst) {
            if (this.validateItem(item)) {
                this.appendInFront.put(parent, item);
            }
        }

        private void addAtEnd(ItemStack item) {
            if (this.validateItem(item)) {
                this.appendToEnd.add(item);
            }
        }

        public boolean validateItem(ItemStack item) {
            if (this.addedItems.contains(item)) {
                KrystalCore.LOGGER.debug("DUPLICATED ITEM IN TAB - " + item.getDisplayName().tryCollapseToString());
                return false;
            } else {
                this.addedItems.add(item);
                return true;
            }
        }
    }
}
