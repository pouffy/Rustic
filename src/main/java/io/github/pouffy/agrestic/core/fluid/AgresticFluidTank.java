package io.github.pouffy.agrestic.core.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AgresticFluidTank extends FluidTank {

    private final Consumer<FluidStack> updateCallback;
    private boolean forbidInsertion = false;
    private boolean forbidExtraction = false;

    public AgresticFluidTank(int capacity, Consumer<FluidStack> updateCallback) {
        super(capacity);
        this.updateCallback = updateCallback;
    }

    public AgresticFluidTank forbidInsertion() {
        this.forbidInsertion = true;
        return this;
    }

    public AgresticFluidTank forbidExtraction() {
        this.forbidExtraction = true;
        return this;
    }

    public boolean canInsert() {
        return !this.forbidInsertion;
    }

    public boolean canExtract() {
        return !this.forbidExtraction;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (forbidInsertion)
            return 0;
        return super.fill(resource, action);
    }

    public int forceFill(FluidStack resource, FluidAction action) {
        return super.fill(resource, action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (forbidExtraction)
            return FluidStack.EMPTY;
        return super.drain(resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (forbidExtraction)
            return FluidStack.EMPTY;
        return super.drain(maxDrain, action);
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        updateCallback.accept(getFluid());
    }

    @Override
    public void setFluid(@NotNull FluidStack stack) {
        super.setFluid(stack);
        updateCallback.accept(stack);
    }

    public boolean isFull() {
        return getFluidAmount() == getCapacity();
    }
}
