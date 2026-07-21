package io.github.pouffy.agrestic.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pouffy.agrestic.client.AgresticSprites;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BrewingBarrelScreen extends AbstractContainerScreen<BrewingBarrelMenu> {
    private static final int[] BUBBLE_PROGRESS = new int[]{0, 6, 13, 18, 22, 26, 32};

    private FluidWidget inputFluid;
    private FluidWidget resultFluid;
    private FluidWidget auxiliaryFluid;

    public BrewingBarrelScreen(BrewingBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderFg(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderFg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        inputFluid.render(guiGraphics, mouseX, mouseY, partialTick);
        resultFluid.render(guiGraphics, mouseX, mouseY, partialTick);
        auxiliaryFluid.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        inputFluid = FluidWidget.builder(menu.getBlockEntity().getInputTank())
                .bounds(i + 62, j + 27, 16, 32)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build();
        resultFluid = FluidWidget.builder(menu.getBlockEntity().getResultTank())
                .bounds(i + 116, j + 27, 16, 32)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build();
        auxiliaryFluid = FluidWidget.builder(menu.getBlockEntity().getAuxiliaryTank())
                .bounds(i + 26, j + 35, 16, 16)
                .posSupplier(() -> menu.getBlockEntity().getBlockPos())
                .build();
        addWidget(inputFluid);
        addWidget(resultFluid);
        addWidget(auxiliaryFluid);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AgresticSprites.BREWING_BARREL.location);

        AgresticSprites.BLANK_CONTAINER.blitWithInventory(guiGraphics, this.leftPos, this.topPos);

        // Auxiliary
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 25, this.topPos + 14);
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 25, this.topPos + 34);
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 25, this.topPos + 54);

        AgresticSprites.TINY_ARROW_BASE.blit(guiGraphics, this.leftPos + 44, this.topPos + 37);

        // Input
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 61, this.topPos + 6);
        AgresticSprites.TANK.blit(guiGraphics, this.leftPos + 61, this.topPos + 26);
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 61, this.topPos + 62);

        AgresticSprites.ARROW_BASE.blit(guiGraphics, this.leftPos + 85, this.topPos + 34);

        // Result
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 115, this.topPos + 6);
        AgresticSprites.TANK.blit(guiGraphics, this.leftPos + 115, this.topPos + 26);
        AgresticSprites.SLOT.blit(guiGraphics, this.leftPos + 115, this.topPos + 62);

        AgresticSprites.BUBBLES_BASE.blit(guiGraphics, this.leftPos + 136, this.topPos + 27);

        renderProgress(guiGraphics, this.leftPos, this.topPos);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        int m = this.menu.getBrewingTime();
        if (m > 0) {
            AgresticSprites.TINY_ARROW.blit(guiGraphics, x + 44, y + 36, 0, 0, menu.getScaledAuxArrowProgress(), 10);

            AgresticSprites.ARROW.blit(guiGraphics, x + 85, y + 33, 0, 0, menu.getScaledArrowProgress(), 16);

            int n = BUBBLE_PROGRESS[m / 3 % 7];
            if (n > 0) {
                AgresticSprites.BUBBLES.blit(guiGraphics, x + 136, y + 27 + 32 - n, 0, 32 - n, 16, n);
            }
        }
    }
}
