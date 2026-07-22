package io.github.pouffy.agrestic.client.ui.slot;

import com.pouffydev.krystal_core.foundation.utility.Pair;
import io.github.pouffy.agrestic.core.block.AgresticContainerBlockEntity;
import io.github.pouffy.agrestic.core.fluid.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidTransferInputSlot extends FluidTransferSlot {
    private final AgresticFluidTank tank;
    private final int outSlot;
    private final Player player;

    public FluidTransferInputSlot(AgresticContainerBlockEntity container, int slot, int outSlot, int x, int y, AgresticFluidTank tank, Player player) {
        super(container, slot, x, y);
        this.tank = tank;
        this.outSlot = outSlot;
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (tank.isEmpty()) {
            return ItemEmptying.canItemBeEmptied(getBlockEntity().getLevel(), stack);
        } else {
            if (ItemEmptying.canItemBeEmptied(getBlockEntity().getLevel(), stack)) {
                FluidStack fluid = ItemEmptying.emptyItem(getBlockEntity().getLevel(), stack, true).getFirst();
                return FluidStack.isSameFluidSameComponents(fluid, tank.getFluid()) && tank.getFluidAmount() + fluid.getAmount() <= tank.getCapacity();
            }
            return ItemFilling.canItemBeFilled(getBlockEntity().getLevel(), stack) && ItemFilling.getRequiredAmountForItem(getBlockEntity().getLevel(), stack, tank.getFluid()) != -1;
        }
    }

    @Override
    public void set(ItemStack stack) {
        if (!stack.isEmpty()) {
            int successfulInputs = calcSuccessfulInputs(stack);
            ItemStack forProcessing = stack.copyWithCount(successfulInputs);
            ItemStack remainingInputs = stack.copyWithCount(stack.getCount() - successfulInputs);
            if (successfulInputs > 0) {
                container.removeItem(getSlot(), successfulInputs);
                Pair<ItemStack, SoundEvent> result = processInputs(forProcessing);
                if (!result.getFirst().isEmpty()) {
                    container.setItem(outSlot, result.getFirst());
                }
                if (result.getSecond() != null) FluidHelper.playUISound(player, result.getSecond());
            }
            // Always put back any remaining inputs (may be empty)
            if (!remainingInputs.isEmpty()) {
                setRemainingInSlot(remainingInputs);
            } else {
                setRemainingInSlot(ItemStack.EMPTY);
            }
        }
    }

    private void setRemainingInSlot(ItemStack stack) {
        super.set(stack);
    }

    private int calcSuccessfulInputs(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        var level = getBlockEntity().getLevel();
        FluidTransfer singleSim = FluidHelper.tryInteractWithTank(level, stack.copyWithCount(1), tank, true);
        if (singleSim == null) return 0;
        FluidStack perItemFluid = singleSim.fluid();
        int perAmount = perItemFluid.getAmount();

        int availableByTank;
        if (ItemEmptying.canItemBeEmptied(level, stack)) {
            availableByTank = (tank.getCapacity() - tank.getFluidAmount()) / perAmount;
        } else if (ItemFilling.canItemBeFilled(level, stack)) {
            availableByTank = tank.getFluidAmount() / perAmount;
        } else {
            availableByTank = 0;
        }
        if (availableByTank <= 0) return 0;

        var container = this.getBlockEntity().getContainer();
        if (!container.canInsert()) return 0;
        ItemStack result = singleSim.stack();
        int availableByOutput;
        ItemStack existing = container.getStackInSlot(outSlot);
        if (existing == ItemStack.EMPTY) {
            if (!container.isItemValid(outSlot, result)) return 0;
            availableByOutput = result.getMaxStackSize();
        } else {
            if (!ItemStack.isSameItemSameComponents(existing, result) || !container.isItemValid(outSlot, result)) return 0;
            availableByOutput = existing.getMaxStackSize() - existing.getCount();
        }

        int max = Math.min(stack.getCount(), Math.min(availableByTank, availableByOutput));
        return Math.max(0, max);
    }

    private boolean canInsert(ItemStack stack, int slot) {
        var container = this.getBlockEntity().getContainer();
        if (!getBlockEntity().getContainer().canInsert()) return false;
        boolean stackCheck = true;
        if (container.getStackInSlot(slot) != ItemStack.EMPTY) {
            stackCheck = ItemStack.isSameItemSameComponents(container.getStackInSlot(slot), stack);
        }
        return stackCheck && container.isItemValid(slot, stack);
    }

    private Pair<ItemStack, SoundEvent> processInputs(ItemStack stack) {
        SoundEvent success = null;
        ItemStack resultStack = ItemStack.EMPTY;
        int amountForSlot = 0;
        for (int i = 1; i <= stack.getCount(); i++) {
            FluidTransfer simulatedResult = FluidHelper.tryInteractWithTank(getBlockEntity().getLevel(), stack, tank, true);
            if (simulatedResult != null) {
                FluidTransfer actualResult = FluidHelper.tryInteractWithTank(getBlockEntity().getLevel(), stack, tank, false);
                if (actualResult != null) {
                    ItemStack result = actualResult.stack().copyWithCount(1);
                    if (resultStack.isEmpty()) {
                        resultStack = result;
                        amountForSlot = 1;
                    } else if (!ItemStack.isSameItemSameComponents(resultStack, result)) {
                        break;
                    } else {
                        amountForSlot++;
                    }
                    success = actualResult.getSound();
                }
            }
        }
        return Pair.of(resultStack.copyWithCount(amountForSlot), success);
    }
}
