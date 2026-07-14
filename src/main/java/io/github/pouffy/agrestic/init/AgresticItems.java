package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemDefinition;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.AgresticEnumParams;
import io.github.pouffy.agrestic.common.item.HerbItem;
import io.github.pouffy.agrestic.core.item.AgresticFoodItem;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
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

    public static final ItemDefinition<SignItem> OLIVE_SIGN = register("olive_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.OLIVE.sign().get(), AgresticBlocks.OLIVE.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> OLIVE_HANGING_SIGN = register("olive_hanging_sign", () -> new HangingSignItem(AgresticBlocks.OLIVE.hangingSign().get(), AgresticBlocks.OLIVE.hangingWallSign().get(), new Item.Properties().stacksTo(16)));
    public static final ItemDefinition<Item> OLIVE_BOAT = register("olive_boat", (p) -> new BoatItem(false, AgresticEnumParams.OLIVE_BOAT_TYPE.getValue(), p.stacksTo(1)));
    public static final ItemDefinition<Item> OLIVE_CHEST_BOAT = register("olive_chest_boat", (p) -> new BoatItem(true, AgresticEnumParams.OLIVE_BOAT_TYPE.getValue(), p.stacksTo(1)), "Olive Boat with Chest");

    public static final ItemDefinition<SignItem> IRONWOOD_SIGN = register("ironwood_sign", () -> new SignItem(new Item.Properties().stacksTo(16), AgresticBlocks.IRONWOOD.sign().get(), AgresticBlocks.IRONWOOD.wallSign().get()));
    public static final ItemDefinition<HangingSignItem> IRONWOOD_HANGING_SIGN = register("ironwood_hanging_sign", () -> new HangingSignItem(AgresticBlocks.IRONWOOD.hangingSign().get(), AgresticBlocks.IRONWOOD.hangingWallSign().get(), new Item.Properties().stacksTo(16)));
    public static final ItemDefinition<Item> IRONWOOD_BOAT = register("ironwood_boat", (p) -> new BoatItem(false, AgresticEnumParams.IRONWOOD_BOAT_TYPE.getValue(), p.stacksTo(1)));
    public static final ItemDefinition<Item> IRONWOOD_CHEST_BOAT = register("ironwood_chest_boat", (p) -> new BoatItem(true, AgresticEnumParams.IRONWOOD_BOAT_TYPE.getValue(), p.stacksTo(1)), "Ironwood Boat with Chest");

    public static final ItemDefinition<Item> TALLOW = register("tallow", Item::new);
    public static final ItemDefinition<Item> TINY_IRON_DUST = register("tiny_iron_dust", Item::new);
    public static final ItemDefinition<Item> GOLD_DUST = register("gold_dust", Item::new);

    public static final ItemDefinition<HerbItem> CLOUDSBLUFF = register("cloudsbluff", (p) -> new HerbItem(AgresticBlocks.CLOUDSBLUFF.get(), p.food(AgresticFoodValues.CLOUDSBLUFF)));
    public static final ItemDefinition<HerbItem> CORE_ROOT = register("core_root", (p) -> new HerbItem(AgresticBlocks.CORE_ROOT.get(), p.food(AgresticFoodValues.CORE_ROOT)));
    public static final ItemDefinition<HerbItem> GINSENG = register("ginseng", (p) -> new HerbItem(AgresticBlocks.GINSENG.get(), p.food(AgresticFoodValues.GINSENG)));
    public static final ItemDefinition<HerbItem> MARSH_MALLOW = register("marsh_mallow", (p) -> new HerbItem(AgresticBlocks.MARSH_MALLOW.get(), p.food(AgresticFoodValues.MARSH_MALLOW)));

    //public static final ItemDefinition<AgresticFoodItem> IRON_BERRIES = register("iron_berries", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.IRON_BERRIES)));
    public static final ItemDefinition<AgresticFoodItem> OLIVES = register("olives", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.OLIVES)));
    public static final ItemDefinition<AgresticFoodItem> CHILLI_PEPPER = register("chilli_pepper", (p) -> new AgresticFoodItem(p.food(AgresticFoodValues.CHILLI_PEPPER)) {
        @Override
        public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
            if (!world.isClientSide()) {
                RandomSource random = world.getRandom();
                if (random.nextInt(24) == 0) {
                    user.hurt(world.damageSources().onFire(), 1.0F);
                }
            }
            return super.finishUsingItem(stack, world, user);
        }
    });

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
}
