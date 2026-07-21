package io.github.pouffy.agrestic.compat.appleskin;

import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import squeek.appleskin.api.event.FoodValuesEvent;

public class AppleskinEventHandler {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        modBusListener(modBus);
        gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(AppleskinEventHandler::handlerFoodValuesEvent);
    }

    public static void handlerFoodValuesEvent(FoodValuesEvent event) {
        Player player = event.player;
        ItemStack itemStack = event.itemStack;
        FoodProperties foodProperties = calculateBoozeProps(itemStack, player);
        if (foodProperties != null) {
            event.modifiedFoodProperties = foodProperties;
        }
    }

    private static FoodProperties calculateBoozeProps(ItemStack stack, Player player) {
        if (stack.getItem() instanceof BoozeBottleItem boozeBottleItem) {
            return boozeBottleItem.getConsumptionProperties().apply(new BoozeBottleItem.BoozeConsumptionContext(player.level(), player, stack.getOrDefault(AgresticDataComponents.QUALITY, BrewingBarrelRecipe.DEFAULT_QUALITY)));
        }
        return null;
    }
}
