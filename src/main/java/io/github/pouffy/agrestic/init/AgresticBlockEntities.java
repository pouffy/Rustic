package io.github.pouffy.agrestic.init;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.client.renderer.CrushingTubRenderer;
import io.github.pouffy.agrestic.client.renderer.EvaporatingBasinRenderer;
import io.github.pouffy.agrestic.client.renderer.FluidBarrelRenderer;
import io.github.pouffy.agrestic.common.block.entity.CrushingTubBlockEntity;
import io.github.pouffy.agrestic.common.block.entity.EvaporatingBasinBlockEntity;
import io.github.pouffy.agrestic.common.block.entity.FluidBarrelBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class AgresticBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> HELPER = Agrestic.getRegistryHelper().createRegister(Registries.BLOCK_ENTITY_TYPE);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrushingTubBlockEntity>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> BlockEntityType.Builder.of(CrushingTubBlockEntity::new, AgresticBlocks.CRUSHING_TUB.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EvaporatingBasinBlockEntity>> EVAPORATING_BASIN = HELPER.register("evaporating_basin", () -> BlockEntityType.Builder.of(EvaporatingBasinBlockEntity::new, AgresticBlocks.EVAPORATING_BASIN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidBarrelBlockEntity>> FLUID_BARREL = HELPER.register("fluid_barrel", () -> BlockEntityType.Builder.of(FluidBarrelBlockEntity::new, AgresticBlocks.FLUID_BARREL.get()).build(null));

    public static void addBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, AgresticBlocks.OLIVE.sign().get(), AgresticBlocks.OLIVE.wallSign().get(), AgresticBlocks.IRONWOOD.sign().get(), AgresticBlocks.IRONWOOD.wallSign().get());
        event.modify(BlockEntityType.HANGING_SIGN, AgresticBlocks.OLIVE.hangingSign().get(), AgresticBlocks.OLIVE.hangingWallSign().get(), AgresticBlocks.IRONWOOD.hangingSign().get(), AgresticBlocks.IRONWOOD.hangingWallSign().get());
    }

    public static void staticInit() {}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CrushingTubBlockEntity.registerCapabilities(event);
        EvaporatingBasinBlockEntity.registerCapabilities(event);
        FluidBarrelBlockEntity.registerCapabilities(event);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class ClientOnly {
        @SubscribeEvent
        public static void blockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(CRUSHING_TUB.get(), CrushingTubRenderer::new);
            event.registerBlockEntityRenderer(EVAPORATING_BASIN.get(), EvaporatingBasinRenderer::new);
            event.registerBlockEntityRenderer(FLUID_BARREL.get(), FluidBarrelRenderer::new);
        }
    }
}
