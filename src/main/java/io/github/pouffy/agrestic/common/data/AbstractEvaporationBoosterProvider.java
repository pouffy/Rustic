package io.github.pouffy.agrestic.common.data;

import com.google.common.collect.Sets;
import com.pouffydev.krystal_core.foundation.data.provider.server.KrysOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractEvaporationBoosterProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final String modid;

    public AbstractEvaporationBoosterProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modid) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "agrestic/evaporation_boosters");
        this.registries = registries;
        this.modid = modid;
    }

    protected abstract void addBoosters(KrysOutput<EvaporationBooster> output, HolderLookup.Provider holderLookup);

    public final CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose((provider) -> this.run(output, provider));
    }

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.addBoosters((location, booster, conditions) -> {
            if (!set.add(location)) {
                throw new IllegalStateException("Duplicate evaporation booster " + location);
            } else {
                list.add(DataProvider.saveStable(output, registries, EvaporationBooster.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(booster, conditions)), pathProvider.json(location)));
            }
        }, registries);

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected void addBooster(KrysOutput<EvaporationBooster> output, String name, EvaporationBooster booster, ICondition... conditions) {
        save(output, booster, ResourceLocation.fromNamespaceAndPath(this.modid, name), conditions);
    }

    public void save(KrysOutput<EvaporationBooster> output, EvaporationBooster booster, ResourceLocation location) {
        output.accept(location, booster);
    }

    public void save(KrysOutput<EvaporationBooster> output, EvaporationBooster booster, ResourceLocation location, ICondition... conditions) {
        output.accept(location, booster, conditions);
    }

    @Override
    public String getName() {
        return "Evaporation Boosters for %s".formatted(this.modid);
    }
}
