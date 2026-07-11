package io.github.pouffy.agrestic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockRegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemRegistryHelper;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidContainerTransferManager;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferSyncPacket;
import io.github.pouffy.agrestic.init.*;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

@Mod(Agrestic.MODID)
public class Agrestic {
    public static Agrestic INSTANCE;
    public static final String MODID = "agrestic";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final boolean isDevelopmentEnvironment = !FMLEnvironment.production;

    public static final Logger LOGGER = LogManager.getLogger();

    private final IEventBus modEventBus;
    private final RegistryHelper registryHelper;

    public final ItemRegistryHelper itemRegistryHelper;
    public final BlockRegistryHelper blockRegistryHelper;

    public Agrestic(IEventBus modEventBus, ModContainer modContainer) {
        KrystalCore.disableDebugFeatures();
        KrystalCore.enableHoneyFluid();
        FluidContainerTransferManager.INSTANCE.init();
        this.modEventBus = modEventBus;
        INSTANCE = this;
        this.registryHelper = new RegistryHelper(MODID, modEventBus);
        this.itemRegistryHelper = this.registryHelper.getItemHelper();
        this.blockRegistryHelper = this.registryHelper.getBlockHelper();
        AgresticCreativeTab.staticInit();
        AgresticBlocks.staticInit();
        AgresticItems.staticInit();
        AgresticBlockEntities.staticInit();
        AgresticRecipeTypes.staticInit();
        AgresticFluidTransferTypes.staticInit();
        AgresticIngredientTypes.staticInit();

        modEventBus.addListener(AgresticBlockEntities::addBlockEntities);
    }

    @SubscribeEvent
    private void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Agrestic.MODID).versioned("1.0");

        registrar.playToClient(FluidTransferSyncPacket.TYPE, FluidTransferSyncPacket.STREAM_CODEC, FluidTransferSyncPacket::handle);
    }

    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static RegistryHelper getRegistryHelper() {
        return INSTANCE.registryHelper;
    }

    @Contract("_ -> new")
    public static ResourceLocation location(String path) {
        if (path.contains(":")) {
            return ResourceLocation.tryParse(path);
        }
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String makeDescriptionId(String base, String name) {
        return Util.makeDescriptionId(base, location(name));
    }
}
