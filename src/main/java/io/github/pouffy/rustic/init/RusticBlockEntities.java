package io.github.pouffy.rustic.init;

import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.client.renderer.CrushingTubRenderer;
import io.github.pouffy.rustic.common.block.entity.CrushingTubBlockEntity;
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
public class RusticBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> HELPER = Rustic.getRegistryHelper().createRegister(Registries.BLOCK_ENTITY_TYPE);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrushingTubBlockEntity>> CRUSHING_TUB = HELPER.register("crushing_tub", () -> BlockEntityType.Builder.of(CrushingTubBlockEntity::new, RusticBlocks.CRUSHING_TUB.get()).build(null));

    public static void addBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, RusticBlocks.OLIVE.sign().get(), RusticBlocks.OLIVE.wallSign().get(), RusticBlocks.IRONWOOD.sign().get(), RusticBlocks.IRONWOOD.wallSign().get());
        event.modify(BlockEntityType.HANGING_SIGN, RusticBlocks.OLIVE.hangingSign().get(), RusticBlocks.OLIVE.hangingWallSign().get(), RusticBlocks.IRONWOOD.hangingSign().get(), RusticBlocks.IRONWOOD.hangingWallSign().get());
    }

    public static void staticInit() {}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CrushingTubBlockEntity.registerCapabilities(event);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class ClientOnly {
        @SubscribeEvent
        public static void blockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(CRUSHING_TUB.get(), CrushingTubRenderer::new);
        }
    }
}
