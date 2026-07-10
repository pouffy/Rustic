package io.github.pouffy.rustic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockRegistryHelper;
import com.pouffydev.krystal_core.foundation.registry.definition.item.ItemRegistryHelper;
import io.github.pouffy.rustic.init.RusticBlockEntities;
import io.github.pouffy.rustic.init.RusticBlocks;
import io.github.pouffy.rustic.init.RusticItems;
import io.github.pouffy.rustic.init.RusticRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

@Mod(Rustic.MODID)
public class Rustic {
    public static Rustic INSTANCE;
    public static final String MODID = "rustic";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final boolean isDevelopmentEnvironment = !FMLEnvironment.production;

    public static final Logger LOGGER = LogManager.getLogger();

    private final IEventBus modEventBus;
    private final RegistryHelper registryHelper;

    public final ItemRegistryHelper itemRegistryHelper;
    public final BlockRegistryHelper blockRegistryHelper;

    public Rustic(IEventBus modEventBus, ModContainer modContainer) {
        KrystalCore.disableDebugFeatures();
        KrystalCore.enableHoneyFluid();
        this.modEventBus = modEventBus;
        INSTANCE = this;
        this.registryHelper = new RegistryHelper(MODID, modEventBus);
        this.itemRegistryHelper = this.registryHelper.getItemHelper();
        this.blockRegistryHelper = this.registryHelper.getBlockHelper();
        RusticCreativeTab.staticInit();
        RusticBlocks.staticInit();
        RusticItems.staticInit();
        RusticBlockEntities.staticInit();
        RusticRecipeTypes.staticInit();

        modEventBus.addListener(RusticBlockEntities::addBlockEntities);
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
}
