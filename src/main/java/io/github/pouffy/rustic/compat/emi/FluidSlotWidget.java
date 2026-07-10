package io.github.pouffy.rustic.compat.emi;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FluidSlotWidget extends SlotWidget {
    protected final float fluidFullness;
    protected final @Nullable FluidStack fluid;
    public static final int WIDTH = 18;
    public static final int HEIGHT = 34;
    public static final float FLUID_AREA_WIDTH = 16f;
    public static final float FLUID_AREA_HEIGHT = 32f;

    public FluidSlotWidget(@Nullable FluidStack fluid, int x, int y, long capacity) {
        super(fluid == null ? EmiStack.EMPTY : EmiStack.of(fluid.getFluid(), fluid.getComponentsPatch(), fluid.getAmount()), x, y);
        int amount = fluid == null ? 0 : fluid.getAmount();
        fluidFullness = Math.min((float) ((double) amount / (double) capacity), 1.0f);
        this.fluid = fluid;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (drawBack) {
            RusticEmiPlugin.TANK.render(context, x, y, delta);
        }

        if (fluid != null) {
            EmiUIUtils.renderFluid(context.pose(), fluid, x + 1, y + 1, FLUID_AREA_HEIGHT, fluidFullness * FLUID_AREA_HEIGHT, FLUID_AREA_WIDTH);
        }

        if (this.catalyst) {
            EmiRender.renderCatalystIcon(this.getStack(), context, x + 2, y + 4);
        }

        Bounds bounds = getBounds();
        if (bounds.contains(mouseX, mouseY)) {
            EmiUIUtils.drawSlotHightlight(context, bounds.x() + 1, bounds.y() + 1, bounds.width() - 2,
                    bounds.height() - 2);
        }
    }
}
