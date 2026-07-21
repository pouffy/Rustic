package io.github.pouffy.agrestic.common.item;

import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import io.github.pouffy.agrestic.init.AgresticEffects;
import lombok.Getter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class BoozeBottleItem extends DrinkableItem {
    @Getter
    private final Function<BoozeConsumptionContext, FoodProperties> consumptionProperties;
    @Getter @Nullable
    private Consumer<BoozeConsumptionContext> runnable = null;

    @Getter
    protected float inebriationChance = 0.5f;

    public BoozeBottleItem(Properties properties, Function<BoozeConsumptionContext, FoodProperties> consumptionProperties) {
        super(properties);
        this.consumptionProperties = consumptionProperties;
    }

    public BoozeBottleItem setInebriationChance(float chance) {
        this.inebriationChance = chance;
        return this;
    }

    public BoozeBottleItem withRunnable(Consumer<BoozeConsumptionContext> runnable) {
        this.runnable = runnable;
        return this;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide && user instanceof Player player) {
            float quality = stack.getOrDefault(AgresticDataComponents.QUALITY, BrewingBarrelRecipe.DEFAULT_QUALITY);
            BoozeConsumptionContext context = new BoozeConsumptionContext(world, player, quality);
            if (getRunnable() != null) {
                getRunnable().accept(context);
            }
            player.eat(world, stack, getConsumptionProperties().apply(context));
            inebriate(world, player, quality);
        }
        return super.finishUsingItem(stack, world, user);
    }

    public void inebriate(Level world, Player player, float quality) {
        int duration = (quality  >= 0.5f)
                ? ((int) (12000 * (Math.max(1 - Math.abs(quality - 0.75F), 0.5F))))
                : ((int) (12000 * (Math.max(1 - (quality * 0.5F), 0.5F))));
        float inebriationChanceMod = Math.clamp(1 - Math.abs(0.67F * (quality - 0.75F)), 0, 1);
        MobEffectInstance tipsyEffect = player.getEffect(AgresticEffects.TIPSY);
        if (world.getRandom().nextFloat() < getInebriationChance() * inebriationChanceMod) {
            if (tipsyEffect == null) {
                player.addEffect(new MobEffectInstance(AgresticEffects.TIPSY, duration, 0, false, false));
            } else if (tipsyEffect.getAmplifier() < 3) {
                player.addEffect(new MobEffectInstance(AgresticEffects.TIPSY, Math.max(duration, tipsyEffect.getDuration()), tipsyEffect.getAmplifier() + 1, false, false));
            }
        }
    }

    public record BoozeConsumptionContext(Level world, Player player, float quality) {}
}
