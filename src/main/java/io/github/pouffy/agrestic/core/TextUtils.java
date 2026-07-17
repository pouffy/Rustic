package io.github.pouffy.agrestic.core;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TextUtils {

    public static void addFoodEffectTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip) {
        FoodProperties foodProperties = stack.get(DataComponents.FOOD);
        if (foodProperties == null) {
            return;
        }

        if (foodProperties.effects().isEmpty()) {
            return;
        }

        for (FoodProperties.PossibleEffect entry : foodProperties.effects()) {
            MobEffectInstance effect = entry.effect();

            MutableComponent text = Component.translatable(effect.getDescriptionId());

            if (effect.getAmplifier() > 0) {
                text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + effect.getAmplifier()));
            }

            if (!effect.endsWithin(20)) {
                text = Component.translatable("potion.withDuration", text, MobEffectUtil.formatDuration(effect, 1.0F, context.tickRate()));
            }
            text.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting());
            tooltip.add(text);
        }
    }

    public static String transform(ResourceLocation id) {
        return transform(id.getPath());
    }

    public static String transform(String path) {
        int pathLength = path.length();
        StringBuilder stringBuilder = (new StringBuilder(pathLength)).append(Character.toUpperCase(path.charAt(0)));

        for(int i = 1; i < pathLength; ++i) {
            char posChar = path.charAt(i);
            if (posChar == '_') {
                stringBuilder.append(' ');
            } else if (path.charAt(i - 1) == '_') {
                stringBuilder.append(Character.toUpperCase(posChar));
            } else {
                stringBuilder.append(posChar);
            }
        }

        return stringBuilder.toString();
    }
}
