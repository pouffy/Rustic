package io.github.pouffy.agrestic.common.item;

import io.github.pouffy.agrestic.client.ClientTextHelper;
import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class BoozeItemColor implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 2) {
            float quality = stack.getOrDefault(AgresticDataComponents.QUALITY, BrewingBarrelRecipe.DEFAULT_QUALITY);
            return ClientTextHelper.getQualityLabelColor(quality);
        }
        return 0xFFFFFFFF;
    }
}
