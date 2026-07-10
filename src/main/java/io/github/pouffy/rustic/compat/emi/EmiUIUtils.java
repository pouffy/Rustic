package io.github.pouffy.rustic.compat.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.pouffy.rustic.client.ClientFluidHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class EmiUIUtils {
    public static final float FLUID_PATCH_WIDTH = 16f;
    public static final float FLUID_PATCH_HEIGHT = 16f;

    public static void drawSlotHightlight(GuiGraphics context, int x, int y, int w, int h) {
        context.pose().pushPose();
        context.pose().translate(0, 0, 200);
        RenderSystem.colorMask(true, true, true, false);
        context.fill(x, y, x + w, y + h, -2130706433);
        RenderSystem.colorMask(true, true, true, true);
        context.pose().popPose();
    }

    public static void renderFluid(PoseStack matrices, FluidStack fluid, int x, int areaY, float areaHeight, float fluidHeight, float fluidWidth) {
        renderFluid(matrices, ClientFluidHelper.getSpritesOrMissing(fluid), ClientFluidHelper.getColor(fluid, null, null), x, areaY, areaHeight, fluidHeight, fluidWidth);
    }

    public static void renderFluid(PoseStack matrices, TextureAtlasSprite[] sprites, int color, int x, int areaY, float areaHeight, float fluidHeight, float fluidWidth) {
        if (sprites == null || sprites.length < 1 || sprites[0] == null) {
            return;
        }

        TextureAtlasSprite sprite = sprites[0];
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, sprite.atlasLocation());
        Matrix4f model = matrices.last().pose();
        float r = (float) (color >> 16 & 0xFF) / 256.0F;
        float g = (float) (color >> 8 & 0xFF) / 256.0F;
        float b = (float) (color & 0xFF) / 256.0F;
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        int fluidStripCount = (int) (fluidHeight / FLUID_PATCH_HEIGHT);
        for (int i = 0; i < fluidStripCount; i++) {
            buildFluidHorizontalStrip(bufferBuilder, model, sprite, (float) x,
                    (float) areaY + areaHeight - FLUID_PATCH_HEIGHT * (i + 1), fluidWidth, FLUID_PATCH_HEIGHT, r,
                    g, b);
        }
        float stripRemainder = fluidHeight % FLUID_PATCH_HEIGHT;
        buildFluidHorizontalStrip(bufferBuilder, model, sprite, (float) x,
                (float) areaY + areaHeight - FLUID_PATCH_HEIGHT * fluidStripCount - stripRemainder, fluidWidth,
                stripRemainder, r, g, b);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    private static void buildFluidHorizontalStrip(BufferBuilder bufferBuilder, Matrix4f model,
                                                  TextureAtlasSprite sprite, float x0,
                                                  float y0, float width, float height, float r, float g, float b) {
        int fluidPatchCount = (int) (width / FLUID_PATCH_WIDTH);
        for (int i = 0; i < fluidPatchCount; i++) {
            buildFluidPatch(bufferBuilder, model, sprite, x0 + FLUID_PATCH_WIDTH * i, y0, FLUID_PATCH_WIDTH, height, r,
                    g, b);
        }
        float patchRemainder = width % FLUID_PATCH_WIDTH;
        buildFluidPatch(bufferBuilder, model, sprite, x0 + FLUID_PATCH_WIDTH * fluidPatchCount, y0, patchRemainder,
                height, r, g, b);
    }

    private static void buildFluidPatch(BufferBuilder bufferBuilder, Matrix4f model, TextureAtlasSprite sprite,
                                        float x0, float y0,
                                        float width, float height, float r, float g, float b) {
        float x1 = x0 + width;
        float y1 = y0 + height;
        float uMax = sprite.getU1();
        float vMax = sprite.getV1();
        float spriteWidth = sprite.getU1() - sprite.getU0();
        float spriteHeight = sprite.getV1() - sprite.getV0();
        float uMin = uMax - spriteWidth * width / 16f;
        float vMin = vMax - spriteHeight * height / 16f;
        bufferBuilder.addVertex(model, x0, y1, 1.0F).setUv(uMin, vMax).setColor(r, g, b, 1.0F);
        bufferBuilder.addVertex(model, x1, y1, 1.0F).setUv(uMax, vMax).setColor(r, g, b, 1.0F);
        bufferBuilder.addVertex(model, x1, y0, 1.0F).setUv(uMax, vMin).setColor(r, g, b, 1.0F);
        bufferBuilder.addVertex(model, x0, y0, 1.0F).setUv(uMin, vMin).setColor(r, g, b, 1.0F);
    }
}
