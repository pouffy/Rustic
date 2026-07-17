package io.github.pouffy.agrestic.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    public static final FoodProperties OLIVE_OIL_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.CONFUSION,580,1,true, false, false),1f).build();
    public static final FoodProperties ALE_WORT_BOTTLE = new FoodProperties.Builder().nutrition(0).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.CONFUSION,580,1,true, false, false),1f).build();
    public static final FoodProperties VANTA_OIL_BOTTLE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).alwaysEdible().effect(() -> new MobEffectInstance(MobEffects.BLINDNESS,580,0,true, false, false),1f).build();
    public static final FoodProperties GOLDEN_APPLE_JUICE_BOTTLE = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.2F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
            .alwaysEdible()
            .build();
}
