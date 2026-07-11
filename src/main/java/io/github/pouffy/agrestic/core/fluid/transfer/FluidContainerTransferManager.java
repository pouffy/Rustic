package io.github.pouffy.agrestic.core.fluid.transfer;

import com.google.gson.*;
import com.pouffydev.krystal_core.foundation.data.RegistryAccessJsonReloadListener;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.data.DatapackHelpers;
import lombok.Setter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class FluidContainerTransferManager extends RegistryAccessJsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); //json object that will write stuff

    public static final FluidContainerTransferManager INSTANCE = new FluidContainerTransferManager();

    private final Map<ResourceLocation, IFluidContainerTransfer> TRANSFERS = new HashMap<>();

    public List<IFluidContainerTransfer> getTransfers() {
        return TRANSFERS.values().stream().toList();
    }

    public Map<ResourceLocation, IFluidContainerTransfer> transferMap() {
        return TRANSFERS;
    }

    @Setter
    @Nullable
    private Set<Item> containerItems = Collections.emptySet();

    protected FluidContainerTransferManager() {
        super(GSON, "agrestic/fluid_transfer");
    }

    public void init() {
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, AddReloadListenerEvent.class, e -> e.addListener(this));
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, OnDatapackSyncEvent.class, e -> DatapackHelpers.syncPackets(e, new FluidTransferSyncPacket(this.getContainerItems())));
    }

    protected Set<Item> getContainerItems() {
        if (this.containerItems == null) {
            List<Item> builder = new ArrayList<>();
            Consumer<Item> consumer = builder::add;
            for (IFluidContainerTransfer transfer : getTransfers()) {
                transfer.addRepresentativeItems(consumer);
            }
            this.containerItems = Set.copyOf(builder);
        }
        return this.containerItems;
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> jsonMap, RegistryAccess registryAccess) {
        long time = System.nanoTime();
        RegistryOps<JsonElement> registryops = this.makeConditionalOps();
        Map<ResourceLocation, JsonElement> map = new HashMap<>();
        for (var e : jsonMap.entrySet()) {
            map.put(e.getKey(), e.getValue().deepCopy());
        }

        Map<ResourceLocation, IFluidContainerTransfer> transfers = new HashMap<>();

        for (var e : map.entrySet()) {
            var json = e.getValue();

            Optional<WithConditions<IFluidContainerTransfer>> result = IFluidContainerTransfer.CONDITIONAL_CODEC.parse(RegistryOps.create(registryops, registryAccess), json).getOrThrow(JsonParseException::new);

            result.ifPresentOrElse((c) -> {
                IFluidContainerTransfer transfer = c.carrier();
                transfers.put(e.getKey(), transfer);
            }, () -> Agrestic.LOGGER.debug("Skipping loading fluid transfer {} as its conditions were not met", e.getKey()));
        }

        TRANSFERS.clear();
        TRANSFERS.putAll(transfers);
        this.containerItems = null;
        Agrestic.LOGGER.info("Loaded {} fluid transfers in {} ms", TRANSFERS.size(), (System.nanoTime() - time) / 1000000f);
    }

    public boolean mayHaveTransfer(ItemLike item) {
        return getContainerItems().contains(item.asItem());
    }

    public boolean mayHaveTransfer(ItemStack stack) {
        return getContainerItems().contains(stack.getItem());
    }

    @Nullable
    public IFluidContainerTransfer getTransfer(ItemStack stack, FluidStack fluid) {
        for (IFluidContainerTransfer transfer : getTransfers()) {
            if (transfer.matches(stack, fluid)) {
                return transfer;
            }
        }
        return null;
    }
}
