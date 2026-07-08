package io.github.pouffy.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.foundation.registry.RegistryHelper;
import io.github.pouffy.examplemod.init.ModBlocks;
import io.github.pouffy.examplemod.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

@Mod(Example.MODID)
public class Example {
    public static Example INSTANCE;
    public static final String MODID = "examplemod";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final boolean isDevelopmentEnvironment = !FMLEnvironment.production;

    public static final Logger LOGGER = LogManager.getLogger();

    //public final BundleManager bundleManager;

    private final IEventBus modEventBus;
    private final RegistryHelper registryHelper;

    public Example(IEventBus modEventBus, ModContainer modContainer) {
        KrystalCore.disableDebugFeatures();
        this.modEventBus = modEventBus;
        INSTANCE = this;
        this.registryHelper = new RegistryHelper(MODID, modEventBus);
        //this.bundleManager = BundleManager.create(MODID, modEventBus);
        ExampleCreativeTab.staticInit();
        //this.bundleManager.visit();
        ModBlocks.staticInit();
        ModItems.staticInit();
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
