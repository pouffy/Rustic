package io.github.pouffy.agrestic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pouffy.agrestic.client.ClientFluidHelper;
import io.github.pouffy.agrestic.common.block.entity.FluidBarrelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidBarrelRenderer implements BlockEntityRenderer<FluidBarrelBlockEntity> {

    public FluidBarrelRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(FluidBarrelBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        int capacity = blockEntity.getTank().getCapacity();
        FluidStack fluid = blockEntity.getFluidStack();
        if (fluid != null && !fluid.isEmpty()){
            int blockLightIn = (light >> 4) & 0xF;
            int luminosity = Math.max(blockLightIn, fluid.getFluidType().getLightLevel());
            light = (light & 0xF00000) | luminosity << 4;
            TextureAtlasSprite fluidTexture = ClientFluidHelper.getStillTextureOrMissing(fluid);
            int color = ClientFluidHelper.getColor(fluid, blockEntity.getLevel(), blockEntity.getBlockPos());
            ms.pushPose();
            float min = 4f / 16f;
            float max = 12f / 16f;
            float depth = (2 / 16f) + ((fluid.getAmount()) / (float)capacity) * (12f / 16f);
            ClientFluidHelper.renderStillTiledFace(Direction.UP, min, min, max, max, depth, buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS)), ms, light, color, fluidTexture);
            ms.popPose();
        }
    }
}
