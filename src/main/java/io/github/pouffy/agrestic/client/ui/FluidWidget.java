package io.github.pouffy.agrestic.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pouffy.agrestic.client.ClientFluidHelper;
import io.github.pouffy.agrestic.client.ClientTextHelper;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.init.AgresticDataComponents;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.IAbstractWidgetExtension;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidWidget implements Renderable, GuiEventListener, LayoutElement, IAbstractWidgetExtension, NarratableEntry {
    private final AgresticFluidTank fluidStorage;
    private final Supplier<BlockPos> posSupplier;

    private final int width, height;
    private int x, y;

    private Component message;
    @Getter
    protected boolean isHovered;
    @Getter
    public boolean active = true;
    @Getter
    public boolean visible = true;
    private boolean focused;

    public FluidWidget(AgresticFluidTank fluidStorage, int x, int y, int width, int height, Supplier<BlockPos> posSupplier) {
        this.fluidStorage = fluidStorage;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.posSupplier = posSupplier;
    }

    public static Builder builder(AgresticFluidTank fluidStorage) {
        return new Builder(fluidStorage);
    }

    @Override
    public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) {
            return;
        }
        this.isHovered = guiGraphics.containsPointInScissor(mouseX, mouseY) && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        long fluidAmount = this.fluidStorage.getFluidAmount();

        if (fluidAmount > 0) {
            long fluidCapacity = this.fluidStorage.getCapacity();
            int fluidHeight = Math.round(((float) fluidAmount / fluidCapacity) * this.height);

            BlockPos pos = this.posSupplier.get();
            Level world = Minecraft.getInstance().level;
            if(world == null)
                return;

            int tintColor = ClientFluidHelper.getColor(this.fluidStorage.getFluid(), world, pos);
            float red = (tintColor >> 16 & 0xFF) / 255f;
            float green = (tintColor >> 8 & 0xFF) / 255f;
            float blue = (tintColor & 0xFF) / 255f;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ClientFluidHelper.renderUIFluid(guiGraphics.pose(), this.fluidStorage.getFluid(), red, green, blue, 1.0f, this.x, this.y, this.height, fluidHeight, this.width);
        }

        if(isMouseOver(mouseX, mouseY)) {
            renderSlotHighlight(guiGraphics, -2130706433);
            drawTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    public void renderSlotHighlight(GuiGraphics guiGraphics, int color) {
        guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + width, y + height, color, color, 0);
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {}

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (this.active && this.visible) {
            return !this.isFocused() ? ComponentPath.leaf(this) : null;
        } else {
            return null;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.width) && mouseY < (double)(this.getY() + this.height);
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return GuiEventListener.super.getRectangle();
    }

    @Override
    public NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return this.isHovered ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    public boolean isHoveredOrFocused() {
        return this.isHovered() || this.isFocused();
    }


    public static class Builder {
        private final AgresticFluidTank fluidStorage;
        private Supplier<BlockPos> posSupplier = () -> null;
        private int x, y;
        private int width, height;

        public Builder(AgresticFluidTank fluidStorage) {
            this.fluidStorage = fluidStorage;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder posSupplier(Supplier<BlockPos> posSupplier) {
            this.posSupplier = posSupplier;
            return this;
        }

        public FluidWidget build() {
            return new FluidWidget(this.fluidStorage, this.x, this.y, this.width, this.height, this.posSupplier);
        }
    }

    protected void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        FluidStack fluid = this.fluidStorage.getFluid();
        long fluidAmount = this.fluidStorage.getFluidAmount();
        long fluidCapacity = this.fluidStorage.getCapacity();

        Font font = Minecraft.getInstance().font;
        if(!fluid.isEmpty() && fluidAmount > 0) {
            List<Component> tooltipLines = new ArrayList<>();

            tooltipLines.add(fluid.getHoverName());
            DataComponentPatch patch = this.fluidStorage.getFluid().getComponentsPatch();
            if (!patch.isEmpty()) {
                try {
                    Optional<?> qualityOpt = patch.get(AgresticDataComponents.QUALITY.get());

                    if (qualityOpt != null && qualityOpt.isPresent() && qualityOpt.get() instanceof Float quality) {
                        tooltipLines.add(ClientTextHelper.getQualityTooltip(quality));
                    }
                } catch (Exception ignored) {

                }
            }
            tooltipLines.add(Component.literal("%s/%s mB".formatted(fluidAmount, fluidCapacity)).withStyle(ChatFormatting.GRAY));
            guiGraphics.renderTooltip(font, tooltipLines, Optional.empty(), mouseX, mouseY);
        } else {
            List<Component> tooltipLines = new ArrayList<>();
            tooltipLines.add(Component.literal("0/%s mB".formatted(fluidCapacity)).withStyle(ChatFormatting.GRAY));
            guiGraphics.renderTooltip(font, tooltipLines, Optional.empty(), mouseX, mouseY);
        }
    }
}
