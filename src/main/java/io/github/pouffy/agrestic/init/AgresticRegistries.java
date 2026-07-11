package io.github.pouffy.agrestic.init;

import com.mojang.serialization.Lifecycle;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber
public class AgresticRegistries {

    public static final ResourceKey<Registry<FluidTransferType<?>>> FLUID_TRANSFER_TYPE_KEY = createRegistryKey("fluid_container_transfer_type");
    public static final Registry<FluidTransferType<?>> FLUID_TRANSFER_TYPE_REGISTRY = makeSyncedRegistry(FLUID_TRANSFER_TYPE_KEY);

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(FLUID_TRANSFER_TYPE_REGISTRY);
    }

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Agrestic.location(name));
    }
    /**
     * Creates a {@link Registry} that get synchronised to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }
    /**
     * Creates a simple {@link Registry} that <B>won't</B> be synced to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
    private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), true);
    }
}
