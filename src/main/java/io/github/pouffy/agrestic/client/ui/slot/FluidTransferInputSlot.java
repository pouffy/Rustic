package io.github.pouffy.agrestic.client.ui.slot;

import io.github.pouffy.agrestic.core.block.AgresticContainerBlockEntity;
import io.github.pouffy.agrestic.core.fluid.*;
import io.github.pouffy.agrestic.core.item.AgresticItemContainer;
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
        AgresticItemContainer itemContainer = getBlockEntity().newContainer();
        if (!stack.isEmpty()) {
            SoundEvent success = null;
            ItemStack resultStack = ItemStack.EMPTY;
            int amountForSlot = container.getItem(outSlot).getCount();
            for (int i = 1; i <= stack.getCount(); i++) {
                ItemStack existing = container.getItem(outSlot);
                if (!existing.isEmpty() && existing.getCount() + 1 > existing.getMaxStackSize()) {
                    super.set(stack);
                    break;
                }
                FluidTransfer simulatedResult = FluidHelper.tryInteractWithTank(getBlockEntity().getLevel(), stack, tank, true);
                if (simulatedResult != null) {
                    if (itemContainer.canInsert(outSlot, simulatedResult.stack())) {
                        FluidTransfer actualResult = FluidHelper.tryInteractWithTank(getBlockEntity().getLevel(), stack, tank, false);
                        if (actualResult != null) {
                            container.removeItem(getSlot(), 1);
                            ItemStack result = actualResult.stack().copyWithCount(1);
                            if (resultStack.isEmpty()) {
                                resultStack = result;
                                amountForSlot = 1;
                            } else {
                                amountForSlot++;
                            }
                            success = actualResult.getSound();
                        }
                    } else break;
                }
            }
            if (!resultStack.isEmpty()) {
                container.setItem(outSlot, resultStack.copyWithCount(amountForSlot));
            }
            if (success != null) FluidHelper.playUISound(player, success);
        }
    }
}
