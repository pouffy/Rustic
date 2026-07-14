package io.github.pouffy.agrestic.common.item;

import io.github.pouffy.agrestic.AgresticConfig;
import io.github.pouffy.agrestic.core.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class HerbItem extends BlockItem {
    public HerbItem(Block block, Properties properties) {
        super(block, properties);
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (AgresticConfig.CLIENT.foodEffectTooltips.get()) {
            TextUtils.addFoodEffectTooltip(stack, context, tooltipComponents);
        }
    }
}
