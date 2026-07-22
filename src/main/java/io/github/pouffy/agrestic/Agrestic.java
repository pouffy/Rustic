package io.github.pouffy.agrestic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import io.github.pouffy.agrestic.common.data.EvaporationBoosterManager;
import io.github.pouffy.agrestic.compat.appleskin.AppleskinIntegration;
import io.github.pouffy.agrestic.core.CompatManager;
import io.github.pouffy.agrestic.init.*;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

import java.util.UUID;

@Mod(Agrestic.MODID)
public class Agrestic {
    public static final UUID KRYSTAL_UUID = UUID.fromString("37b8527a-8e9b-4b23-9d3b-6196c9d70551");
    public static Agrestic INSTANCE;
    public static final String MODID = "agrestic";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final boolean isDevelopmentEnvironment = !FMLEnvironment.production;

    public static final Logger LOGGER = LogManager.getLogger();

    private final IEventBus modEventBus;
    private final RegistryHelper registryHelper;

    public Agrestic(IEventBus modEventBus, ModContainer modContainer) {
        KrystalCore.disableDebugFeatures();
        KrystalCore.enableHoneyFluid();
        EvaporationBoosterManager.INSTANCE.init();
        this.modEventBus = modEventBus;
        INSTANCE = this;
        this.registryHelper = new RegistryHelper(MODID, modEventBus);
        AgresticCreativeTab.staticInit();
        AgresticFluids.Types.staticInit(modEventBus);
        AgresticFluids.staticInit(modEventBus);
        AgresticBlocks.staticInit(modEventBus);
        AgresticItems.staticInit(modEventBus);
        AgresticCreativeTab.populate();
        AgresticBlockEntities.staticInit();
        AgresticRecipeTypes.staticInit();
        AgresticIngredientTypes.staticInit();
        AgresticDataComponents.staticInit();
        AgresticEffects.staticInit();
        AgresticAttachmentTypes.staticInit();
        AgresticMenuTypes.staticInit();

        modEventBus.addListener(AgresticBlockEntities::addBlockEntities);

        modContainer.registerConfig(ModConfig.Type.SERVER, AgresticConfig.serverSpec);
        modContainer.registerConfig(ModConfig.Type.CLIENT, AgresticConfig.clientSpec);
        modContainer.registerConfig(ModConfig.Type.COMMON, AgresticConfig.commonSpec);

        registerIntegration();
    }

    private void registerIntegration() {
        if (CompatManager.APPLESKIN.isLoaded()) {
            NeoForge.EVENT_BUS.register(AppleskinIntegration.class);
        }
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
