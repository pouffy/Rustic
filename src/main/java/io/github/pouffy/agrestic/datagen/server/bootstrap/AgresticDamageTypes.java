package io.github.pouffy.agrestic.datagen.server.bootstrap;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class AgresticDamageTypes {
    public static final ResourceKey<DamageType> BAD_AMBROSIA = makeKey("bad_ambrosia");

    private static ResourceKey<DamageType> makeKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Agrestic.location(name));
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(BAD_AMBROSIA, new DamageType("bad_ambrosia", DamageScaling.NEVER, 0.1F));
    }
}
