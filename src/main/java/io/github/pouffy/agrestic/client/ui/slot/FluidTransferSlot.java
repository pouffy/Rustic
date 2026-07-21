package io.github.pouffy.agrestic.client.ui.slot;

import io.github.pouffy.agrestic.core.block.AgresticContainerBlockEntity;
import io.github.pouffy.agrestic.core.fluid.ItemEmptying;
import io.github.pouffy.agrestic.core.fluid.ItemFilling;
import lombok.Getter;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FluidTransferSlot extends Slot {
    @Getter
    private final AgresticContainerBlockEntity blockEntity;
    @Getter
    private final int slot;
    public FluidTransferSlot(AgresticContainerBlockEntity container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.blockEntity = container;
        this.slot = slot;
    }

    public boolean mayPlace(ItemStack stack) {
        return ItemEmptying.canItemBeEmptied(blockEntity.getLevel(), stack) || ItemFilling.canItemBeFilled(blockEntity.getLevel(), stack);
    }
}
