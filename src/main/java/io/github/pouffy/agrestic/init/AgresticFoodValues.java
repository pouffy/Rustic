package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;

public class AgresticFoodValues {
    public static final FoodProperties OLIVES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION,200,1,true, false, false),1f).build();
    public static final FoodProperties CHILLI_PEPPER = new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,400,0,true, false, true),1f).build();
    public static final FoodProperties GHOST_PEPPER = new FoodProperties.Builder().alwaysEdible().nutrition(3).saturationModifier(0.4f).effect(() -> new MobEffectInstance(AgresticEffects.FIRE_POWER,400,0),0.95f).build();
    public static final FoodProperties TOMATO = new FoodProperties.Builder().nutrition(4).saturationModifier(0.4f).build();
    public static final FoodProperties GRAPES = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties IRONBERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.4f).effect(() -> new MobEffectInstance(AgresticEffects.FULLMETAL,280,0,true, false, false),1f).build();
    public static final FoodProperties ROOT = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).build();

    public static final FoodProperties CLOUDSBLUFF = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).effect(() -> new MobEffectInstance(MobEffects.LEVITATION,400,1,true, false, false),1f).build();
    public static final FoodProperties CORE_ROOT = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build();
    public static final FoodProperties GINSENG = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build();
    public static final FoodProperties MARSH_MALLOW = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();

    public static final FoodProperties SWEET_BERRY_JUICE_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(1f).alwaysEdible().build();
    public static final FoodProperties GRAPE_JUICE_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.9f).alwaysEdible().build();
    public static final FoodProperties APPLE_JUICE_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(1.2f).alwaysEdible().build();
    public static final FoodProperties IRONBERRY_JUICE_BOTTLE = new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(0.8f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION,280,0,true, false, false),1f).build();
    public static final FoodProperties OLIVE_OIL_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.CONFUSION,600,1,true, false, false),1f).build();
    public static final FoodProperties ALE_WORT_BOTTLE = new FoodProperties.Builder().nutrition(0).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.CONFUSION,400,1,true, false, false),1f).build();
    public static final FoodProperties VANTA_OIL_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.BLINDNESS,600,0,true, false, false),1f).build();
    public static final FoodProperties GOLDEN_APPLE_JUICE_BOTTLE = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(1.8F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
            .alwaysEdible()
            .build();

    public static FoodProperties ale(float quality) {
        if (quality >= 0.5F) {
            float saturation = 4F * quality;
            int duration = 1200 + ((int) (10800 * (Math.max(Math.abs((quality - 0.5F) * 2F), 0F))));
            return new FoodProperties.Builder().nutrition(2).saturationModifier(saturation).effect(() -> new MobEffectInstance(AgresticEffects.FULL, duration), 1.0F).build();
        } else {
            int duration = (int) (6000 * Math.max(1 - quality, 0.25));
            return new FoodProperties.Builder()
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER, duration), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, duration), 1.0F)
                    .build();
        }
    }

    public static FoodProperties cider(float quality) {
        if (quality >= 0.5F) {
            float saturation = 2F * quality;
            int duration = 1200 + ((int) (10800 * (Math.max(Math.abs((quality - 0.5F) * 2F), 0F))));
            return new FoodProperties.Builder().nutrition(1).saturationModifier(saturation).effect(() -> new MobEffectInstance(AgresticEffects.MAGIC_RESISTANCE, duration), 1.0F).build();
        } else {
            int poisonDur = (int) (1200 * Math.max(1 - quality, 0.25F));
            int confusionDur = (int) (6000 * Math.max(1 - quality, 0.25F));
            return new FoodProperties.Builder()
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, poisonDur), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, confusionDur), 1.0F)
                    .build();
        }
    }

    public static FoodProperties ironWine(float quality) {
        if (quality >= 0.5F) {
            float saturation = 2F * quality;
            return new FoodProperties.Builder().nutrition(1).saturationModifier(saturation).build();
        } else {
            int duration = (int) (6000 * Math.max(1 - quality, 0.25));
            return new FoodProperties.Builder().effect(() -> new MobEffectInstance(MobEffects.CONFUSION, duration), 1.0F).build();
        }
    }

    public static FoodProperties mead(float quality) {
        if (quality >= 0.5F) {
            float saturation = 2F * quality;
            int duration = 1200 + ((int) (6000 * (Math.max(Math.abs((quality - 0.5F) * 2F), 0F))));
            return new FoodProperties.Builder().nutrition(1).saturationModifier(saturation).effect(() -> new MobEffectInstance(AgresticEffects.WITHER_WARD, duration), 1.0F).build();
        } else {
            int witherDur = (int) (800 * Math.max(1 - quality, 0.25));
            int confusionDur = (int) (6000 * Math.max(1 - quality, 0.25));
            return new FoodProperties.Builder()
                    .effect(() -> new MobEffectInstance(MobEffects.WITHER, witherDur), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, confusionDur), 1.0F)
                    .build();
        }
    }

    public static FoodProperties sweetBerryWine(float quality) {
        if (quality >= 0.5F) {
            float saturation = 2F * quality;
            return new FoodProperties.Builder().nutrition(1).saturationModifier(saturation).build();
        } else {
            int duration = (int) (6000 * Math.max(1 - quality, 0.25F));
            return new FoodProperties.Builder().effect(() -> new MobEffectInstance(MobEffects.CONFUSION, duration), 1.0F).build();
        }
    }

    public static FoodProperties wine(float quality) {
        if (quality >= 0.5F) {
            float saturation = 2F * quality;
            return new FoodProperties.Builder().nutrition(1).saturationModifier(saturation).build();
        } else {
            int duration = (int) (6000 * Math.max(1 - quality, 0));
            return new FoodProperties.Builder().effect(() -> new MobEffectInstance(MobEffects.CONFUSION, duration), 1.0F).build();
        }
    }

    public static FoodProperties ambrosia(float quality) {
        if (quality >= 0.5F) {
            float saturation = 2F * quality;
            int duration;
            if (quality > 0.99F) {
                duration = 999999;//Short.MAX_VALUE;
            } else {
                duration = (20 * 300) + ((int) (18000 * (Math.max(Math.abs((quality - 0.5F) * 2F), 0.00F))));
            }
            return new FoodProperties.Builder().nutrition(2).saturationModifier(saturation).effect(() -> new MobEffectInstance(AgresticEffects.UNDYING, duration, 0, false, false), 1.0F).build();
        }
        return new FoodProperties.Builder().build();
    }
}
