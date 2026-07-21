package io.github.pouffy.agrestic.core.booze;

import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticDamageTypes;
import io.github.pouffy.agrestic.init.AgresticEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class ExtraBoozeRunnables {

    public static void ironWine(BoozeBottleItem.BoozeConsumptionContext context) {
        float quality = context.quality();
        Player player = context.player();
        if (quality >= 0.5F) {
            float absorption = 10F * (Math.max((quality - 0.5F) * 2F, 0F));
            player.setAbsorptionAmount(Math.clamp(player.getAbsorptionAmount() + absorption, player.getAbsorptionAmount(), 20F));
        } else {
            float damage = 10F * (Math.max(Math.abs(quality - 0.5F) + 0.1F, 0.25F));
            player.hurt(player.damageSources().magic(), damage);
        }
    }

    public static void sweetBerryWine(BoozeBottleItem.BoozeConsumptionContext context) {
        float quality = context.quality();
        Player player = context.player();
        if (quality >= 0.5F) {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (effect.getEffect().value().isBeneficial() && effect.getAmplifier() < 2) {
                    player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() + 1, effect.isAmbient(), effect.isVisible()));
                }
            }
        } else {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (effect.getEffect().value().isBeneficial()) {
                    if (effect.getAmplifier() > 0) {
                        player.removeEffect(effect.getEffect());
                        player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() - 1, effect.isAmbient(), effect.isVisible()));
                    } else if (effect.getAmplifier() == 0) {
                        player.removeEffect(effect.getEffect());
                    }
                }
            }
        }
    }

    public static void wine(BoozeBottleItem.BoozeConsumptionContext context) {
        float quality = context.quality();
        Player player = context.player();
        if (quality >= 0.5F) {
            int durationIncrease = 600 + ((int) (2400 * ((quality - 0.5F) * 2F)));
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (effect.getEffect().value().isBeneficial() && effect.getDuration() < 12000) {
                    int duration = Math.clamp(effect.getDuration() + durationIncrease, effect.getDuration(), 12000);
                    player.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
                }
            }
        } else {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                int durationDecrease = (int) (2400 * (Math.abs(quality - 0.6)));
                if (effect.getEffect().value().isBeneficial()) {
                    int duration = effect.getDuration() - durationDecrease;
                    if (duration > 0) {
                        player.removeEffect(effect.getEffect());
                        player.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
                    } else {
                        player.removeEffect(effect.getEffect());
                    }
                }
            }
        }
    }

    public static void ambrosia(BoozeBottleItem.BoozeConsumptionContext context) {
        float quality = context.quality();
        Player player = context.player();
        if (quality > 0.99F) {
            player.removeEffect(AgresticEffects.UNDYING);
        }
        if (quality < 0.5f) {
            player.hurt(player.damageSources().source(AgresticDamageTypes.BAD_AMBROSIA), Float.MAX_VALUE);
        }

    }
}
