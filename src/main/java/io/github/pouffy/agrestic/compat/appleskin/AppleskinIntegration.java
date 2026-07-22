package io.github.pouffy.agrestic.compat.appleskin;

import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import squeek.appleskin.api.event.FoodValuesEvent;

public class AppleskinIntegration {

    @SubscribeEvent
    public static void handlerFoodValuesEvent(FoodValuesEvent event) {
        ItemStack itemStack = event.itemStack;
        FoodProperties foodProperties = calculateBoozeProps(itemStack);
        if (foodProperties != null) {
            event.modifiedFoodProperties = foodProperties;
        }
    }

    private static FoodProperties calculateBoozeProps(ItemStack stack) {
        if (stack.getItem() instanceof BoozeBottleItem boozeBottleItem) {
            return boozeBottleItem.getConsumptionProperties().apply(stack.getOrDefault(AgresticDataComponents.QUALITY, BrewingBarrelRecipe.DEFAULT_QUALITY));
        }
        return null;
    }
}
