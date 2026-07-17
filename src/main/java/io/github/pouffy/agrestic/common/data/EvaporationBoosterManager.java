package io.github.pouffy.agrestic.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pouffydev.krystal_core.foundation.data.RegistryAccessJsonReloadListener;
import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.*;

public class EvaporationBoosterManager extends RegistryAccessJsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); //json object that will write stuff

    public static final EvaporationBoosterManager INSTANCE = new EvaporationBoosterManager();

    private final Map<ResourceLocation, EvaporationBooster> BOOSTERS = new HashMap<>();

    public List<EvaporationBooster> getBoosters() {
        return BOOSTERS.values().stream().toList();
    }

    public Map<ResourceLocation, EvaporationBooster> boosterMap() {
        return BOOSTERS;
    }

    protected EvaporationBoosterManager() {
        super(GSON, "agrestic/evaporation_boosters");
    }

    public void init() {
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, AddReloadListenerEvent.class, e -> e.addListener(this));
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> jsonMap, RegistryAccess registryAccess) {
        long time = System.nanoTime();
        RegistryOps<JsonElement> registryops = this.makeConditionalOps();
        Map<ResourceLocation, JsonElement> map = new HashMap<>();
        for (var e : jsonMap.entrySet()) {
            map.put(e.getKey(), e.getValue().deepCopy());
        }

        Map<ResourceLocation, EvaporationBooster> transfers = new HashMap<>();

        for (var e : map.entrySet()) {
            var json = e.getValue();

            Optional<WithConditions<EvaporationBooster>> result = EvaporationBooster.CONDITIONAL_CODEC.parse(RegistryOps.create(registryops, registryAccess), json).getOrThrow(JsonParseException::new);

            result.ifPresentOrElse((c) -> {
                EvaporationBooster booster = c.carrier();
                transfers.put(e.getKey(), booster);
            }, () -> Agrestic.LOGGER.debug("Skipping loading evaporation booster {} as its conditions were not met", e.getKey()));
        }

        BOOSTERS.clear();
        BOOSTERS.putAll(transfers);
        Agrestic.LOGGER.info("Loaded {} evaporation boosters in {} ms", BOOSTERS.size(), (System.nanoTime() - time) / 1000000f);
    }

    public float getMultiplier(BlockState state) {
        EvaporationBooster booster = getBoosters().stream().filter((eb) -> eb.test(state)).findFirst().orElse(null);
        return booster != null ? booster.multiplier() : 1.0f;
    }
}
