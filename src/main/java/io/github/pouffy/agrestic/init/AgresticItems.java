package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.AgresticEnumParams;
import io.github.pouffy.agrestic.common.item.ChilliItem;
import io.github.pouffy.agrestic.common.item.DrinkableItem;
import io.github.pouffy.agrestic.common.item.HerbItem;
import io.github.pouffy.agrestic.common.item.TomatoItem;
import io.github.pouffy.agrestic.core.TextUtils;
import io.github.pouffy.agrestic.core.item.AgresticFoodItem;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AgresticItems {
    public static final DeferredRegister.Items HELPER = DeferredRegister.createItems(Agrestic.MODID);
    public static List<ItemDefinition<?>> ITEM_DEFINITIONS = new ArrayList<>();

    // =================================================
    // ||                   OLIVE                     ||
    // =================================================
    public static final ItemDefinition<SignItem> OLIVE_SIGN = register("olive_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.OLIVE.sign().get(), AgresticBlocks.OLIVE.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> OLIVE_HANGING_SIGN = register("olive_hanging_sign", () -> new HangingSignItem(AgresticBlocks.OLIVE.hangingSign().get(), AgresticBlocks.OLIVE.hangingWallSign().get(), new Item.Properties().stacksTo(16)));
    public static final ItemDefinition<Item> OLIVE_BOAT = register("olive_boat", (p) -> new BoatItem(false, AgresticEnumParams.OLIVE_BOAT_TYPE.getValue(), p.stacksTo(1)));
    public static final ItemDefinition<Item> OLIVE_CHEST_BOAT = register("olive_chest_boat", (p) -> new BoatItem(true, AgresticEnumParams.OLIVE_BOAT_TYPE.getValue(), p.stacksTo(1)), "Olive Boat with Chest");

    // =================================================
    // ||                  IRONWOOD                   ||
    // =================================================
    public static final ItemDefinition<SignItem> IRONWOOD_SIGN = register("ironwood_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.IRONWOOD.sign().get(), AgresticBlocks.IRONWOOD.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> IRONWOOD_HANGING_SIGN = register("ironwood_hanging_sign", () -> new HangingSignItem(AgresticBlocks.IRONWOOD.hangingSign().get(), AgresticBlocks.IRONWOOD.hangingWallSign().get(), new Item.Properties().stacksTo(16)));
    public static final ItemDefinition<Item> IRONWOOD_BOAT = register("ironwood_boat", (p) -> new BoatItem(false, AgresticEnumParams.IRONWOOD_BOAT_TYPE.getValue(), p.stacksTo(1)));
    public static final ItemDefinition<Item> IRONWOOD_CHEST_BOAT = register("ironwood_chest_boat", (p) -> new BoatItem(true, AgresticEnumParams.IRONWOOD_BOAT_TYPE.getValue(), p.stacksTo(1)), "Ironwood Boat with Chest");


    public static final ItemDefinition<Item> IRON_DUST = register("iron_dust", Item::new);
    public static final ItemDefinition<Item> TINY_IRON_DUST = register("tiny_iron_dust", Item::new);
    public static final ItemDefinition<Item> GOLD_DUST = register("gold_dust", Item::new);
    public static final ItemDefinition<Item> TINY_GOLD_DUST = register("tiny_gold_dust", Item::new);
    // =================================================
    // ||                   HERBS                     ||
    // =================================================
    public static final ItemDefinition<HerbItem> CLOUDSBLUFF = register("cloudsbluff", (p) -> new HerbItem(AgresticBlocks.CLOUDSBLUFF.get(), p.food(AgresticFoodValues.CLOUDSBLUFF)));
    public static final ItemDefinition<HerbItem> CORE_ROOT = register("core_root", (p) -> new HerbItem(AgresticBlocks.CORE_ROOT.get(), p.food(AgresticFoodValues.CORE_ROOT)));
    public static final ItemDefinition<HerbItem> GINSENG = register("ginseng", (p) -> new HerbItem(AgresticBlocks.GINSENG.get(), p.food(AgresticFoodValues.GINSENG)));
    public static final ItemDefinition<HerbItem> MARSH_MALLOW = register("marsh_mallow", (p) -> new HerbItem(AgresticBlocks.MARSH_MALLOW.get(), p.food(AgresticFoodValues.MARSH_MALLOW)));

    // =================================================
    // ||                AGRICULTURE                  ||
    // =================================================
    public static final ItemDefinition<Item> TALLOW = register("tallow", Item::new);

    public static final ItemDefinition<ItemNameBlockItem> CHILLI_PEPPER_SEEDS = register("chilli_pepper_seeds", (p) -> new ItemNameBlockItem(AgresticBlocks.CHILLI_PEPPERS.get(), p));

    public static final ItemDefinition<AgresticFoodItem> GRAPES = register("grapes", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.GRAPES)));
    public static final ItemDefinition<TomatoItem> TOMATO = register("tomato", (p) -> new TomatoItem(p.food(AgresticFoodValues.TOMATO)));
    public static final ItemDefinition<AgresticFoodItem> IRONBERRIES = register("ironberries", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.IRONBERRIES)));
    public static final ItemDefinition<AgresticFoodItem> OLIVES = register("olives", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.OLIVES)));
    public static final ItemDefinition<ChilliItem> CHILLI_PEPPER = register("chilli_pepper", (p) -> new ChilliItem(p.food(AgresticFoodValues.CHILLI_PEPPER), false));
    public static final ItemDefinition<ChilliItem> GHOST_PEPPER = register("ghost_pepper", (p) -> new ChilliItem(p.food(AgresticFoodValues.GHOST_PEPPER), true));

    // =================================================
    // ||                   BUCKETS                   ||
    // =================================================
    public static final ItemDefinition<BucketItem> APPLE_JUICE_BUCKET = registerBucket("apple_juice", AgresticFluids.APPLE_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> GOLDEN_APPLE_JUICE_BUCKET = registerBucket("golden_apple_juice", AgresticFluids.GOLDEN_APPLE_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> GRAPE_JUICE_BUCKET = registerBucket("grape_juice", AgresticFluids.GRAPE_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> SWEET_BERRY_JUICE_BUCKET = registerBucket("sweet_berry_juice", AgresticFluids.SWEET_BERRY_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> IRONBERRY_JUICE_BUCKET = registerBucket("ironberry_juice", AgresticFluids.IRONBERRY_JUICE, p -> p);
    public static final ItemDefinition<BucketItem> ALE_WORT_BUCKET = registerBucket("ale_wort", AgresticFluids.ALE_WORT, p -> p);
    public static final ItemDefinition<BucketItem> OLIVE_OIL_BUCKET = registerBucket("olive_oil", AgresticFluids.OLIVE_OIL, p -> p);
    public static final ItemDefinition<BucketItem> VANTA_OIL_BUCKET = registerBucket("vanta_oil", AgresticFluids.VANTA_OIL, p -> p);

    // =================================================
    // ||                   BOTTLES                   ||
    // =================================================
    public static final ItemDefinition<DrinkableItem> APPLE_JUICE_BOTTLE = registerBottle("apple_juice", (p) -> new DrinkableItem(p.food(AgresticFoodValues.APPLE_JUICE_BOTTLE).stacksTo(16)));
    public static final ItemDefinition<DrinkableItem> GOLDEN_APPLE_JUICE_BOTTLE = registerBottle("golden_apple_juice", (p) -> new DrinkableItem(p.food(AgresticFoodValues.GOLDEN_APPLE_JUICE_BOTTLE).stacksTo(16).rarity(Rarity.RARE)));
    public static final ItemDefinition<DrinkableItem> GRAPE_JUICE_BOTTLE = registerBottle("grape_juice", (p) -> new DrinkableItem(p.food(AgresticFoodValues.GRAPE_JUICE_BOTTLE).stacksTo(16)));
    public static final ItemDefinition<DrinkableItem> SWEET_BERRY_JUICE_BOTTLE = registerBottle("sweet_berry_juice", (p) -> new DrinkableItem(p.food(AgresticFoodValues.SWEET_BERRY_JUICE_BOTTLE).stacksTo(16)));
    public static final ItemDefinition<DrinkableItem> IRONBERRY_JUICE_BOTTLE = registerBottle("ironberry_juice", (p) -> new DrinkableItem(p.food(AgresticFoodValues.IRONBERRY_JUICE_BOTTLE).stacksTo(16)));
    public static final ItemDefinition<DrinkableItem> ALE_WORT_BOTTLE = registerBottle("ale_wort", (p) -> new DrinkableItem(p.food(AgresticFoodValues.ALE_WORT_BOTTLE).stacksTo(16)));
    public static final ItemDefinition<DrinkableItem> OLIVE_OIL_BOTTLE = registerBottle("olive_oil", (p) -> new DrinkableItem(p.food(AgresticFoodValues.OLIVE_OIL_BOTTLE).stacksTo(16)));
    public static final ItemDefinition<DrinkableItem> VANTA_OIL_BOTTLE = registerBottle("vanta_oil", (p) -> new DrinkableItem(p.food(AgresticFoodValues.VANTA_OIL_BOTTLE).stacksTo(16)));

    public static void staticInit(IEventBus bus) {
        HELPER.register(bus);
    }

    public static <T extends Item> ItemDefinition<T> registerWithoutTab(String name, Supplier<T> item, String customLang) {
        DeferredItem<T> deferred = HELPER.register(name, item);
        ItemDefinition<T> definition = ItemDefinition.fromHolder(deferred, customLang);
        ITEM_DEFINITIONS.add(definition);
        return definition;
    }

    public static <T extends Item> ItemDefinition<T> register(String name, Supplier<T> item, String customLang) {
        return registerWithoutTab(name, item, customLang);
    }

    public static <T extends Item> ItemDefinition<T> register(String name, Function<Item.Properties, T> constructor, String customLang) {
        return register(name, () -> constructor.apply(new Item.Properties()), customLang);
    }

    public static <T extends Item> ItemDefinition<T> register(String name, Function<Item.Properties, T> constructor) {
        return register(name, constructor, "");
    }

    public static <T extends Item> ItemDefinition<T> register(String name, Supplier<T> item) {
        return register(name, item, "");
    }

    public static <T extends Item> ItemDefinition<T> registerBottle(String name, Function<Item.Properties, T> constructor) {
        return register(name + "_bottle", (p) -> constructor.apply(p.craftRemainder(Items.GLASS_BOTTLE)), "Bottle of %s".formatted(TextUtils.transform(name)));
    }
    public static ItemDefinition<BucketItem> registerBucket(String name, Fluid fluid, Function<Item.Properties, Item.Properties> properties) {
        return register(name + "_bucket", (p) -> new BucketItem(fluid, properties.apply(p).craftRemainder(Items.BUCKET).stacksTo(1)), "Bucket of %s".formatted(TextUtils.transform(name)));
    }
}
